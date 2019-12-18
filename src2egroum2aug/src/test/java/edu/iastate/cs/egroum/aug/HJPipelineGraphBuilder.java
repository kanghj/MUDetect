package edu.iastate.cs.egroum.aug;

import de.tu_darmstadt.stg.mudetect.aug.model.APIUsageExample;
import edu.iastate.cs.egroum.dot.DotGraph;
import smu.hongjin.SubgraphMiningFormatter;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.tu_darmstadt.stg.mudetect.aug.AUGTestUtils.exportAUGsAsPNG;
import static edu.iastate.cs.egroum.aug.AUGBuilderTestUtils.buildAUGsForClassFromSomewhereElse;
import static edu.iastate.cs.egroum.aug.TypeUsageExamplePredicate.usageExamplesOf;

public class HJPipelineGraphBuilder {

	private static Map<String, String> directoriesToExamplesOfAPI = new HashMap<>();
	private static Map<String, String> APIToClass = new HashMap<>();

	static {
		directoriesToExamplesOfAPI.put("java.io.ByteArrayOutputStream__toByteArray",
				"/Users/kanghongjin/repos/github-code-search/src/main/java/com/project/githubsearch/data/java.io.ByteArrayOutputStream__toByteArray/");

		APIToClass.put("java.io.ByteArrayOutputStream__toByteArray", "java.io.ByteArrayOutputStream");
	}

	private int i;

	@Test
	public void debug() throws IOException {

		for (Entry<String, String> entry : directoriesToExamplesOfAPI.entrySet()) {
			String API = entry.getKey();
			String directory = entry.getValue();

			Map<String, Integer> map1 = new HashMap<>();
			Map<String, Integer> map2 = new HashMap<>();

			try (BufferedWriter writer = new BufferedWriter(new FileWriter("./output/" + API + "_formatted.txt"))) {
				
				try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
					paths.filter(Files::isRegularFile).forEach(path -> {
						System.out.println("path is " + path);
						
						try {
							String code = new String(Files.readAllBytes(path));

							String filePath = path.toFile().toString();
							Collection<APIUsageExample> augs = buildAUGsForClassFromSomewhereElse(code,
//									filePath.substring(0, filePath.lastIndexOf("/")),
									filePath,
									filePath.substring(filePath.lastIndexOf("/")),
//									new AUGConfiguration()
									new AUGConfiguration() {{
							            usageExamplePredicate = usageExamplesOf(APIToClass.get(API));
							        }}
							);
//							System.out.println("Going to export!");
//							exportAUGsAsPNG(augs, "./output/", "Debug-aug");

							System.out.println("Done");

							SubgraphMiningFormatter.convert(augs, i, map1, map2, writer);
							i += augs.size();

						} catch (Exception e) {
							System.out.println("err on " + path);
							throw new RuntimeException(e);
						}
					});
				}
			}
			System.out.println("will write to \"./output/\" + API + \"_vertmap.txt\""+ " = ./output/" + API + "_vertmap.txt");
			try (BufferedWriter writer = new BufferedWriter(new FileWriter("./output/" + API + "_vertmap.txt"))) {
				for (Entry<String, Integer> entry1 : map1.entrySet()) {
					writer.write(entry1.getKey() + "," + entry1.getValue() + "\n");
				}
			}
			System.out.println("will write to \"./output/\" + API + \"_edgemap.txt\"");
			try (BufferedWriter writer = new BufferedWriter(new FileWriter("./output/" + API + "_edgemap.txt"))) {
				for (Entry<String, Integer> entry1 : map2.entrySet()) {
					writer.write(entry1.getKey() + "," + entry1.getValue() + "\n");
				}
			}
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
