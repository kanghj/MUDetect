package edu.iastate.cs.egroum.aug;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Used for Experiment P.
 * @author kanghongjin
 *
 */
public class HJPipelineCombProject2 {
	@Test
	public void run2() throws IOException {
		List<String> projects = Arrays.asList(
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jmrtd/51/",
		"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jodatime/1231/",
		"/Users/kanghongjin/repos/MUBench/mubench-checkouts/asterisk-java/304421c/"
		
		);
		
		HJPipelineCombProjectForAPIUsageGraphBuilder.run(projects);
	}
}
