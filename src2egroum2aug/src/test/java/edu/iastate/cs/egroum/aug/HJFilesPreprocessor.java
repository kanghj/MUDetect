package edu.iastate.cs.egroum.aug;


import static edu.iastate.cs.egroum.aug.ExtendedAUGTypeUsageExamplePredicate.EAUGUsageExamplesOf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
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
import smu.hongjin.GraphBuildingUtils;

/**
 * HJ: labels should be on individual methods in file
 * 
 * Writes labels.csv and converts the .java.txt files to .java
 * 
 * @author kanghongjin
 *
 */
public class HJFilesPreprocessor {

	int percentageToLabel = 1;

	int percentageToPartitionToTest = 1;

	Map<String, Set<String>> pathsAndMethodToTokens = new HashMap<>();
	Map<String, Integer> pathsAndMethodToCounts = new HashMap<>();

	@Test
	public void debug() {
		preprocess();
	}

	public void preprocess() {
		for (Entry<String, String> entry : HJConstants.directoriesToExamplesOfAPI.entrySet()) {
			String API = entry.getKey();
			String directory = entry.getValue();
			
			if (!directory.endsWith("/")) {
				throw new RuntimeException("Directories should end with '/'");
			}

			Random r = new java.util.Random();

			if (new File(directory + "test_labels.csv").exists()) {
				System.out.println("delete existing test_labels.csv first");
				throw new RuntimeException("remove existing test_labels.csv first");
//				new File(directory + "test_labels.csv").delete();
			}

			if (new File(directory + "labels.csv").exists()) {
				System.out.println("deleting existing labels.csv first");
				new File(directory + "labels.csv").delete();
			}

			try (BufferedWriter trainingDataWriter = new BufferedWriter(new FileWriter(directory + "labels.csv", true));
					BufferedWriter testDataWriter = new BufferedWriter(
							new FileWriter(directory + "test_labels.csv", true))) {

				trainingDataWriter.write("location,label(either 'M' for misuse or 'C' for correct usage)\n");

				try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
					paths.filter(Files::isRegularFile).forEach(path -> {
						if (path.endsWith("labels.csv") || path.endsWith("metadata.csv")
								|| path.endsWith("metadata_locations.csv")) {
							return;
						}
						if (path.toString().contains("/files/")) {
							System.out.println("Skipping : " + path + ", which contains /files");
							return;
						}
						if (!path.toString().endsWith(".java.txt")) {

							System.out.println("Skipping : " + path
									+ ". Unexpected file extension. We only look for java.txt files");
							return;
						}


						System.out.println("path is " + path);

//						System.out.println("\t " + path.toString().indexOf(directory));

						String id = path.toString().substring(directory.length()).split("/")[0];

						String code;
						try {
							code = new String(Files.readAllBytes(path));

							String filePath = path.toFile().toString();
							CompilationUnit cu = (CompilationUnit) JavaASTUtil.parseSource(code, filePath,
									filePath.substring(filePath.lastIndexOf("/")), null);
							
//							String packageName = cu.getPackage().getName().toString();

							Set<String> uniq = new HashSet<>();
							Set<String> clones = new HashSet<>(); // clones can only map to unlabeled

							for (int i = 0; i < cu.types().size(); i++) {
								if (cu.types().get(i) instanceof TypeDeclaration) {
									TypeDeclaration typ = (TypeDeclaration) cu.types().get(i);

									for (MethodDeclaration md : typ.getMethods()) {
										if (!GraphBuildingUtils.APIToMethodName.containsKey(API) || 
												!GraphBuildingUtils.APIToClass.containsKey(API)
												) {
											throw new RuntimeException("GraphBuildingUtils does not have the necessary information");
										}
										if (!EAUGUsageExamplesOf(GraphBuildingUtils.APIToMethodName.get(API),
												GraphBuildingUtils.APIToClass.get(API)).matches(md)) {
//											System.out.println("\tno match " + md.getName());
											continue;
										}

										if (isTooBig(md)) {
											System.out.println("\ttoo big");
											continue;
										}

										boolean isClone = isCloneOfPrevious(filePath, md);

										String sig = JavaASTUtil.buildSignature(md);
										if (isClone) {
											clones.add(id + " - " + typ.getName().getIdentifier() + "." + sig);
										} else {
											uniq.add(id + " - " + typ.getName().getIdentifier() + "." + sig);
										}
									}
								}
							}
							for (String item : uniq) {
								int randomValue = r.nextInt(100);
								if (randomValue < percentageToLabel) { // [0..percentageToLabel-1]
									trainingDataWriter.write(item);
									trainingDataWriter.write(",?\n");
								} else if (randomValue > percentageToLabel
										&& randomValue <= percentageToLabel + percentageToPartitionToTest) {
									// [percentageToLabel .. percentageToLabel + percentageToPartitionToTest-1]
									// for testing
									testDataWriter.write(item);
									testDataWriter.write(",?\n");
								} else {
									trainingDataWriter.write(item);
									trainingDataWriter.write(",\n");
								}
							}

							for (String item : clones) {
								int randomValue = r.nextInt(100);
								if (randomValue > percentageToLabel
										&& randomValue < percentageToLabel + percentageToPartitionToTest) {
									// [percentageToLabel .. percentageToLabel + percentageToPartitionToTest]
									// for testing
									testDataWriter.write(item);
									testDataWriter.write(",?\n");
								} else {
									trainingDataWriter.write(item);
									trainingDataWriter.write(",\n");
								}
							}

						} catch (IOException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						} catch (IllegalStateException ise) {
							ise.printStackTrace();
							System.out.println("Unable to parse java file");
							return;
						} catch (NullPointerException npe) {
							npe.printStackTrace();
							System.out.println("Unable to parse java file");
							return;
						}
					});
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}

			System.out.println(
					"renaming files from .java.txt to .java. It was .java.txt due to some legacy constraint on the github searching tool");
			try (Stream<Path> paths = Files.walk(Paths.get(directory + "files/"))) {
				paths.filter(Files::isRegularFile).forEach(path -> {
					try {
						Files.move(path, Paths.get(path.toString().replace(".java.txt", ".java")));
					} catch (IOException e) {
						System.out.println("Failed to move file");
						e.printStackTrace();
					}
				});
			} catch (IOException e1) {
				System.out.println("Failed to get files in 'files/' directory");
				e1.printStackTrace();
				throw new RuntimeException(e1);
			}

			System.out.println("It's time to start labeling. Fill in " + directory + "labels.csv");

		}
	}

	static int count = 0;

	public static boolean isTooBig(MethodDeclaration md) {
		count = 0;
		md.accept(new ASTVisitor(false) {
			@Override
			public boolean preVisit2(ASTNode node) {
				if (node instanceof Statement) {
					count++;
				}
				return true;
			}
		});
		return count > new AUGConfiguration().maxStatements;
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
			if (ratio > 0.95) { // is clone
				isCloneOf = entry.getKey();
			}
		}

		if (isCloneOf != null) {
			pathsAndMethodToCounts.put(isCloneOf, pathsAndMethodToCounts.get(isCloneOf) + 1);
			System.out.println("\t is clone!");
			return true;
		} else {
			pathsAndMethodToCounts.put(path + "::" + md.getName(), 0);
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
