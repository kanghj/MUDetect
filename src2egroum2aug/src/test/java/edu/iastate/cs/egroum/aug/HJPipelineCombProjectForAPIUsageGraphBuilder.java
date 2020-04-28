package edu.iastate.cs.egroum.aug;

import static smu.hongjin.EAUGUtils.buildAUGsForClassFromSomewhereElse;
import static edu.iastate.cs.egroum.aug.ExtendedAUGTypeUsageExamplePredicate.EAUGUsageExamplesOf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import smu.hongjin.EnhancedAUG;
import smu.hongjin.ExperimentP;
import smu.hongjin.GraphBuildingUtils;
import smu.hongjin.LiteralsUtils;
import smu.hongjin.SubgraphMiningFormatter;

/**
 * Used for Experiment P.
 * @author kanghongjin
 *
 */
public class HJPipelineCombProjectForAPIUsageGraphBuilder {

	@Test
	public void run1() throws IOException {
		List<String> projects = Arrays.asList(
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/closure/319/",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/itext/5091/",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/lucene/1918/",
				"/Users/kanghongjin/repos/MUBench/mubench-checkouts/chensun/cf23b99/"
				);

		ExperimentP.run(projects);
	}
	
	
	
	@Test
	public void run3() throws IOException {
		List<String> projects = Arrays.asList(
		"/Users/kanghongjin/repos/MUBench/mubench-checkouts/bcel/24014e5/",
		"/Users/kanghongjin/repos/MUBench/mubench-checkouts/jigsaw/205/",
		"/Users/kanghongjin/repos/MUBench/mubench-checkouts/testng/677302c/");
		
		ExperimentP.run(projects);
	}
	
	
	
	
}
