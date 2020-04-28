package edu.iastate.cs.egroum.aug;

import static edu.iastate.cs.egroum.aug.ExtendedAUGTypeUsageExamplePredicate.EAUGUsageExamplesOf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.core.dom.NaiveASTFlattener;
import org.junit.Test;

import com.google.common.collect.Sets;

import edu.iastate.cs.egroum.utils.JavaASTUtil;
import smu.hongjin.GraphBuildingUtils;
import smu.hongjin.HJConstants;
import smu.hongjin.HJPreprocessor;

/**
 * HJ: labels should be on individual methods in file
 * 
 * Writes labels.csv and converts the .java.txt files to .java
 * 
 * @author kanghongjin
 *
 */
public class HJFilesPreprocessor {

	@Test
	public void debug() throws FileNotFoundException, IOException {
		HJPreprocessor.preprocess();
	}

	
}
