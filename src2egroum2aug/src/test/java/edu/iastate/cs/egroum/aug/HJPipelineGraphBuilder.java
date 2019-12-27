package edu.iastate.cs.egroum.aug;

import edu.iastate.cs.egroum.dot.DotGraph;
import smu.hongjin.EnhancedAUG;
import smu.hongjin.SubgraphMiningFormatter;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.iastate.cs.egroum.aug.AUGBuilderTestUtils.buildAUGsForClassFromSomewhereElse;
import static edu.iastate.cs.egroum.aug.TypeUsageExamplePredicate.usageExamplesOf;

public class HJPipelineGraphBuilder {

	public static Map<String, String> directoriesToExamplesOfAPI = new HashMap<>();
	public static Map<String, String> APIToClass = new HashMap<>();

	public static final String examplesRoot = "/workspace/github-code-search/src/main/java/com/project/githubsearch/";

	static {
		directoriesToExamplesOfAPI.put("java.io.ByteArrayOutputStream__toByteArray__0",
				examplesRoot + "java.io.ByteArrayOutputStream__toByteArray__0/");

		// These first 5 cases should be interesting
//		"java.util.StringTokenizer__nextToken__0"  	// need to check for hasNext
//		"javax.crypto.Cipher__init__2"             	// check param value; minority are correct
//		"java.lang.Long__parseLong__1"				// need to catch exception
//		"org.jfree.data.statistics.StatisticalCategoryDataset__getMeanValue__2"  // need to check null
//      "java.io.InputStream__read__1" 				// call "close"
//      ByteArrayOutputStream__toByteArray__0 		// many cases in the MUBench Experiment R; 

		APIToClass.put("java.io.ByteArrayOutputStream__toByteArray__0", "java.io.ByteArrayOutputStream");

//		directoriesToExamplesOfAPI.put("java.util.StringTokenizer__nextToken__0",
//				examplesRoot + "java.util.StringTokenizer__nextToken__0/");
//		APIToClass.put("java.util.StringTokenizer__nextToken__0", "java.util.StringTokenizer");
//		
//		directoriesToExamplesOfAPI.put("javax.crypto.Cipher__init__2",
//				examplesRoot + "javax.crypto.Cipher__init__2/");
//		APIToClass.put("javax.crypto.Cipher__init__2", "javax.crypto.Cipher");
//		
		assert directoriesToExamplesOfAPI.size() == APIToClass.size();
	}

	private int i;

	
	boolean smallSample = false;
	// used to ensure a small sample has enough data
	Map<String, Integer> countsOfLabelsWritten = smallSample ? new HashMap<>() : Collections.emptyMap();


	@Test
	public void build() throws IOException {

		System.out.println("smallSample=" + smallSample);
		for (Entry<String, String> entry : directoriesToExamplesOfAPI.entrySet()) {
			String API = entry.getKey();
			String directory = entry.getValue();

			// read the labels
			Map<String, String> labels = new HashMap<>();
			readLabels(directory, labels);

			// read metadata to know how many copies!
			Map<String, Integer> quantities = new HashMap<>();
			readCounts(directory, quantities);

			Map<String, Integer> map1 = new HashMap<>();
			Map<String, Integer> map2 = new HashMap<>();
			//
			try (BufferedWriter writer = new BufferedWriter(new FileWriter("./output/" + API + "_formatted.txt"))) {

				try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
					paths.filter(Files::isRegularFile).forEach(path -> {
						if (path.endsWith("labels.csv") || path.endsWith("metadata.csv")
								|| path.endsWith("metadata_locations.csv")) {
							System.out.println("Skipping : " + path + ", which is metadata-related file");
							return;
						}
						if (path.toString().contains("/files/")) {
							System.out.println("Skipping : " + path + ", which contains /files");
							return;
						}
						if (!path.toString().endsWith(".java")) {

							System.out.println(
									"Skipping : " + path + ". Unexpected file extension. We only look for java files");
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

						if (!quantities.containsKey(id))
							throw new RuntimeException("unknown quantity of the graph for ID = " + id);
						int quantity = quantities.get(id);

						try {
							String code = new String(Files.readAllBytes(path));

							String filePath = path.toFile().toString();
							Collection<EnhancedAUG> eaugs = buildAUGsForClassFromSomewhereElse(code, filePath,
									filePath.substring(filePath.lastIndexOf("/")),
//									new AUGConfiguration()
									new AUGConfiguration() {
										{
											usageExamplePredicate = usageExamplesOf(APIToClass.get(API));
										}
									});
//							System.out.println("Going to export!");
//							exportAUGsAsPNG(augs, "./output/", "Debug-aug");

							System.out.println("Done");

							String fileId = id;

							if (!smallSample) {
								// always write if `smallSample` is false
								SubgraphMiningFormatter.convert(eaugs, EnhancedAUG.class, i, map1, map2, fileId, labels,
										quantity, writer);
								i += eaugs.size();
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
											quantity, writer);
									i += 1;

								}

							}

						} catch (Exception e) {
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
			System.out.println("will write to \"./output/\" + API + \"_edgemap.txt\"");
			try (BufferedWriter writer = new BufferedWriter(new FileWriter("./output/" + API + "_edgemap.txt"))) {
				for (Entry<String, Integer> entry1 : map2.entrySet()) {
					writer.write(entry1.getKey().trim() + "," + entry1.getValue() + "\n");
				}
			}
		}

	}

	private void readCounts(String directory, Map<String, Integer> quantities) {
		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(directory + "metadata/metadata.csv"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Couldn't read labels");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		for (String line : lines) {
			String[] splitted = line.split(",");
			String id = splitted[0];
			int quantity = Integer.parseInt(splitted[1]);

			quantities.put(id, quantity);
		}
	}

	private void readLabels(String directory, Map<String, String> labels) {
		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(directory + "labels.csv"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Couldn't read labels");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		int i = 0;
		for (String line : lines) {
			if (i == 0) {
				i += 1;
				continue;
			}
			String[] splitted = line.split(",");
			String location = splitted[0];
			String label;
			if (splitted.length < 2) {
				label = "U";
			} else {
				label = splitted[1];
			}

			i += 1;
			labels.put(location, label);
		}
	}

	private Collection<EGroumGraph> buildEGroumsForClasses(String[] sources) {
		return Arrays.stream(sources).flatMap(source -> buildEGroumsForClass(source).stream())
				.collect(Collectors.toList());
	}

	private ArrayList<EGroumGraph> buildEGroumsForClass(String source) {
		String projectName = "test";
		String basePath = AUGBuilderTestUtils.class.getResource("/").getFile() + projectName;
		return new EGroumBuilder(new AUGConfiguration()).buildGroums(source, basePath, projectName, null);
	}

	private void exportEGroumsAsPNG(Collection<EGroumGraph> egroums, String pathname, String name) {
		Iterator<EGroumGraph> it = egroums.iterator();
		for (int i = 0; it.hasNext(); i++) {
			EGroumGraph egroum = it.next();
			new DotGraph(egroum).toPNG(new File(pathname), name + "-" + i);
		}
	}
}
