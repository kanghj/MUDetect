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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

//		String pathToJavaFile =
//				"/Users/kanghongjin//Downloads/github-code-search/java.io.ObjectOutputStream__writeObject__1[toByteArray]/774/ch/qos/logback/core/encoder/ObjectStreamEncoder.java.txt";
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/checkout/src/test/java/org/joda/time/TestYearMonth_Basics.java";
//				"/Users/kanghongjin//repos/MUBench/mubench-checkouts/ivantrendafilov-confucius/2c30287/checkout/src/main/java/org/trendafilov/confucius/core/AbstractConfiguration.java";
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/asterisk-java/41461b4/checkout/src/main/java/org/asteriskjava/manager/event/RtcpReceivedEvent.java";
		
		List<String> pathsToJavaFiles = Arrays.asList(
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/checkout/src/test/java/org/joda/time/TestDurationFieldType.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/checkout/src/test/java/org/joda/time/TestDays.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/checkout/src/test/java/org/joda/time/TestDateTimeZone.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/checkout/src/test/java/org/joda/time/TestDateTimeFieldType.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/checkout/src/test/java/org/joda/time/TestDateTimeComparator.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/checkout/src/test/java/org/joda/time/TestDateMidnight_Basics.java",
				"/Users/kanghongjin/Downloads/github-code-search/java.io.ObjectOutputStream__writeObject__1[ByteArrayOutputStream]_true/21924/com/selfimpr/storagedemo/StorageUtil.java.txt",
				"/Users/kanghongjin/Downloads/github-code-search/java.io.ObjectOutputStream__writeObject__1[ByteArrayOutputStream]_true/10755/com/slfuture/carrie/world/logic/Result.java.txt",
				"/Users/kanghongjin/Downloads/github-code-search/java.io.ObjectOutputStream__writeObject__1[ByteArrayOutputStream]_true/1518/esutdal/javanotes/cache/util/DefaultSerializer.java.txt"
				
				);
		
		
		List<String> pathsToClassPath = Arrays.asList(
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/dependencies",
				null,
				null,
				null
				
				);
		
		// for java.lang.Long__parseLong__1
//		List<String> pathsToJavaFiles = Arrays.asList(
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/checkout/src/main/java/weiboclient4j/params/Cid.java",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/checkout/src/main/java/weiboclient4j/params/Id.java",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/checkout/src/main/java/weiboclient4j/params/Uid.java",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/checkout/src/main/java/weiboclient4j/params/Suid.java",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/checkout/src/main/java/weiboclient4j/params/TargetUid.java",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/checkout/src/main/java/weiboclient4j/params/SourceUid.java",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jriecken-gae-java-mini-profiler/80f3a59/checkout/src/main/java/com/google/appengine/tools/appstats/MiniProfilerAppstats.java",
//				
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/ivantrendafilov-confucius/2c30287/checkout/src/main/java/org/trendafilov/confucius/core/AbstractConfiguration.java",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/asterisk-java/41461b4/checkout/src/main/java/org/asteriskjava/manager/event/RtcpReceivedEvent.java"
//				);
//		List<String> pathsToClassPath = Arrays.asList(
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/dependencies",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/dependencies",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/dependencies",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/dependencies",
//				null,
//				null,
//				null,
//				null,
//				null
//				
//				
//				);
//		
		// probably for java.util.Map
		
//		List<String> pathsToJavaFiles = Arrays.asList(
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/closure/319/checkout/src/com/google/javascript/jscomp/SimpleDefinitionFinder.java"
//				,
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jackrabbit/1678/checkout/jackrabbit-jcr-server/src/main/java/org/apache/jackrabbit/webdav/jcr/JcrDavException.java"
//				,
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jfreechart/881//checkout/source/org/jfree/chart/plot/CategoryPlot.java",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/lucene/754/checkout//src/java/org/apache/lucene/search/FieldCacheImpl.java",
//				"/Users/kanghongjin/Downloads/github-code-search/java.util.Map__get__1_true/14640/javaapplication193/SequenceReconstruction.java.txt",
//				"/Users/kanghongjin/Downloads/github-code-search/java.util.Map__get__1_true/8368/com/puzzle/SortMapByValue.java.txt",
//				"/Users/kanghongjin/Downloads/github-code-search/java.util.Map__get__1_true/12190/myWorld/utils/Zpl_test.java.txt",
//				"/Users/kanghongjin/Downloads/github-code-search/java.util.Map__get__1_true/17064/sds/chemicalexport/workers/CsvExport.java.txt",
//				"/Users/kanghongjin/Downloads/github-code-search/java.util.Map__get__1_true/11906/com/yfy/crr/Analyser.java.txt",
//				"/Users/kanghongjin/Downloads/github-code-search/java.util.Map__get__1_true/3362/edu/usc/ini/igc/ENIGMA/ml/MDD/J_SiteDictionary.java.txt"
//				
//				);
//		List<String> pathsToClassPath = Arrays.asList(
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/closure/319/dependencies",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jackrabbit/1678/dependencies",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jfreechart/881/dependencies",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/lucene/754/dependencies",
//				null,
//				null,
//				null,
//				null,
//				null,
//				null
//				);
		if (pathsToJavaFiles.size() != pathsToClassPath.size()) {
			throw new RuntimeException("wrong size");
		}
		
		// PreparedStatement?
//		List<String> pathsToJavaFiles = Arrays.asList(
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/chensun/cf23b99/checkout/CS5430/src//database/SocialNetworkDatabasePosts.java",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/chensun/cf23b99/checkout/CS5430/src//database/DatabaseAdmin.java",
//				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/chensun/cf23b99/checkout/CS5430/src//database/SocialNetworkDatabaseBoards.java"
//				);
		// TDOO ideally only one place to change
		String API = "java.io.ObjectOutputStream__writeObject__1";
//		String API = "java.lang.Long__parseLong__1";
//		String API = "java.util.Map__get__1";
//		String API = "java.sql.PreparedStatement__executeUpdate__0";
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
