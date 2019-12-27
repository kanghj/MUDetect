package edu.iastate.cs.egroum.aug;

import static edu.iastate.cs.egroum.aug.AUGBuilderTestUtils.buildAUGsForClassFromSomewhereElse;
import static edu.iastate.cs.egroum.aug.TypeUsageExamplePredicate.usageExamplesOf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.junit.Test;

import edu.iastate.cs.egroum.utils.JavaASTUtil;
import smu.hongjin.EnhancedAUG;
import smu.hongjin.SubgraphMiningFormatter;

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
	public void debug() {

		for (Entry<String, String> entry : HJPipelineGraphBuilder.directoriesToExamplesOfAPI.entrySet()) {
			String API = entry.getKey();
			String directory = entry.getValue();

			if (new File(directory + "labels.csv").exists()) {
				new File(directory + "labels.csv").delete();
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(directory + "labels.csv", true))) {
				writer.write("location,label(either 'M' for misuse or 'C' for correct usage)\n");

				try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
					paths.filter(Files::isRegularFile).forEach(path -> {
						if (path.endsWith("labels.csv") || path.endsWith("metadata.csv")
								|| path.endsWith("metadata_locations.csv")) {
							return;
						}
						if (path.toString().contains("/files/")) {
							System.out.println("Skipping : " + path + ", which contains /files");
							return;
						}
						if (!path.toString().endsWith(".java.txt")) {

							System.out.println("Skipping : " + path
									+ ". Unexpected file extension. We only look for java.txt files");
							return;
						}

						System.out.println("path is " + path);

//						System.out.println("\t " + path.toString().indexOf(directory));

						String id = path.toString().substring(directory.length()).split("/")[0];

						String code;
						try {
							code = new String(Files.readAllBytes(path));

							String filePath = path.toFile().toString();
							CompilationUnit cu = (CompilationUnit) JavaASTUtil.parseSource(code, filePath,
									filePath.substring(filePath.lastIndexOf("/")), null);

							for (int i = 0; i < cu.types().size(); i++)
								if (cu.types().get(i) instanceof TypeDeclaration) {
									TypeDeclaration typ = (TypeDeclaration) cu.types().get(i);

									for (MethodDeclaration md : typ.getMethods()) {
										if (usageExamplesOf(HJPipelineGraphBuilder.APIToClass.get(API)).matches(md)) {
//											String nameAsStr = typ.getName().toString() + "#" + md.getName();
											String sig = JavaASTUtil.buildSignature(md);
											writer.write(
													id + " - " + typ.getName().getIdentifier() + "." + sig + ",\n");
										}

									}

								}

						} catch (IOException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
					});
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}

			System.out.println(
					"renaming files from .java.txt to .java. It was .java.txt due to some legacy constraint on the github searching tool");
			try (Stream<Path> paths = Files.walk(Paths.get(directory + "files/"))) {
				paths.filter(Files::isRegularFile).forEach(path -> {
					try {
						Files.move(path, Paths.get(path.toString().replace(".java.txt", ".java")));
					} catch (IOException e) {
						System.out.println("Failed to move file");
						e.printStackTrace();
					}
				});
			} catch (IOException e1) {
				System.out.println("Failed to get files in 'files/' directory");
				e1.printStackTrace();
			}

		}
}}
