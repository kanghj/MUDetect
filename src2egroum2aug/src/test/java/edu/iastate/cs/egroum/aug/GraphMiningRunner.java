package edu.iastate.cs.egroum.aug;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import smu.hongjin.HJConstants;
import smu.hongjin.HJGraphBuilder;
import smu.hongjin.HJPreprocessor;

public class GraphMiningRunner {

	public static void main(String... args) throws FileNotFoundException, IOException {
		if  (args.length < 2) {
			throw new RuntimeException("Please supply the running type and API name to the runner");
		}
		
		HJConstants.APIUnderMiner = Arrays.asList(args[1]); 
		if (args[0].equals("preprocess")) {
			HJPreprocessor.preprocess();
		} else if (args[0].equals("mine")) {
			HJGraphBuilder.buildGraphs();
		} else if (args[0].equals("buildTestGraphs")) {
			HJPipelineTestDataGraphBuilder.buildGraphs(args[1]);
		} else if (args[0].equals("postprocess")) {
			HJFilesPostprocessor.postprocess();
		}
		
	}
}
