package smu.hongjin;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.tu_darmstadt.stg.mudetect.aug.model.APIUsageExample;
import de.tu_darmstadt.stg.mudetect.aug.model.Edge;
import de.tu_darmstadt.stg.mudetect.aug.model.Node;
import de.tu_darmstadt.stg.mudetect.aug.visitors.BaseAUGLabelProvider;

/**
 * THe GSpan software expects the graph in a certain format.
 * We'll give it the graph in that format using the code here.
 * @author kanghongjin
 *
 */
public class SubgraphMiningFormatter {

	public static void convert(Collection<APIUsageExample> augs, int i, Map<String, Integer> vertexLabels, Map<String, Integer> edgeLabels, BufferedWriter writer) throws IOException {
		// along the way,
		// we collect the labels of vertices and edges

		
		for (APIUsageExample aug : augs) {
			writer.write("t " + "# " + i + "\n");

			Map<Node, Integer> vertexNumbers = new HashMap<>();
			
			int nodeNumber = 0;
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
			i++;
		}
		
	}
	
}
