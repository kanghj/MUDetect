package edu.iastate.cs.egroum.aug;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ReconstructSubgraphWithLabels {
	@Test
	public void debug() throws IOException {
		
		Map<Integer, String> vertexMap = new HashMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("./output/vertmap.txt"))) {
			String st;
			while ((st = reader.readLine()) != null) {
				String[] splitted = st.split(",");
				vertexMap.put(Integer.parseInt(splitted[1]), splitted[0]);
			}
		}
		
		Map<Integer, String> edgeMap = new HashMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("./output/edgemap.txt"))) {
			String st;
			while ((st = reader.readLine()) != null) {
				String[] splitted = st.split(",");
				edgeMap.put(Integer.parseInt(splitted[1]), splitted[0]);
			}
		}
		

		try (BufferedReader reader = new BufferedReader(new FileReader("./output/formatted.txt_result"))) {
			String st;
			while ((st = reader.readLine()) != null) {
				if (st.startsWith("t")) {
					System.out.println(st);
				} else if (st.startsWith("v")) {
					String[] splitted = st.split(" ");
					System.out.println("v " + splitted[1] + " " + vertexMap.get(Integer.parseInt(splitted[2])));
					
				} else if (st.startsWith("e")) {
					String[] splitted = st.split(" ");
					
					System.out.println("e " + splitted[1] + " " + splitted[2] + " " + edgeMap.get(Integer.parseInt(splitted[3])));
				} else {
					throw new RuntimeException("what just happened? no other line type was expected");
				}
			}
		}

	}

}
