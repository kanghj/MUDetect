package edu.iastate.cs.egroum.aug;

import java.io.IOException;

import org.junit.Test;

import smu.hongjin.HJConstants;
import smu.hongjin.HJGraphBuilder;

/**
 * A convenience script, disguised as a test, that exists just to run the graph builder.
 * Configure things in HJConstants.APIUnderMiner in HJConstants.java.
 * 
 * Ran after HJFilesPreprocessor
 * Run before Running GSpan on the server
 */
public class HJPipelineGraphBuilderTest {


	@Test
	public void build() throws IOException {

		System.out.println(HJConstants.APIUnderMiner);
		HJGraphBuilder.buildGraphs();

	}


}
