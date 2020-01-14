package edu.iastate.cs.egroum.aug;

import static edu.iastate.cs.egroum.aug.AUGBuilderTestUtils.buildAUGsForClassFromSomewhereElse;
import static edu.iastate.cs.egroum.aug.ExtendedAUGTypeUsageExamplePredicate.EAUGUsageExamplesOf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import smu.hongjin.EnhancedAUG;
import smu.hongjin.GraphBuildingUtils;
import smu.hongjin.LiteralsUtils;
import smu.hongjin.SubgraphMiningFormatter;

public class HJPipelineTestDataGraphBuilder {

	@Test
	public void debug() throws IOException {

		String API = "java.io.ObjectOutputStream__writeObject__1";
//		String API = "java.lang.Long__parseLong__1";
//		String API = "java.util.Map__get__1";
//		String API = "java.sql.PreparedStatement__executeUpdate__0";
	

		List<String> pathsToJavaFiles = HJRegressionTestConstants.javaFilesForApi.get(API);
		List<String> pathsToClassPath = HJRegressionTestConstants.javaClassPathForApi.get(API);
		
		if (pathsToJavaFiles.size() != pathsToClassPath.size()) {
			throw new RuntimeException("wrong size");
		}
		
		// PreparedStatement?
//		List<String> pathsToJavaFiles = Arrays.asList(
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/chensun/cf23b99/checkout/CS5430/src//database/SocialNetworkDatabasePosts.java",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/chensun/cf23b99/checkout/CS5430/src//database/DatabaseAdmin.java",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/chensun/cf23b99/checkout/CS5430/src//database/SocialNetworkDatabaseBoards.java"
//				);

		String directoryToLabels = "/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/";
		
		System.out.println("writing graph IDS to ");
		System.out.println(directoryToLabels + API +"_test_graph_id_mapping.txt");
		
		int i = 0;
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(directoryToLabels + API + "_test_formatted.txt"));
				BufferedWriter idMappingWriter = new BufferedWriter(new FileWriter(directoryToLabels + API +"_test_graph_id_mapping.txt" ))) {
			
			Iterator<String> classPathIter = pathsToClassPath.iterator();
			for (String pathToJavaFile : pathsToJavaFiles) {
				System.out.println("writing to " + directoryToLabels + API + "_test_formatted.txt");
				String code = new String(Files.readAllBytes(new File(pathToJavaFile).toPath()));
	
				// get classpath stuff first
				String classpathDirectory = classPathIter.next();
				String[] classpath = null;
				if (classpathDirectory != null) {
					try (Stream<Path> walk = Files.walk(Paths.get(classpathDirectory))) {
	
						List<String> result = walk.filter(Files::isRegularFile)
								.map(x -> x.toString()).collect(Collectors.toList());
	
						classpath = result.toArray(new String[] {});
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				
				Collection<EnhancedAUG> eaugs = buildAUGsForClassFromSomewhereElse(code, pathToJavaFile,
						pathToJavaFile.substring(pathToJavaFile.lastIndexOf("/")),
						new AUGConfiguration() {
							{
								usageExamplePredicate = EAUGUsageExamplesOf(
										GraphBuildingUtils.APIToMethodName.get(API),
										GraphBuildingUtils.APIToClass.get(API));
								maxStatements = 500;
							}
						},
						classpath);
	
	
				System.out.println("\tFound!");
				for (EnhancedAUG eaug : eaugs) {
					System.out.println("\t\tFound " + eaug.aug.name);
				}
				
				// read vertmap
				Map<String, Integer> map1 = new HashMap<>();
				String vertMapDirectory = directoryToLabels + API + "_vertmap.txt";
				List<String> lines = Files.readAllLines(Paths.get(vertMapDirectory));
				for (String line : lines) {
					int lastIndex = line.lastIndexOf(",");
					String token = line.substring(0, lastIndex);
					String countAsStr = line.substring(lastIndex + 1);
					map1.put(token, Integer.parseInt(countAsStr));
				}
					
				
				Map<String, Integer> map2 = new HashMap<>();
				String edgeMapDirectory = directoryToLabels + API + "_edgemap.txt";
				lines = Files.readAllLines(Paths.get(edgeMapDirectory));
				for (String line : lines) {
					String[] splitted = line.split(",");
					map2.put(splitted[0], Integer.parseInt(splitted[1]));
				}
				
				System.out.println("\tDone");
	
				String fileId = "testFile";
				Map<String, String> labels = new HashMap<>();
				for (EnhancedAUG eaug : eaugs) {
					String labelId = fileId + " - " + eaug.aug.name;
					labels.put(labelId, "U");
				}
	//			
				LiteralsUtils.isTestTime = true;
				SubgraphMiningFormatter.convert(eaugs, EnhancedAUG.class, i, map1, map2, fileId, labels, 1,
						writer, idMappingWriter);
				i += eaugs.size();

			}
		} catch (Exception e) {
			System.out.println("err!!");
			throw new RuntimeException(e);
		}
		System.out.println("Now it is truly done!");

	}
}
