package edu.iastate.cs.egroum.aug;

import java.io.IOException;

import org.junit.Test;

/**
 * A convenience script, disguised as a test, that exists just to run the graph builder.
 * 
 * 
 * @author kanghongjin
 *
 */
public class HJPipelineGraphBuilderTest {


	@Test
	public void build() throws IOException {

		HJGraphBuilder.buildGraphs();

	}


}
