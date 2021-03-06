package smu.hongjin;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HJConstants {
	public static Map<String, Set<String>> directoriesToExamplesOfAPI = new HashMap<>();
	

	public static final String examplesRoot = "/Users/kanghongjin/Downloads/github-code-search/";
	
	public static List<String> APIUnderMiner = Arrays.asList(//"java.awt.Shape__getPathIterator__1"
//			"org.jfree.chart.plot.XYPlot__getRendererForDataset__1"
//			"javax.crypto.spec.SecretKeySpec__<init>__2"
//			"javax.crypto.Cipher__init__2"
//			"java.util.Iterator__next__0"
//			"java.lang.StringBuilder__append__1"
			"org.apache.commons.httpclient.HttpConnection__open__0",
//			"java.util.Enumeration__nextElement__0",
//			"java.util.List__get__1"
//			"java.util.Map__get__1"
//			"java.sql.ResultSet__next__0",
//			"java.io.ObjectOutputStream__writeObject__1",
//			"java.lang.Long__parseLong__1"
//			"java.sql.PreparedStatement__execute*__0"
			"java.io.RandomAccessFile__close__0"
			);
	// java.awt.Shape__getPathIterator__1
	
	// org.jfree.chart.plot.XYPlot__getRendererForDataset__1
	// avax.crypto.spec.SecretKeySpec__<init>__2
	// java.lang.String__<init>__2

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
		

		directoriesToExamplesOfAPI.put("java.util.StringTokenizer__nextToken__0",
				Collections.singleton(
				examplesRoot + "java.util.StringTokenizer__nextToken__0/"));
//		
		directoriesToExamplesOfAPI.put("javax.crypto.Cipher__init__2",
				Collections.singleton(
				examplesRoot + "javax.crypto.Cipher__init__2/"));
//		APIToClass.put("javax.crypto.Cipher__init__2", "javax.crypto.Cipher");
//		
////		
		directoriesToExamplesOfAPI.put("java.lang.Long__parseLong__1",
				Collections.singleton(
				examplesRoot +	"java.lang.Long__parseLong__1_true/"));
//		
		directoriesToExamplesOfAPI.put("java.io.ObjectOutputStream__writeObject__1",
				Collections.singleton(
				examplesRoot + "java.io.ObjectOutputStream__writeObject__1[ByteArrayOutputStream]_true/"));
		directoriesToExamplesOfAPI.put("java.util.StringTokenizer__nextToken__0",
				Collections.singleton(
				examplesRoot + "java.util.StringTokenizer__nextToken__0_true/"));
		
		directoriesToExamplesOfAPI.put("javax.crypto.Cipher__init__2",
				Collections.singleton(
				examplesRoot + "javax.crypto.Cipher__init__2_true/"));
		
		directoriesToExamplesOfAPI.put("java.io.DataOutputStream__<init>__1",
				Collections.singleton(
				examplesRoot + "java.io.DataOutputStream__<init>__1[ByteArrayOutputStream]_true/"));
		
		directoriesToExamplesOfAPI.put("java.sql.PreparedStatement__execute*__0",
				new HashSet<>(
						Arrays.asList(
								examplesRoot + "java.sql.PreparedStatement__execute__0_true/",
								examplesRoot + "java.sql.PreparedStatement__executeUpdate__0_true/",
								examplesRoot + "java.sql.PreparedStatement__executeQuery__0_true/"))
				);
	
		directoriesToExamplesOfAPI.put("java.util.Iterator__next__0",
				Collections.singleton(examplesRoot + "java.util.Iterator__next__0_true/")
				);
		
//		
		
		directoriesToExamplesOfAPI.put("java.util.Map__get__1",
				Collections.singleton(
				examplesRoot + "java.util.Map__get__1_true/"));
		

		
		directoriesToExamplesOfAPI.put("java.util.List__get__1",
				Collections.singleton(
				examplesRoot + "java.util.List__get__1_true/"));
		
		
		directoriesToExamplesOfAPI.put("org.apache.lucene.index.SegmentInfos__info__1",
				Collections.singleton(
				examplesRoot +	"org.apache.lucene.index.SegmentInfos__info__1_true/"));
		
		directoriesToExamplesOfAPI.put("org.jfree.data.statistics.StatisticalCategoryDataset__getMeanValue__2",
				Collections.singleton(
				examplesRoot +	"org.jfree.data.statistics.StatisticalCategoryDataset__getMeanValue__2_true/"));
		
		directoriesToExamplesOfAPI.put("java.util.Scanner__next__0",
				Collections.singleton(
				examplesRoot +	"java.util.Scanner__next__0_true/"));

		directoriesToExamplesOfAPI.put("com.itextpdf.text.pdf.PdfArray__getPdfObject__1",
		Collections.singleton(
		examplesRoot +	"com.itextpdf.text.pdf.PdfArray__getPdfObject__1_true/"));

		directoriesToExamplesOfAPI.put("java.sql.ResultSet__next__0",
		Collections.singleton(
		examplesRoot +	"java.sql.ResultSet__next__0_true/"));

		
		directoriesToExamplesOfAPI.put("java.lang.Byte__parseByte__1",
		Collections.singleton(
		examplesRoot +	"java.lang.Byte__parseByte__1_true/"));
		
		
		directoriesToExamplesOfAPI.put("java.lang.Short__parseShort__1",
				Collections.singleton(
				examplesRoot +	"java.lang.Short__parseShort__1_true/"));
				
		
		
		directoriesToExamplesOfAPI.put("java.util.Enumeration__nextElement__0",
				Collections.singleton(
				examplesRoot +	"java.util.Enumeration__nextElement__0_true/"));
		
		
		
		directoriesToExamplesOfAPI.put("org.jfree.chart.plot.XYPlot__getRendererForDataset__1",
				Collections.singleton(
				examplesRoot +	"org.jfree.chart.plot.XYPlot__getRendererForDataset__1_true/"));
		
		directoriesToExamplesOfAPI.put("org.jfree.chart.plot.PlotRenderingInfo__getOwner__0",
				Collections.singleton(
				examplesRoot +	"org.jfree.chart.plot.PlotRenderingInfo__getOwner__0_true/"));
		
		
		
		directoriesToExamplesOfAPI.put("com.itextpdf.text.pdf.PdfDictionary__getAsString__1",
				Collections.singleton(
				examplesRoot +	"com.itextpdf.text.pdf.PdfDictionary__getAsString__1_true/"));
		
		
		directoriesToExamplesOfAPI.put("org.jfree.chart.plot.CategoryPlot__getDataset__1",
				Collections.singleton(
				examplesRoot +	"org.jfree.chart.plot.CategoryPlot__getDataset__1_true/"));
		
		directoriesToExamplesOfAPI.put("org.apache.commons.httpclient.auth.AuthState__isPreemptive__0",
				Collections.singleton(
				examplesRoot +	"org.apache.commons.httpclient.auth.AuthState__isPreemptive__0_true/"));
		
		//
		directoriesToExamplesOfAPI.put("java.nio.ByteBuffer__put__1",
				Collections.singleton(
				examplesRoot +	"java.nio.ByteBuffer__put__1_true/"));
		
		directoriesToExamplesOfAPI.put("java.nio.channels.FileChannel__write__1",
				Collections.singleton(
				examplesRoot +	"java.nio.channels.FileChannel__write__1_true/"));
		
		directoriesToExamplesOfAPI.put("org.kohsuke.args4j.spi.Parameters__getParameter__1",
				Collections.singleton(
				examplesRoot +	"org.kohsuke.args4j.spi.Parameters__getParameter__1_true/"));
		
		directoriesToExamplesOfAPI.put("java.net.URLDecoder__decode__2",
				Collections.singleton(
				examplesRoot +	"java.net.URLDecoder__decode__2_true/"));
		
		directoriesToExamplesOfAPI.put("java.util.SortedMap__firstKey__0",
				Collections.singleton(
				examplesRoot +	"java.util.SortedMap__firstKey__0_true/"));
		
		directoriesToExamplesOfAPI.put("java.io.PrintWriter__write__1",
				Collections.singleton(
				examplesRoot +	"java.io.PrintWriter__write__1_true/"));
		
		directoriesToExamplesOfAPI.put("javax.swing.JFrame__setVisible__1",
				Collections.singleton(
				examplesRoot +	"java.awt.Window__setVisible__1_true/"));
		
		
		
		directoriesToExamplesOfAPI.put("java.util.Optional__get__0",
				Collections.singleton(
				examplesRoot +	"java.util.Optional__get__0_true/"));
		
		directoriesToExamplesOfAPI.put("com.google.common.collect.Multimap__get__1",
				Collections.singleton(
				examplesRoot +	"com.google.common.collect.Multimap__get__1_true/"));
		
		
		
		directoriesToExamplesOfAPI.put("org.apache.commons.lang.text.StrBuilder__getNullText__0",
				Collections.singleton(
				examplesRoot +	"org.apache.commons.lang.text.StrBuilder__getNullText__0_true/"));

		directoriesToExamplesOfAPI.put("org.apache.commons.math3.geometry.euclidean.threed.Line__intersection__1",
				Collections.singleton(
				examplesRoot +	"org.apache.commons.math3.geometry.euclidean.threed.Line__intersection__1_true/"));
		
		directoriesToExamplesOfAPI.put("org.apache.commons.math3.geometry.euclidean.twod.Line__intersection__1",
				Collections.singleton(
				examplesRoot +	"org.apache.commons.math3.geometry.euclidean.twod.Line__intersection__1_true/"));
		
		directoriesToExamplesOfAPI.put("javax.crypto.spec.SecretKeySpec__<init>__2",
				Collections.singleton(
				examplesRoot +	"javax.crypto.spec.SecretKeySpec__<init>__2_true/"));
		
		directoriesToExamplesOfAPI.put("java.lang.String__charAt__1",
				Collections.singleton(
				examplesRoot +	"java.lang.String__charAt__1_true/"));
		
//		directoriesToExamplesOfAPI.put("java.lang.String__<init>__2",
//				Collections.singleton(
//				examplesRoot +	"java.lang.String__<init>__2[doFinal]_true/"));
		
		directoriesToExamplesOfAPI.put("java.awt.Shape__getPathIterator__1",
				Collections.singleton(
				examplesRoot +	"java.awt.Shape__getPathIterator__1_true/"));
		directoriesToExamplesOfAPI.put("org.jfree.chart.plot.XYPlot__getRendererForDataset__1",
				Collections.singleton(
				examplesRoot +	"org.jfree.chart.plot.XYPlot__getRendererForDataset__1_true/"));
		
		
		
		directoriesToExamplesOfAPI.put("java.lang.StringBuilder__append__1",
				Collections.singleton(
				examplesRoot +	"java.lang.StringBuilder__append__1_true/"));
		
		directoriesToExamplesOfAPI.put("org.apache.commons.httpclient.HttpConnection__open__0",
				Collections.singleton(
				examplesRoot +	"org.apache.commons.httpclient.HttpConnection__open__0_true/"));
		directoriesToExamplesOfAPI.put("java.io.RandomAccessFile__close__0",
				Collections.singleton(
				examplesRoot +	"java.io.RandomAccessFile__close__0_true/"));
		
		
//		assert directoriesToExamplesOfAPI.size() == GraphBuildingUtils.APIToClass.size();
	}
}
