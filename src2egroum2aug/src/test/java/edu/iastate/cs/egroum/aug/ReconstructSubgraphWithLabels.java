package edu.iastate.cs.egroum.aug;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class ReconstructSubgraphWithLabels {
//	@Test
//	public void debug() throws IOException {
//		for (Entry<String, String> entry : HJPipelineGraphBuilder.directoriesToExamplesOfAPI.entrySet()) {
//			String clazz = HJPipelineGraphBuilder.APIToClass.get(entry.getKey());
//			String API = entry.getKey();
//
//			convert(API, "./output/" + API + "_vertmap.txt", "./output/" + API + "_edgemap.txt",
//					"./output/" + API + "_formatted.txt_result");
//		}
//
//	}

	/**
	 * For testing the ability to reconstrust the best subgraphs 
	 * @throws IOException
	 */
	@Test
	public void debug2() throws IOException {

		Set<Integer> bestSubgraphs = new HashSet<>();
		Files.lines(Paths.get("/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.io.ByteArrayOutputStream__toByteArray__0_formatted.txt_result_best_subgraphs.txt")).map(s -> s.trim())
				.filter(s -> !s.isEmpty()).forEach(line -> {
					String[] splitted = line.split(",");
					bestSubgraphs.add(Integer.parseInt(splitted[0]));
				});

		convert("ByteOutputStream",
				"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.io.ByteArrayOutputStream__toByteArray__0_vertmap.txt",
				"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.io.ByteArrayOutputStream__toByteArray__0_edgemap.txt",
				"/Users/kanghongjin/repos/MUDetect/src2egroum2aug/output/java.io.ByteArrayOutputStream__toByteArray__0_formatted.txt_result",
				bestSubgraphs);

	}

	private void convert(String API, String vertmapPath, String edgeMapPath, String graphPath)
			throws IOException, FileNotFoundException {
		convert(API, vertmapPath, edgeMapPath, graphPath, Collections.emptySet());
	}

	private void convert(String API, String vertmapPath, String edgeMapPath, String graphPath, Set<Integer> onlyShow)
			throws IOException, FileNotFoundException {
		Map<Integer, String> vertexMap = new HashMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(vertmapPath))) {
			String st;
			while ((st = reader.readLine()) != null) {
				int splitAt = st.lastIndexOf(",");

				vertexMap.put(Integer.parseInt(st.substring(splitAt + 1)), st.substring(0, splitAt ));
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

		try (BufferedReader reader = new BufferedReader(new FileReader(graphPath))) {
			String st;
			int currentId = -1;
			boolean alwaysShow = onlyShow.isEmpty();
			while ((st = reader.readLine()) != null) {
				
				if (st.startsWith("t")) {
					String[] splitted = st.split(" ");
					currentId = Integer.parseInt(splitted[2]);
					
					if (alwaysShow || onlyShow.contains(currentId)) {
						System.out.println(st);
					}

					
				} else if (st.startsWith("v")) {
					String[] splitted = st.split(" ");
					if (alwaysShow || onlyShow.contains(currentId)) {
						System.out.println("v " + splitted[1] + " " + vertexMap.get(Integer.parseInt(splitted[2])));
					}

				} else if (st.startsWith("e")) {
					String[] splitted = st.split(" ");

					if (alwaysShow || onlyShow.contains(currentId)) {
						System.out.println(
							"e " + splitted[1] + " " + splitted[2] + " " + edgeMap.get(Integer.parseInt(splitted[3])));
					}
				} else {
					throw new RuntimeException("what just happened? no other line type was expected " + st);
				}
			}
		}
	}

}
