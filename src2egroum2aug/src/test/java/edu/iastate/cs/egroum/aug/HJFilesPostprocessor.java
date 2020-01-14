package edu.iastate.cs.egroum.aug;

import static edu.iastate.cs.egroum.aug.AUGBuilderTestUtils.buildAUGsForClassFromSomewhereElse;
import static edu.iastate.cs.egroum.aug.ExtendedAUGTypeUsageExamplePredicate.EAUGUsageExamplesOf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.core.dom.NaiveASTFlattener;
import org.junit.Test;

import com.google.common.collect.Sets;

import edu.iastate.cs.egroum.utils.JavaASTUtil;
import smu.hongjin.EnhancedAUG;
import smu.hongjin.GraphBuildingUtils;
import smu.hongjin.SubgraphMiningFormatter;

/**
 * After mining significant subgraphs, it is likely that there are more graphs
 * that contain useful information for us.
 * 
 * From the uncovered, unlabeled data, find an overlap of clones to label s.t.
 * we maximise the coverage
 * 
 * @author kanghongjin
 *
 */
public class HJFilesPostprocessor {

	Map<String, Set<String>> pathsAndMethodToTokens = new HashMap<>();
	Map<String, Integer> pathsAndMethodToCounts = new HashMap<>();

	@Test
	public void debug() throws IOException {

		for (Entry<String, String> entry : HJPipelineGraphBuilder.directoriesToExamplesOfAPI.entrySet()) {
			String API = entry.getKey();
			String directory = entry.getValue();

			Random r = new java.util.Random();

			Set<String> graphIdsToRead = new HashSet<>();
			List<String> allLines = Files.readAllLines(Paths.get("./output/" + API + "_formatted_result_interesting_unlabeled.txt"));
			for (String line : allLines) {
				graphIdsToRead.add(line);
			}

			Set<String> fileIdsToRead = new HashSet<>();
			allLines = Files.readAllLines(Paths.get("./output/" + API + "_graph_id_mapping.txt"));
			for (String line : allLines) {
				String[] splitted = line.split(",");
				String graphId = splitted[1];
				
				if (!graphIdsToRead.contains(graphId)) {
					continue;
				}
				
				fileIdsToRead.add(splitted[0]);

				try {
					// throw early if id is not integer, which it should be
					Integer.parseInt(splitted[0]);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

//			allLines = Files.readAllLines(Paths.get(directory + "labels.csv"));
//			
//			for (String line : allLines.subList(1, toIndex)) {
//				String[] splitted = line.split(",");
//				String identifier = splitted[0];
//				int number = Integer.parseInt(identifier.split(" -")[0]);
//			}

			System.out.println("Selecting");
			try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
				paths.filter(Files::isRegularFile).forEach(path -> {
					if (!HJPipelineGraphBuilder.isExpectedJavaSourceFileFromRightSubdirectory(path)) {
						return;
					}
					
					System.out.println("path is " + path);

					String after = path.toAbsolutePath().toString().substring(directory.length());
					String id = after.split("/")[0];

					try {
						// throw early if id is not integer, which it should be
						Integer.parseInt(id);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}

					if (!fileIdsToRead.contains(id)) {
						return;
					}

					String code;
					try {
						code = new String(Files.readAllBytes(path));

						String filePath = path.toFile().toString();
						CompilationUnit cu = (CompilationUnit) JavaASTUtil.parseSource(code, filePath,
								filePath.substring(filePath.lastIndexOf("/")), null);

						for (int i = 0; i < cu.types().size(); i++) {
							if (cu.types().get(i) instanceof TypeDeclaration) {
								TypeDeclaration typ = (TypeDeclaration) cu.types().get(i);

								for (MethodDeclaration md : typ.getMethods()) {	
									if (!EAUGUsageExamplesOf(GraphBuildingUtils.APIToMethodName.get(API),
											GraphBuildingUtils.APIToClass.get(API)).matches(md)) {
										continue;
									}

									if (HJFilesPreprocessor.isTooBig(md)) {
										continue;
									}

									boolean isClone = isCloneOfPrevious(filePath, md);

									String sig = JavaASTUtil.buildSignature(md);
									if (!isClone) {}
								}
							}
						}

					} catch (IOException e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					} catch (IllegalStateException ise) {
						ise.printStackTrace();
						System.out.println("Unable to parse java file");
						return;
					}

					
				});
			}
			Optional<Integer> total = pathsAndMethodToCounts.values().stream()
					.reduce((accumulated, current) -> accumulated + current);
			if (!total.isPresent()) {
				throw new RuntimeException("Can't count?");
			}
			

			for (Entry<String, Integer> labelCandidates : pathsAndMethodToCounts.entrySet()) {
				Integer counts = labelCandidates.getValue();
				
				System.out.println("label this ->" + labelCandidates.getKey());
				System.out.println("\tIt covers " + counts + " / " + total.get());
			}

		}
	}

	private boolean isCloneOfPrevious(String path, MethodDeclaration md) {

		Set<String> tokens = new HashSet<>();

		NaiveASTFlattener printer = new NaiveASTFlattener();
		md.accept(printer);
		String body = printer.getResult();
		tokens = Sets.newHashSet(body.split(" "));
		tokens.remove(""); // if there's empty string, just remove it.

		String isCloneOf = null;

		for (Map.Entry<String, Set<String>> entry : pathsAndMethodToTokens.entrySet()) {
			Set<String> tokensInCloneType = entry.getValue();
			float ratio = intersectionRatio(tokensInCloneType, tokens);
			if (ratio > 0.8) { // is clone
				isCloneOf = entry.getKey();
			}
		}

		if (isCloneOf != null) {
			pathsAndMethodToCounts.put(isCloneOf, pathsAndMethodToCounts.get(isCloneOf) + 1);
			return true;
		} else {
			pathsAndMethodToCounts.put(path + "::" + md.getName(), 1);
			pathsAndMethodToTokens.put(path + "::" + md.getName(), tokens);
			return false;
		}
	}

	private static float intersectionRatio(Set<String> canonicalCopy, Set<String> tokens) {
		Set<String> intersection = new HashSet<>();
		intersection.addAll(canonicalCopy);
		intersection.retainAll(tokens);

		int numerator = intersection.size();
		float denominator = Math.min(canonicalCopy.size(), tokens.size());
		return numerator / denominator;
	}
}
