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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import smu.hongjin.EnhancedAUG;
import smu.hongjin.GraphBuildingUtils;
import smu.hongjin.LiteralsUtils;
import smu.hongjin.SubgraphMiningFormatter;

public class HJPipelineCombProjectForAPIUsageGraphBuilder {

	@Test
	public void run() throws IOException {
		List<String> APIs = Arrays.asList(
				"java.io.ObjectOutputStream__writeObject__1", 
				"java.lang.Long__parseLong__1",
				"java.util.Map__get__1",
				"java.util.List__get__1", 
				"java.util.StringTokenizer__nextToken__0", "javax.crypto.Cipher__init__2",
				"java.io.DataOutputStream__<init>__1", 
				"java.sql.PreparedStatement__execute*__0",
				"java.util.Iterator__next__0", 
				"org.jfree.data.statistics.StatisticalCategoryDataset__getMeanValue__2", 
				"java.util.Scanner__next__0",
				"com.itextpdf.text.pdf.PdfArray__getPdfObject__1", "java.sql.ResultSet__next__0",
				"org.apache.lucene.index.SegmentInfos__info__1", "java.lang.Byte__parseByte__1",
				"java.lang.Short__parseShort__1", "java.util.Enumeration__nextElement__0",
				"org.jfree.chart.plot.XYPlot__getRendererForDataset__1",
				"org.jfree.chart.plot.PlotRenderingInfo__getOwner__0",
				"org.jfree.chart.plot.CategoryPlot__getDataset__1",
				"com.itextpdf.text.pdf.PdfDictionary__getAsString__1", "java.nio.ByteBuffer__put__1",
				"java.util.SortedMap__firstKey__0", "org.kohsuke.args4j.spi.Parameters__getParameter__1",
				"java.nio.channels.FileChannel__write__1", "java.io.PrintWriter__write__1",
				"javax.swing.JFrame__setVisible__1", "java.util.Optional__get__0"
				);

		List<String> projects = Arrays.asList(
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/closure/319/",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/itext/5091/",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/lucene/1918/",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jmrtd/51/",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/1231/",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/asterisk-java/304421c/",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/chensun/cf23b99/",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/bcel/24014e5/",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jigsaw/205/",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/testng/677302c/"
				);
		
		for (String project : projects) {
			for (String API : APIs) {
				buildGraphs(API, project);
				
				
			}
			fileContents.clear();
			edu.iastate.cs.egroum.utils.JavaASTUtil.astNodeCache.clear();
		}

	}
	
	static Map<File, String> fileContents = new HashMap<>();

	public static void buildGraphs(String API, String pathToProject) {
		String pathToProjectFiles = pathToProject + "/checkout/";
		String pathToClassPath = pathToProject + "dependencies/";
		
		String[] splittedPath = pathToProject.split("/");
		String projectName = splittedPath[splittedPath.length - 2];
		System.out.println(projectName);

		String outputDirectory = "/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/" + projectName  + "___"
				+ API + "/";
		String directoryToOriginalLabels = "/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/" + API + "/";

		System.out.println("writing graph IDS to ");
		System.out.println(outputDirectory + API + "_combing_test_graph_id_mapping.txt");

		new File(outputDirectory).mkdirs();
		int i = 0;
		try (BufferedWriter writer = new BufferedWriter(
				new FileWriter(outputDirectory + API + "_combing_test_formatted.txt"));
				BufferedWriter idMappingWriter = new BufferedWriter(
						new FileWriter(outputDirectory + API + "_combing_test_graph_id_mapping.txt"))) {

			Collection<File> pathsToJavaFiles = FileUtils.listFiles(new File(pathToProjectFiles), new String[] {"java"}, true);
			
			
			for (File javaFile : pathsToJavaFiles) {
				String pathToJavaFile = javaFile.toString();
				System.out.println("writing to " + outputDirectory + API + "_combing_test_formatted.txt");
				String code;
				if (!fileContents.containsKey(javaFile)) {
					code = new String(Files.readAllBytes(new File(pathToJavaFile).toPath()));
				} else {
					code = fileContents.get(javaFile);
				}

				// get classpath stuff first
				String[] classpath = null;
				List<String> result = new ArrayList<>();
				if (pathToClassPath != null) {
					try (Stream<Path> walk = Files.walk(Paths.get(pathToClassPath))) {

						result = walk.filter(Files::isRegularFile).map(x -> x.toString())
								.collect(Collectors.toList());

						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				String additionalJar = ExtendedAUGTypeUsageExamplePredicate.additionalJar.get(API);
				if (additionalJar != null) {
					result.add(additionalJar);
				}
				classpath = result.toArray(new String[] {});
				
				// use text to perform early return.
				String[] APIsplitted = API.split("__");
				String textToFind = APIsplitted[1]; // method name
				if (!code.contains(textToFind)) {
					continue;
				}
				
				Collection<EnhancedAUG> eaugs = buildAUGsForClassFromSomewhereElse(code, pathToJavaFile,
						pathToJavaFile.substring(pathToJavaFile.lastIndexOf("/")), new AUGConfiguration() {
							{
								usageExamplePredicate = EAUGUsageExamplesOf(GraphBuildingUtils.APIToMethodName.get(API),
										GraphBuildingUtils.APIToClass.get(API));
								maxStatements = 500;
							}
						}, classpath);

				if (!eaugs.isEmpty())  {System.out.println("\tFinished building some eaugs!"); } 
				else {System.out.println("\tNo eaug found.");}
				for (EnhancedAUG eaug : eaugs) {
					System.out.println("\t\tFound " + eaug.aug.name);
				}

				// read vertmap
				Map<String, Integer> map1 = new HashMap<>();
				String vertMapDirectory = directoryToOriginalLabels + API + "_vertmap.txt";
				List<String> lines = Files.readAllLines(Paths.get(vertMapDirectory));
				for (String line : lines) {
					int lastIndex = line.lastIndexOf(",");
					String token = line.substring(0, lastIndex);
					String countAsStr = line.substring(lastIndex + 1);
					map1.put(token, Integer.parseInt(countAsStr));
				}

				Map<String, Integer> map2 = new HashMap<>();
				String edgeMapDirectory = directoryToOriginalLabels + API + "_edgemap.txt";
				lines = Files.readAllLines(Paths.get(edgeMapDirectory));
				for (String line : lines) {
					String[] splitted = line.split(",");
					map2.put(splitted[0], Integer.parseInt(splitted[1]));
				}

				System.out.println("\tDone with one file");

				String fileId = pathToJavaFile;
				Map<String, String> labels = new HashMap<>();
				for (EnhancedAUG eaug : eaugs) {
					String labelId = fileId + " - " + eaug.aug.name;
					labels.put(labelId, "U");
				}
				//
				LiteralsUtils.isTestTime = true;
				i = SubgraphMiningFormatter.convert(eaugs, EnhancedAUG.class, i, map1, map2, fileId, labels, 1, "",
						writer, idMappingWriter);

			}
		} catch (Exception e) {
			System.out.println("err!! .. " + API + " at " + pathToProject);
			throw new RuntimeException(e);
		}
		System.out.println("Now it is truly done!");
	}
}
