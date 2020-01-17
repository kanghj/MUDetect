package edu.iastate.cs.egroum.aug;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Paths used for easy testing that the approach still kinda works
 * 
 * @author kanghongjin
 *
 */
public class HJRegressionTestConstants {

	public static Map<String, List<String>> javaFilesForApi = new HashMap<>();
	public static Map<String, List<String>> javaClassPathForApi = new HashMap<>();

	static {
		List<String> pathsToJavaFiles = Arrays.asList(
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/checkout/src/test/java/org/joda/time/TestDurationFieldType.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/checkout/src/test/java/org/joda/time/TestDays.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/checkout/src/test/java/org/joda/time/TestDateTimeZone.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/checkout/src/test/java/org/joda/time/TestDateTimeFieldType.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/checkout/src/test/java/org/joda/time/TestDateTimeComparator.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/checkout/src/test/java/org/joda/time/TestDateMidnight_Basics.java",
				"/Users/kanghongjin/Downloads/github-code-search/java.io.ObjectOutputStream__writeObject__1[ByteArrayOutputStream]_true/21924/com/selfimpr/storagedemo/StorageUtil.java.txt",
				"/Users/kanghongjin/Downloads/github-code-search/java.io.ObjectOutputStream__writeObject__1[ByteArrayOutputStream]_true/10755/com/slfuture/carrie/world/logic/Result.java.txt",
				"/Users/kanghongjin/Downloads/github-code-search/java.io.ObjectOutputStream__writeObject__1[ByteArrayOutputStream]_true/1518/esutdal/javanotes/cache/util/DefaultSerializer.java.txt");
		List<String> pathsToClassPath = Arrays.asList(
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/dependencies", null, null, null);
		javaFilesForApi.put("java.io.ObjectOutputStream__writeObject__1", pathsToJavaFiles);
		javaClassPathForApi.put("java.io.ObjectOutputStream__writeObject__1", pathsToClassPath);

		// for java.lang.Long__parseLong__1
		pathsToJavaFiles = Arrays.asList(
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/checkout/src/main/java/weiboclient4j/params/Cid.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/checkout/src/main/java/weiboclient4j/params/Id.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/checkout/src/main/java/weiboclient4j/params/Uid.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/checkout/src/main/java/weiboclient4j/params/Suid.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/checkout/src/main/java/weiboclient4j/params/TargetUid.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/checkout/src/main/java/weiboclient4j/params/SourceUid.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jriecken-gae-java-mini-profiler/80f3a59/checkout/src/main/java/com/google/appengine/tools/appstats/MiniProfilerAppstats.java",

				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/ivantrendafilov-confucius/2c30287/checkout/src/main/java/org/trendafilov/confucius/core/AbstractConfiguration.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/asterisk-java/41461b4/checkout/src/main/java/org/asteriskjava/manager/event/RtcpReceivedEvent.java",

				"/Users/kanghongjin/Downloads/github-code-search/java.lang.Long__parseLong__1_true/502/org/stempeluhr/util/Parser.java.txt",
				"/Users/kanghongjin/Downloads/github-code-search/java.lang.Long__parseLong__1_true/470/com/bin/brother/integerLong/Test1.java.txt",
				"/Users/kanghongjin/Downloads/github-code-search/java.lang.Long__parseLong__1_true/1841/org/motechproject/nms/reportfix/kilkari/helpers/Parser.java.txt"

		);
		pathsToClassPath = Arrays.asList(
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/hoverruan-weiboclient4j/6ca0c73/dependencies", null,
				null, null, null, null,
				null, null, null);

		javaFilesForApi.put("java.lang.Long__parseLong__1", pathsToJavaFiles);
		javaClassPathForApi.put("java.lang.Long__parseLong__1", pathsToClassPath);

		pathsToJavaFiles = Arrays.asList(
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/closure/319/checkout/src/com/google/javascript/jscomp/SimpleDefinitionFinder.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jackrabbit/1678/checkout/jackrabbit-jcr-server/src/main/java/org/apache/jackrabbit/webdav/jcr/JcrDavException.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jfreechart/881//checkout/source/org/jfree/chart/plot/CategoryPlot.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/lucene/754/checkout//src/java/org/apache/lucene/search/FieldCacheImpl.java",
				"/Users/kanghongjin/Downloads/github-code-search/java.util.Map__get__1_true/14640/javaapplication193/SequenceReconstruction.java.txt",
				"/Users/kanghongjin/Downloads/github-code-search/java.util.Map__get__1_true/8368/com/puzzle/SortMapByValue.java.txt",
				"/Users/kanghongjin/Downloads/github-code-search/java.util.Map__get__1_true/12190/myWorld/utils/Zpl_test.java.txt",
				"/Users/kanghongjin/Downloads/github-code-search/java.util.Map__get__1_true/17064/sds/chemicalexport/workers/CsvExport.java.txt",
				"/Users/kanghongjin/Downloads/github-code-search/java.util.Map__get__1_true/11906/com/yfy/crr/Analyser.java.txt",
				"/Users/kanghongjin/Downloads/github-code-search/java.util.Map__get__1_true/3362/edu/usc/ini/igc/ENIGMA/ml/MDD/J_SiteDictionary.java.txt"

		);
		pathsToClassPath = Arrays.asList("/Users/kanghongjin/repos/MUBench/mubench-checkouts/closure/319/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jackrabbit/1678/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jfreechart/881/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/lucene/754/dependencies", null, null, null, null,
				null, null);

		javaFilesForApi.put("java.util.Map__get__1", pathsToJavaFiles);
		javaClassPathForApi.put("java.util.Map__get__1", pathsToClassPath);

		pathsToJavaFiles = Arrays.asList(
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/checkout/src/main/java/org/joda/time/tz/ZoneInfoCompiler.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/itext/5091/checkout/itext/src/main/java/com/itextpdf/text/pdf/SimpleBookmark.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/itext/5091/checkout/itext/src/main/java/com/itextpdf/text/pdf/CJKFont.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/itext/5091/checkout/itext/src/main/java/com/itextpdf/text/html/WebColors.java",

				"/Users/kanghongjin/Downloads/github-code-search/java.util.StringTokenizer__nextToken__0_true/939/com/practice/string/Op5_StringTokenizerTest.java.txt",
				"/Users/kanghongjin/Downloads/github-code-search/java.util.StringTokenizer__nextToken__0_true/1068/Hunter/H_92.java.txt",
				"/Users/kanghongjin/Downloads/github-code-search/java.util.StringTokenizer__nextToken__0_true/1209/TokenPrueba2/TokenPrueba2.java.txt");
		pathsToClassPath = Arrays.asList(
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/cc35fb2/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/itext/5091/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/itext/5091/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/itext/5091/dependencies", null, null, null

		);

		javaFilesForApi.put("java.util.StringTokenizer__nextToken__0", pathsToJavaFiles);
		javaClassPathForApi.put("java.util.StringTokenizer__nextToken__0", pathsToClassPath);

		pathsToJavaFiles = Arrays.asList(
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/corona-old/0d0d18b/checkout/src/com/corona/crypto/DESCypher.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/corona-old/0d0d18b/checkout/src/com/corona/crypto/AESCypher.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/alibaba-druid/e10f28/checkout/src/main/java/com/alibaba/druid/filter/config/ConfigTools.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/chensun/cf23b99/checkout/CS5430/src/server/generateChecksumPostsAndReplies.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/itext/5091/checkout/itext/src/main/java/com/itextpdf/text/pdf/PdfPublicKeySecurityHandler.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jmrtd/67/checkout/src/sos/mrtd/PassportAuthService.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/minecraft-launcher/e62d1bb/checkout/src/main/java/net/minecraft/launcher/authentication/BaseAuthenticationService.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/saavn/e576758/checkout/src/saavn/cz/vity/freerapid/plugins/services/saavn/SaavnFileRunner.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/secure-tcp/checkout/src/main/java/org/network/stcp/server/SecureConnectionHandler.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/synthetic_jca/jsl/checkout/src/mubench/examples/jca/Encrypting.java",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/synthetic_jca/jsl/checkout/src/mubench/examples/jca/ReinitializingCipher.java");
		pathsToClassPath = Arrays.asList(
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/corona-old/0d0d18b/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/corona-old/0d0d18b/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/alibaba-druid/e10f28/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/chensun/cf23b99/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/itext/5091/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jmrtd/67/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/minecraft-launcher/e62d1bb/dependencies",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/saavn/e576758/dependencies", null, null, null
		);

		javaFilesForApi.put("javax.crypto.Cipher__init__2", pathsToJavaFiles);
		javaClassPathForApi.put("javax.crypto.Cipher__init__2", pathsToClassPath);

	}

}
