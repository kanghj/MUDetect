package edu.iastate.cs.egroum.aug;

import static edu.iastate.cs.egroum.aug.AUGBuilderTestUtils.buildAUGsForClassFromSomewhereElse;
import static edu.iastate.cs.egroum.aug.TypeUsageExamplePredicate.usageExamplesOf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.junit.Test;

import smu.hongjin.EnhancedAUG;
import smu.hongjin.SubgraphMiningFormatter;

public class HJPipelineTestDataGraphBuilder {

	@Test
	public void debug() throws IOException {

		String pathToJavaFile = "";
		String API = "java.io.ByteArrayOutputStream__toByteArray__0";
		String directoryToLabels = "/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/";
		
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("testing/test_formatted.txt"))) {
			String code = new String(Files.readAllBytes(new File(pathToJavaFile).toPath()));

			
			Collection<EnhancedAUG> eaugs = buildAUGsForClassFromSomewhereElse(code, pathToJavaFile,
					pathToJavaFile.substring(pathToJavaFile.lastIndexOf("/")),
					new AUGConfiguration() {
						{
							usageExamplePredicate = usageExamplesOf(HJPipelineGraphBuilder.APIToClass.get(API));
						}
					});


			// read vertmap
			Map<String, Integer> map1 = new HashMap<>();
			String vertMapDirectory = directoryToLabels + API + "_vertmap.txt";
			List<String> lines = Files.readAllLines(Paths.get(vertMapDirectory));
			for (String line : lines) {
				String[] splitted = line.split(",");
				map1.put(splitted[0], Integer.parseInt(splitted[1]));
			}
				
			
			Map<String, Integer> map2 = new HashMap<>();
			String edgeMapDirectory = directoryToLabels + API + "_edgemap.txt";
			lines = Files.readAllLines(Paths.get(edgeMapDirectory));
			for (String line : lines) {
				String[] splitted = line.split(",");
				map2.put(splitted[0], Integer.parseInt(splitted[1]));
			}
			
			System.out.println("Done");

			String fileId = "testFile";
			Map<String, String> labels = new HashMap<>();
			for (EnhancedAUG eaug : eaugs) {
				String labelId = fileId + " - " + eaug.aug.name;
				labels.put(labelId, "U");
			}
//			
			SubgraphMiningFormatter.convert(eaugs, EnhancedAUG.class, 0, map1, map2, fileId, labels, 1,
					writer);

		} catch (Exception e) {
			System.out.println("err!!");
			throw new RuntimeException(e);
		}
		

	}
}
