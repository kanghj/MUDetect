package smu.hongjin;

import java.util.Optional;

import org.jgrapht.graph.DirectedMultigraph;

import de.tu_darmstadt.stg.mudetect.aug.model.APIUsageExample;
import de.tu_darmstadt.stg.mudetect.aug.model.Edge;
import de.tu_darmstadt.stg.mudetect.aug.model.Node;
import de.tu_darmstadt.stg.mudetect.aug.model.controlflow.FinallyEdge;
import de.tu_darmstadt.stg.mudetect.aug.model.data.ConstantNode;
import de.tu_darmstadt.stg.mudetect.aug.model.dataflow.ImplementsEdge;

/**
 * For drawing as a dot file
 * 
 * @author kanghongjin
 *
 */
public class EnhancedAUGAsGraph extends DirectedMultigraph<Node, Edge> {

	public EnhancedAUGAsGraph() {
		super(Edge.class);
	}

	public void build(EnhancedAUG eaug) {

		for (Node node : eaug.aug.vertexSet()) {
			this.addVertex(node);
		}

		for (Edge edge : eaug.aug.edgeSet()) {
			this.addEdge(edge.getSource(), edge.getTarget(), edge);
		}
		
		if (eaug.interfaces.isEmpty()) {
			return; // done
		}

		// find the recv node of 
		if (eaug.aug instanceof APIUsageExample) {
			APIUsageExample aue = (APIUsageExample) eaug.aug;
			
			int bestLineNumberToLinkTo = 99999999;
			Node earliestNode = null;
			// get the earliest line.
			for (Node node : eaug.aug.vertexSet()) {
				Optional<Integer> lineNum = aue.getSourceLineNumber(node);
				if (lineNum.isPresent()) {
					bestLineNumberToLinkTo = Math.min(lineNum.get(), bestLineNumberToLinkTo);
					if (bestLineNumberToLinkTo == lineNum.get()) {
						earliestNode = node;
					}
				}
			}
		
		
			for (String interfaze : eaug.interfaces) {
				ConstantNode interfaceNode = new ConstantNode("Class", interfaze, interfaze);
				this.addVertex(interfaceNode);
				this.addEdge(interfaceNode, earliestNode, new ImplementsEdge(interfaceNode, earliestNode));
				
			}
		}
	}

}
