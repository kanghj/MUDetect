package edu.iastate.cs.egroum.aug;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * After running GSPAN, can use this class to figure out what exactly the subgraphs contan
 * @author kanghongjin
 *
 */
public class ReconstructSubgraphWithLabels {

	private Path guessPathToBestSubgraphFile(String API) {
		String baseDir = "/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/" + API +"/";
		String fileName = API + "_formatted_result_best_subgraphs.txt";
		
		return Paths.get(baseDir + fileName);
	}
	
	private String pathToVertMapFile(String API) {
		String baseDir = "/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/" + API +"/";
		String fileName = API + "_vertmap.txt";
		
		return baseDir + fileName;
	}
	
	private String pathToEdgeMapFile(String API) {
		String baseDir = "/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/" + API +"/";
		String fileName = API + "_edgemap.txt";
		
		return baseDir + fileName;
	}
	
	private String pathToFrequentSubgraphFile(String API) {
		String baseDir = "/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/" + API +"/";
		String fileName = API + "_formatted_result";
		
		return baseDir + fileName;
	}
	
	/**
	 * For testing the ability to reconstruct the best subgraphs 
	 * @throws IOException
	 */
	@Test
	public void debug2() throws IOException {

		Map<Integer, Float> bestSubgraphs = new HashMap<>();
//		Path path = Paths.get("/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.util.Map__get__1_formatted_result_best_subgraphs.txt");
//		Path path = Paths.get("/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.lang.Long__parseLong__1_formatted_result_best_subgraphs.txt");
//		Path path = Paths.get("/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.util.StringTokenizer__nextToken__0/java.util.StringTokenizer__nextToken__0_formatted_result_best_subgraphs.txt");
//		Path path = Paths.get("/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.io.ObjectOutputStream__writeObject__1_formatted_result_best_subgraphs.txt");
//		Files.lines(Paths.get("/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.io.ByteArrayOutputStream__toByteArray__0_formatted.txt_result_best_subgraphs.txt")).map(s -> s.trim())
		
		String APIunderTest = "javax.crypto.Cipher__init__2";
		Path path = guessPathToBestSubgraphFile(APIunderTest);
		
		Files.lines(path).map(s -> s.trim())
//		Files.lines(Paths.get("/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.sql.PreparedStatement__executeUpdate__0_formatted_result_best_subgraphs.txt")).map(s -> s.trim())
				.filter(s -> !s.isEmpty()).forEach(line -> {
					String[] splitted = line.split(",");
					
					bestSubgraphs.put(Integer.parseInt(splitted[0]), Float.parseFloat(splitted[1]));
				});
		List<Integer> sorted = bestSubgraphs.entrySet().stream()
		   .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
		   .map(Entry::getKey)
		   .collect(Collectors.toList());

		convert(pathToVertMapFile(APIunderTest),
				pathToEdgeMapFile(APIunderTest),
				pathToFrequentSubgraphFile(APIunderTest),
				sorted);
		
//		System.out.println(sorted);
//		convert("ByteOutputStream",
//				"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.io.ByteArrayOutputStream__toByteArray__0_vertmap.txt",
//				"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.io.ByteArrayOutputStream__toByteArray__0_edgemap.txt",
//				"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.io.ByteArrayOutputStream__toByteArray__0_formatted.txt_result",
//				bestSubgraphs);
//		convert("ObjectOutputStream",
//						"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.io.ObjectOutputStream__writeObject__1_vertmap.txt",
//						"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.io.ObjectOutputStream__writeObject__1_edgemap.txt",
//						"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.io.ObjectOutputStream__writeObject__1_formatted_result",
//						sorted);
//		convert(
//				"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.util.StringTokenizer__nextToken__0/java.util.StringTokenizer__nextToken__0_vertmap.txt",
//				"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.util.StringTokenizer__nextToken__0/java.util.StringTokenizer__nextToken__0_edgemap.txt",
//				"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.util.StringTokenizer__nextToken__0/java.util.StringTokenizer__nextToken__0_formatted_result",
//				sorted);
//		
//		convert(pathToVertMapFile(APIunderTest),
//				pathToEdgeMapFile(APIunderTest),
//				pathToFrequentSubgraphFile(APIunderTest),
//				sorted);
//		convert("Long",
//				"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.lang.Long__parseLong__1_vertmap.txt",
//				"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.lang.Long__parseLong__1_edgemap.txt",
//				"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.lang.Long__parseLong__1_formatted_result",
//				sorted);
//		convert("Map",
//		"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.util.Map__get__1_vertmap.txt",
//		"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.util.Map__get__1_edgemap.txt",
//		"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.util.Map__get__1_formatted_result",
//		sorted);
//		convert("PreparedStatement",
//				"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.sql.PreparedStatement__executeUpdate__0_vertmap.txt",
//				"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.sql.PreparedStatement__executeUpdate__0_edgemap.txt",
//				"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.sql.PreparedStatement__executeUpdate__0_formatted_result",
//				bestSubgraphs);

	}



	private void convert(String vertmapPath, String edgeMapPath, String graphPath, List<Integer> onlyShow)
			throws IOException, FileNotFoundException {
		Map<Integer, String> vertexMap = new HashMap<>();
		int line = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(vertmapPath))) {
			String st;
			while ((st = reader.readLine()) != null) {
				int splitAt = st.lastIndexOf(",");

				try {
					vertexMap.put(Integer.parseInt(st.substring(splitAt + 1)), st.substring(0, splitAt ));
				} catch (NumberFormatException nfe) {
					System.out.println("weird input format. string=" + st + " at line " + line);
					throw new RuntimeException(nfe);
					
				}
				line++;
			}
		}

		Map<Integer, String> edgeMap = new HashMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(edgeMapPath))) {
			String st;
			while ((st = reader.readLine()) != null) {
				int splitAt = st.lastIndexOf(",");
				edgeMap.put(Integer.parseInt(st.substring(splitAt + 1)), st.substring(0, splitAt ));
			}
		}
		
		Map<Integer, StringBuilder> content = new HashMap<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(graphPath))) {
			String st;
			int currentId = -1;
			boolean alwaysShow = onlyShow.isEmpty();
			while ((st = reader.readLine()) != null) {
				
				if (st.startsWith("t")) {
					String[] splitted = st.split(" ");
					currentId = Integer.parseInt(splitted[2]);
					
					if (alwaysShow || onlyShow.contains(currentId)) {
//						System.out.println(st);
						content.putIfAbsent(currentId, new StringBuilder());
						
						content.get(currentId).append(st + "\n");
					}

					
				} else if (st.startsWith("v")) {
					String[] splitted = st.split(" ");
					if (alwaysShow || onlyShow.contains(currentId)) {
//						System.out.println("v " + splitted[1] + " " + vertexMap.get(Integer.parseInt(splitted[2])));
						content.get(currentId).append("v " + splitted[1] + " " + vertexMap.get(Integer.parseInt(splitted[2])) + "\n");
					}

				} else if (st.startsWith("e")) {
					String[] splitted = st.split(" ");

					if (alwaysShow || onlyShow.contains(currentId)) {
//						System.out.println(
//							"e " + splitted[1] + " " + splitted[2] + " " + edgeMap.get(Integer.parseInt(splitted[3])));
						content.get(currentId).append(
								"e " + splitted[1] + " " + splitted[2] + " " + edgeMap.get(Integer.parseInt(splitted[3]))+ "\n");
					}
				} else {
					throw new RuntimeException("what just happened? no other line type was expected " + st );
				}
			}
			
			for (Integer item : onlyShow) {
				System.out.println(content.get(item).toString());
			}
		}
	}

}
