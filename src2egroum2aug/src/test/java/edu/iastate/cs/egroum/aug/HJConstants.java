package edu.iastate.cs.egroum.aug;

import java.util.HashMap;
import java.util.Map;

public class HJConstants {
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
}
