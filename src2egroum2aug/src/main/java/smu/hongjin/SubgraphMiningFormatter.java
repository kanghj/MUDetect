package smu.hongjin;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.tu_darmstadt.stg.mudetect.aug.model.APIUsageExample;
import de.tu_darmstadt.stg.mudetect.aug.model.APIUsageGraph;
import de.tu_darmstadt.stg.mudetect.aug.model.Edge;
import de.tu_darmstadt.stg.mudetect.aug.model.Node;
import de.tu_darmstadt.stg.mudetect.aug.model.data.ConstantNode;
import de.tu_darmstadt.stg.mudetect.aug.visitors.BaseAUGLabelProvider;

/**
 * THe GSpan software expects the graph in a certain format.
 * We'll give it the graph in that format using the code here.
 * @author kanghongjin
 *
 */
public class SubgraphMiningFormatter {

//	public static void convert(Collection<APIUsageExample> augs, int i, Map<String, Integer> vertexLabels, Map<String, Integer> edgeLabels, BufferedWriter writer) throws IOException {
//		// along the way,
//		// we collect the labels of vertices and edges
//
//		
//		for (APIUsageExample aug : augs) {
//			writer.write("t " + "# " + i + "\n");
//
//			Map<Node, Integer> vertexNumbers = new HashMap<>();
//			
//			int nodeNumber = 0;
//			for (Node vertex : aug.vertexSet()) {
//				 String nodeLabel = new BaseAUGLabelProvider().getLabel(vertex);
//				 
//				 if (!vertexLabels.containsKey(nodeLabel)) {
//					 vertexLabels.put(nodeLabel, vertexLabels.size());	 
//				 }
//				 int nodeLabelIndex = vertexLabels.get(nodeLabel);
//				 
//				 writer.write("v " + nodeNumber + " " + nodeLabelIndex+ "\n");
//				 
//				 
//				 vertexNumbers.put(vertex, nodeNumber);
//				 
//				 nodeNumber+=1;
//			}
//			
//			//
//			
//			for (Edge edge : aug.edgeSet()) {
//				String edgeLabel = new BaseAUGLabelProvider().getLabel(edge);
//				
//				if (!edgeLabels.containsKey(edgeLabel)) {
//					edgeLabels.put(edgeLabel, edgeLabels.size());	 
//				}
//				int edgeLabelIndex = edgeLabels.get(edgeLabel);
//				 
//				
//				int sourceNumber = vertexNumbers.get(edge.getSource());
//				int targetNumber = vertexNumbers.get(edge.getTarget());
//				
//				writer.write("e " + sourceNumber + " " + targetNumber + " " + edgeLabelIndex+ "\n");
//			}
//			i++;
//		}
//		
//	}
//	
	/**
	 * This mutates vertexLabels and edgeLabels
	 * @param eaugs
	 * @param type
	 * @param i
	 * @param vertexLabels
	 * @param edgeLabels
	 * @param label
	 * @param quantity
	 * @param writer
	 * @throws IOException
	 */
	public static void convert(Collection<EnhancedAUG> eaugs, Class<?> type ,int i, 
			Map<String, Integer> vertexLabels, Map<String, Integer> edgeLabels, 
			String fileId,
			Map<String, String> labels, int quantity,
			BufferedWriter writer) throws IOException {
		// along the way,
		// we collect the labels of vertices and edges
		
		for (EnhancedAUG eaug : eaugs) {
			APIUsageGraph aug = eaug.aug;
			
			if (aug.vertexSet().size() == 0) {
				continue;
			}
			
			// 
			String labelId = fileId + " - " + eaug.aug.name;
			String label = labels.get(labelId);
			if (label == null) throw new RuntimeException("missing label!");
			
			
			writer.write("t " + "# " + i + " " + label + " " + quantity + "\n");

			Map<Node, Integer> vertexNumbers = new HashMap<>();
			
			int nodeNumber = 0;
			writeNodesInAug(vertexLabels, writer, aug, vertexNumbers, nodeNumber);
			
			for (APIUsageGraph relAug : eaug.related) {
				writeNodesInAug(vertexLabels, writer, relAug, vertexNumbers, nodeNumber);				
			}
			// write nodes for the interface
			for (String interfaceObj : eaug.interfaces) {
				String interfaceString = "interface_" + interfaceObj;
				if (!vertexLabels.containsKey(interfaceString)) {
					 vertexLabels.put(interfaceString, vertexLabels.size());	 
				 }
				 int nodeLabelIndex = vertexLabels.get(interfaceString);
				 
				 writer.write("v " + nodeNumber + " " + nodeLabelIndex+ "\n");
				 
				 ConstantNode interfaceNode = new ConstantNode("DummyInterfaceType", "Interface", interfaceString); 
				 vertexNumbers.put(interfaceNode, nodeNumber);
				 
				 nodeNumber+=1;
			}
			
			//
			writeEdgesInAug(edgeLabels, writer, aug, vertexNumbers);
			for (APIUsageGraph relAug : eaug.related) {
				writeEdgesInAug(edgeLabels, writer, relAug, vertexNumbers);				
			}
			
			writer.write("-\n");
			
			i++;
		}
		
	}

	private static void writeEdgesInAug(Map<String, Integer> edgeLabels, BufferedWriter writer, APIUsageGraph aug,
			Map<Node, Integer> vertexNumbers) throws IOException {
		for (Edge edge : aug.edgeSet()) {
			String edgeLabel = new BaseAUGLabelProvider().getLabel(edge);
			
			if (!edgeLabels.containsKey(edgeLabel)) {
				edgeLabels.put(edgeLabel, edgeLabels.size());	 
			}
			int edgeLabelIndex = edgeLabels.get(edgeLabel);
			 
			
			int sourceNumber = vertexNumbers.get(edge.getSource());
			int targetNumber = vertexNumbers.get(edge.getTarget());
			
			writer.write("e " + sourceNumber + " " + targetNumber + " " + edgeLabelIndex+ "\n");
		}
	}

	private static void writeNodesInAug(Map<String, Integer> vertexLabels, BufferedWriter writer, APIUsageGraph aug,
			Map<Node, Integer> vertexNumbers, int nodeNumber) throws IOException {
		for (Node vertex : aug.vertexSet()) {
			 String nodeLabel = new BaseAUGLabelProvider().getLabel(vertex);
			 
			 if (!vertexLabels.containsKey(nodeLabel)) {
				 vertexLabels.put(nodeLabel, vertexLabels.size());	 
			 }
			 int nodeLabelIndex = vertexLabels.get(nodeLabel);
			 
			 writer.write("v " + nodeNumber + " " + nodeLabelIndex+ "\n");
			 
			 
			 vertexNumbers.put(vertex, nodeNumber);
			 
			 nodeNumber+=1;
		}
	}
	
}
