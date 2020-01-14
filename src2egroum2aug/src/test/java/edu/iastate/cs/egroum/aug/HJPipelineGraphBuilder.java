package edu.iastate.cs.egroum.aug;

import edu.iastate.cs.egroum.utils.JavaASTUtil;
import smu.hongjin.EnhancedAUG;
import smu.hongjin.GraphBuildingUtils;
import smu.hongjin.LiteralsUtils;
import smu.hongjin.SubgraphMiningFormatter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static edu.iastate.cs.egroum.aug.AUGBuilderTestUtils.buildAUGsForClassFromSomewhereElse;
import static edu.iastate.cs.egroum.aug.ExtendedAUGTypeUsageExamplePredicate.EAUGUsageExamplesOf;

public class HJPipelineGraphBuilder {

	public static Map<String, String> directoriesToExamplesOfAPI = new HashMap<>();
	

	public static final String examplesRoot = "/Users/kanghongjin/Downloads/github-code-search/";

	static {
//		directoriesToExamplesOfAPI.put("java.util.StringTokenizer__nextToken__0",
//				examplesRoot + "java.util.StringTokenizer__nextToken__0/");

		// These first 5 cases should be interesting
//		"java.util.StringTokenizer__nextToken__0"  	// need to check for hasNext
//		"javax.crypto.Cipher__init__2"             	// check param value; minority are correct
//		"java.lang.Long__parseLong__1"				// need to catch exception
//		"org.jfree.data.statistics.StatisticalCategoryDataset__getMeanValue__2"  // need to check null
//      "java.io.InputStream__read__1" 				// call "close"
//      ByteArrayOutputStream__toByteArray__0 		// many cases in the MUBench Experiment R; 
		// java.io.ObjectOutputStream__writeObject__1 // many cases in the MUBench Experiment R;

//		APIToClass.put("java.io.ByteArrayOutputStream__toByteArray__0", "java.io.ByteArrayOutputStream");
		

//		directoriesToExamplesOfAPI.put("java.util.StringTokenizer__nextToken__0",
//				examplesRoot + "java.util.StringTokenizer__nextToken__0/");
//		APIToClass.put("java.util.StringTokenizer__nextToken__0", "java.util.StringTokenizer");
//		
//		directoriesToExamplesOfAPI.put("javax.crypto.Cipher__init__2",
//				examplesRoot + "javax.crypto.Cipher__init__2/");
//		APIToClass.put("javax.crypto.Cipher__init__2", "javax.crypto.Cipher");
//		
////		
//		directoriesToExamplesOfAPI.put("java.lang.Long__parseLong__1",
//				examplesRoot +	"java.lang.Long__parseLong__1_true/");
//		
		directoriesToExamplesOfAPI.put("java.io.ObjectOutputStream__writeObject__1",
				examplesRoot + "java.io.ObjectOutputStream__writeObject__1[ByteArrayOutputStream]_true/");
//		
//		directoriesToExamplesOfAPI.put("java.util.Map__get__1",
//				examplesRoot + "java.util.Map__get__1_true/");
		
//		directoriesToExamplesOfAPI.put("java.sql.PreparedStatement__executeUpdate__0",
//				examplesRoot + "java.sql.PreparedStatement__executeUpdate__0_true/");
//		assert directoriesToExamplesOfAPI.size() == GraphBuildingUtils.APIToClass.size();
	}

	private int i;
	int fileCounts = 0;

	
	boolean smallSample = false;
	// used to ensure a small sample has enough data
	Map<String, Integer> countsOfLabelsWritten = smallSample ? new HashMap<>() : Collections.emptyMap();


	@Test
	public void build() throws IOException {

		System.out.println("smallSample=" + smallSample);
		for (Entry<String, String> entry : directoriesToExamplesOfAPI.entrySet()) {
			
			i = 0;
			fileCounts = 0;
			
			String API = entry.getKey();
			String directory = entry.getValue();

			// read the labels
			Map<String, String> labels = new HashMap<>();
			GraphBuildingUtils.readLabels(directory, labels);

			// read metadata to know how many copies!
			Map<String, Integer> quantities = new HashMap<>();
			GraphBuildingUtils.readCounts(directory, quantities);

			Map<String, Integer> map1 = new HashMap<>();
			Map<String, Integer> map2 = new HashMap<>();
			
			// first do an initial pass to count literals
			try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
				paths.filter(Files::isRegularFile).forEach(path -> {
					if (!isExpectedJavaSourceFileFromRightSubdirectory(path)) {
						return;
					}
					try {
						String code = new String(Files.readAllBytes(path));
						
						
						ArrayList<EGroumGraph> groums = new ArrayList<>();
						String filePath = path.toFile().toString();
						CompilationUnit cu = (CompilationUnit) JavaASTUtil.parseSource(code, filePath, filePath.substring(filePath.lastIndexOf("/")), null);
						cu.accept(new ASTVisitor(false) {
							@Override
							public boolean preVisit2(ASTNode node) {
								if (node.getNodeType() == ASTNode.STRING_LITERAL) {
									StringLiteral strLiteral = (StringLiteral) node;
									LiteralsUtils.increaseFreq(strLiteral.getLiteralValue());
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
//						throw new RuntimeException(npe);
					}
					
					
				}
				);
			}
			
			System.out.println("done first pass to count literals");
			
			//
			try (BufferedWriter writer = new BufferedWriter(new FileWriter("./output/" + API + "_formatted.txt"));
					BufferedWriter idMappingWriter = new BufferedWriter(new FileWriter("./output/" + API + "_graph_id_mapping.txt" ))) {

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

						try {
							// throw early if id is not integer, which it should be
							Integer.parseInt(id);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}

						if (!quantities.containsKey(id))
							throw new RuntimeException("unknown quantity of the graph for ID = " + id);
						int quantity = quantities.get(id);

						try {
							String code = new String(Files.readAllBytes(path));

							String filePath = path.toFile().toString();
							Collection<EnhancedAUG> eaugs = buildAUGsForClassFromSomewhereElse(code, filePath,
									filePath.substring(filePath.lastIndexOf("/")),
									new AUGConfiguration() {
										{
											usageExamplePredicate = EAUGUsageExamplesOf(
													GraphBuildingUtils.APIToMethodName.get(API),
													GraphBuildingUtils.APIToClass.get(API));
										}
									});
							System.out.println("\tDone");

							String fileId = id;

							if (!smallSample) {
								// always write if `smallSample` is false
								i = SubgraphMiningFormatter.convert(eaugs, EnhancedAUG.class, i, map1, map2, fileId, labels,
										quantity, writer, idMappingWriter);
//								i += eaugs.size();
							} else {
								// otherwise, select a small number only
								for (EnhancedAUG eaug : eaugs) {
									String labelId = fileId + " - " + eaug.aug.name;
									String label = labels.get(labelId);

									countsOfLabelsWritten.putIfAbsent(label, 0);
									if (label.equals("M") || label.equals("C")) {
										if (countsOfLabelsWritten.get(label) > 75)
											continue;
									} else {
										if (countsOfLabelsWritten.get(label) > 150)
											continue;
									}

									countsOfLabelsWritten.put(label, countsOfLabelsWritten.get(label) + 1);
									SubgraphMiningFormatter.convert(Arrays.asList(eaug), EnhancedAUG.class, i, map1, map2, fileId, labels,
											quantity, writer, idMappingWriter);
									i += 1;

								}

							}

						} catch (NullPointerException npe) {
							System.out.println("err on " + path);
							npe.printStackTrace();
							
							
						}catch (Exception e) {
							System.out.println("err on " + path);
							throw new RuntimeException(e);
						}
					});
				}
			}
			System.out.println("will write to  " + " ./output/" + API + "_formatted.txt");
			System.out.println("will write to  " + " ./output/" + API + "_vertmap.txt");
			try (BufferedWriter writer = new BufferedWriter(new FileWriter("./output/" + API + "_vertmap.txt"))) {
				for (Entry<String, Integer> entry1 : map1.entrySet()) {
					writer.write(entry1.getKey().trim() + "," + entry1.getValue() + "\n");
				}
			}
			System.out.println("will write to ./output/" + API + "_edgemap.txt");
			try (BufferedWriter writer = new BufferedWriter(new FileWriter("./output/" + API + "_edgemap.txt"))) {
				for (Entry<String, Integer> entry1 : map2.entrySet()) {
					writer.write(entry1.getKey().trim() + "," + entry1.getValue() + "\n");
				}
			}
		}

	}



	public static boolean isExpectedJavaSourceFileFromRightSubdirectory(Path path) {
		if (path.endsWith("labels.csv") || path.endsWith("metadata.csv")
				|| path.endsWith("metadata_locations.csv")) {
			System.out.println("Skipping : " + path + ", which is metadata-related file");
			return false;
		}
		if (path.toString().contains("/files/")) {
			System.out.println("Skipping : " + path + ", which contains /files");
			return false;
		}
		if (!path.toString().endsWith(".java.txt")) {

			System.out.println(
					"Skipping : " + path + ". Unexpected file extension. We only look for java.txt files");
			return false;
		}
		return true;
	}
	
}
