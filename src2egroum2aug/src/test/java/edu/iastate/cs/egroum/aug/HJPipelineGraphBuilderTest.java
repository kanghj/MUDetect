package edu.iastate.cs.egroum.aug;

import java.io.IOException;

import org.junit.Test;

import smu.hongjin.HJConstants;
import smu.hongjin.HJGraphBuilder;

/**
 * A convenience script, disguised as a test, that exists just to run the graph builder.
 * Configure things in HJConstants.APIUnderMiner in HJConstants.java.
 * 
 *
 */
public class HJPipelineGraphBuilderTest {


	@Test
	public void build() throws IOException {

		HJGraphBuilder.buildGraphs();

	}


}
