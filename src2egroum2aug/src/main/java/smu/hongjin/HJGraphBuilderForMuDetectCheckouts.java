package smu.hongjin;

import static smu.hongjin.EAUGUtils.buildAUGsForClassFromSomewhereElse;
import static edu.iastate.cs.egroum.aug.ExtendedAUGTypeUsageExamplePredicate.EAUGUsageExamplesOf;

import java.io.BufferedWriter;
import java.io.File;
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
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;

import edu.iastate.cs.egroum.aug.AUGConfiguration;
import edu.iastate.cs.egroum.aug.EGroumBuilder;
import edu.iastate.cs.egroum.utils.JavaASTUtil;
import smu.hongjin.EnhancedAUG;
import smu.hongjin.LiteralsUtils;

public class HJGraphBuilderForMuDetectCheckouts {
	private static int i;
	static int fileCounts = 0;
	static String output = "/checkouts-xp/alp_extract/";
	

	
	public static void main(String... args) throws IOException {
		buildGraphs(
				Arrays.asList(args));
	}

	public static void buildGraphs(List<String> APIs) throws IOException {
		
		Map<String, String> apiToSpecificMethod = getLongMapping(APIs);
		
		for (String API : APIs) {
			String specificAPIId = apiToSpecificMethod.get(API);
			System.out.println("Building graphs for " + API);
//			Set<String> directories = HJConstants.directoriesToExamplesOfAPI.get(API);
			
			Set<String> directories = new HashSet<>();
			directories.add(output + "mudetectxp_checkouts_" + API + "/files/");
			
			System.out.println("running " + API);

			i = 0;
			fileCounts = 0;


			Map<String, String> labels = new HashMap<>();
			Map<String, Integer> map1 = new HashMap<>();
			Map<String, Integer> map2 = new HashMap<>();

			String APIDirectory = output + "graph_output/" + API + "/";
			new File(APIDirectory).mkdirs();

			for (String directory : directories) {
				// read the labels
				GraphBuildingUtils.readLabels(directory, labels);


				try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
					paths.filter(Files::isRegularFile).forEach(path -> {
						if (!isExpectedJavaSourceFileFromRightSubdirectory(path)) {
							return;
						}
						try {
							String code = new String(Files.readAllBytes(path));

							String filePath = path.toFile().toString();
							System.out.println("next is " + filePath);
							CompilationUnit cu = (CompilationUnit) JavaASTUtil.parseSource(code);
							cu.accept(new ASTVisitor(false) {
								@Override
								public boolean preVisit2(ASTNode node) {
									if (node.getNodeType() == ASTNode.STRING_LITERAL) {
										StringLiteral strLiteral = (StringLiteral) node;
										LiteralsUtils.increaseFreq(strLiteral.getLiteralValue().replaceAll("\n", " "));
									} else if (node.getNodeType() == ASTNode.NUMBER_LITERAL) {
										NumberLiteral numLiteral = (NumberLiteral) node;
										LiteralsUtils.increaseFreq(numLiteral.getToken());
									}

									return true;
								}
							});

						} catch (IOException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						} catch (NullPointerException npe) {
							npe.printStackTrace();
						}

					});
				}

				System.out.println("done first pass to count literals");

			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(APIDirectory + API + "_formatted.txt"));
					BufferedWriter idMappingWriter = new BufferedWriter(
							new FileWriter(APIDirectory + API + "_graph_id_mapping.txt"))) {
				for (String directory : directories) {
	
					String[] splitted = directory.split("/");
					String subIdentifier = splitted[splitted.length - 1];


					try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
						paths.filter(Files::isRegularFile).forEach(path -> {
							if (!isExpectedJavaSourceFileFromRightSubdirectory(path)) {
								return;
							}

							System.out.println("path is " + path);
							fileCounts += 1;
							if (fileCounts % 100 == 0) {
								System.out.println("count is " + fileCounts);
							}

							String after = path.toAbsolutePath().toString().substring(directory.length());
							String id = after.split("/")[0];
							
							String[] splitDots = id.split("\\.");
							id = String.join(".", Arrays.asList(splitDots).subList(0, splitDots.length - 2));

						
							int quantity = 1;

							try {
								String code = new String(Files.readAllBytes(path));

								String filePath = path.toFile().toString();
								EGroumBuilder.USE_FALLBACK = true; // allow EgroumBuilder to use a less informative parse of the source code.
								
								Collection<EnhancedAUG> eaugs = buildAUGsForClassFromSomewhereElse(code, filePath,
										filePath.substring(filePath.lastIndexOf("/")), new AUGConfiguration() {
											{
												usageExamplePredicate = EAUGUsageExamplesOf(
														GraphBuildingUtils.APIToMethodName.get(specificAPIId),
														GraphBuildingUtils.APIToClass.get(specificAPIId));
											}
										});
								System.out.println("\tDone. eaug size=" + eaugs.size());

								String fileId = id;

								int oldI = i;
								i = SubgraphMiningFormatter.convert(eaugs, EnhancedAUG.class, i, map1, map2, fileId,
										labels, quantity, subIdentifier, writer, idMappingWriter);
//							if (i == oldI) {
//								throw new RuntimeException("'i' should be increased i=" + i);
//							}

							} catch (NullPointerException npe) {
								System.out.println("err on " + path);
								npe.printStackTrace();
								System.err.println("err on " + path);
							} catch (Exception e) {
								System.out.println("err on " + path);
								throw new RuntimeException(e);
							}
						});
					}
				}

			}
			System.out.println("will write to  " + APIDirectory + API + "_formatted.txt");
			System.out.println("will write to  " + APIDirectory + API + "_vertmap.txt");
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(APIDirectory + API + "_vertmap.txt"))) {
				for (Entry<String, Integer> entry1 : map1.entrySet()) {
					writer.write(entry1.getKey().trim() + "," + entry1.getValue() + "\n");
				}
			}
			System.out.println("will write to " + APIDirectory + API + "_edgemap.txt");
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(APIDirectory + API + "_edgemap.txt"))) {
				for (Entry<String, Integer> entry1 : map2.entrySet()) {
					writer.write(entry1.getKey().trim() + "," + entry1.getValue() + "\n");
				}
			}
		}
	}

	public static boolean isExpectedJavaSourceFileFromRightSubdirectory(Path path) {
		if (path.endsWith("labels.csv") || path.endsWith("metadata.csv") || path.endsWith("metadata_locations.csv")) {
			System.out.println("Skipping : " + path + ", which is metadata-related file");
			return false;
		}
		
		if (path.toString().contains("/cocci_files/")) {
			System.out.println("Skipping : " + path + ", which contains /cocci_files");
			return false;
		}

		return true;
	}
	
	private static Map<String, String> getLongMapping(Collection<String> APIs) {
		Map<String, String> result = new HashMap<>();
		for (String key : GraphBuildingUtils.APIToMethodName.keySet()) {
			for (String API : APIs) {
				String APIForComparison; // needed because some method names are associated with the interface/superclass etc
				if (API.contains("PdfLayer")) {
					APIForComparison = API.replace("PdfLayer", "PdfDictionary");
				} else {
					APIForComparison = API;
				}
				if (key.contains(APIForComparison)) {
					result.put(API, key);
					System.out.println("inserting mapping " + API + " -> " + key);
				}
			}
			
		}
		 
		return result;
	}
}
