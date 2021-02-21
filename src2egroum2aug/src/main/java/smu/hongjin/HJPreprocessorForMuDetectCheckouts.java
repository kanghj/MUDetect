package smu.hongjin;

import static edu.iastate.cs.egroum.aug.ExtendedAUGTypeUsageExamplePredicate.EAUGUsageExamplesOf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.core.dom.NaiveASTFlattener;

import com.google.common.collect.Sets;

import edu.iastate.cs.egroum.aug.EGroumBuilder;
import edu.iastate.cs.egroum.aug.ExtendedAUGTypeUsageExamplePredicate;
import edu.iastate.cs.egroum.utils.JavaASTUtil;

public class HJPreprocessorForMuDetectCheckouts {

	static double percentageToLabel = 0.5;

	static Map<String, Set<String>> pathsAndMethodToTokens = new HashMap<>();
	static Map<String, Integer> pathsAndMethodToCounts = new HashMap<>();

	static Map<String, Set<String>> apiRepos;
	
	static Map<String, String> apiToSpecificMethod;
	static String checkoutLocation = "/checkouts-xp/checkouts/"; // TODO: make this not hardcoded lol
	static String output = "/checkouts-xp/alp_extract/";
//	static String checkoutLocation = "./";

	static {
		try {
			apiRepos = init();
			
			// because we need to label the method, we might as well just focus on the methods that can be misused
			// ALP will trivially filter out the cases (based on the code missing the dangerous method) 
			// that are not misused afterwards anyway, even if they were included for labelling.
			apiToSpecificMethod = getLongMapping(apiRepos);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static Map<String, Set<String>> init() throws IOException {

		Map<String, Set<String>> apiToRepo = new HashMap<>();

		List<String> lines = Files.lines(new File("example_projects_by_API.yml").toPath()).collect(Collectors.toList());
		String api = null;
		for (String line : lines) {
			if (!(line.contains("//github") && line.contains("http"))) {
				api = line.split(":")[0];
				if (apiToRepo.containsKey(api)) {
					throw new RuntimeException("assertion; violates my assumption that api is seen for only once");
				}
				apiToRepo.put(api, new HashSet<>());
				continue;
			}
			String url = line.split(": ")[0].trim();
			if ("http://github.com/".equals(url)) {
				continue; // weird data
			}
//			System.out.println(api);
//			System.out.println(url);
			apiToRepo.get(api).add(url.split("http://github.com/")[1]);
		}

		return apiToRepo;
	}

	private static Map<String, String> getLongMapping(Map<String, Set<String>> apiRepos) {
		Map<String, String> result = new HashMap<>();
		for (String key : GraphBuildingUtils.APIToMethodName.keySet()) {
			for (String API : apiRepos.keySet()) {
				String APIForComparison; // needed because some method names are associated with the interface/superclass etc
				if (API.contains("PdfLayer")) {
					APIForComparison = API.replace("PdfLayer", "PdfDictionary");
					
				} else {
					APIForComparison = API;
				}
				if (key.contains(APIForComparison)) {
					result.put(API, key);
					System.out.println("inserting mapping " + API + " -> " + key);
				}
			}
		}
		 
		return result;
	}

	public static void preprocess(List<String> APIs) throws FileNotFoundException, IOException {
		for (String API : APIs) {
			if (!apiRepos.keySet().contains(API)) {
				throw new RuntimeException(API + " is missing from known APIs");
			}
		}
		
		EGroumBuilder.USE_FALLBACK = true; // this modifies the behavior during matching.
		for (String API : APIs) {
			Random r = new java.util.Random();

			Set<String> repos = apiRepos.get(API);

			System.out.println(API);
			String outputDirectory = output + "mudetectxp_checkouts_" + API + "/";

			if (new File(outputDirectory + "labels.csv").exists()) {
				System.out.println("deleting existing labels.csv first");
				new File(outputDirectory + "labels.csv").delete();
			}

			System.out.println("creating directory = " + outputDirectory);
			new File(outputDirectory).mkdir();
			for (String repo : repos) {

				String repoDir = checkoutLocation + repo;
				
				System.out.println("checking directory = " + repoDir);
				if (!(new File(repoDir).exists())) {
					System.out.println("skipping missing directory = " + repoDir);
					continue;
				}

				String repoAsId = repo.replace("/", "_");
				try (BufferedWriter trainingDataWriter = new BufferedWriter(
						new FileWriter(outputDirectory + repoAsId + "_labels.csv", true))
						) {

					trainingDataWriter.write("location,label(either 'M' for misuse or 'C' for correct usage)\n");

					List<File> files = Files.walk(Paths.get(repoDir))
							.filter(Files::isRegularFile)
							.filter(path -> path.toString().endsWith(".java"))
							.filter(path -> !path.toString().contains("/_")) 
							.filter(path -> !path.toString().contains("/.")) 
							.map(Path::toFile)
							.collect(Collectors.toList());
					for (File file : files) {
			
						System.out.println("path is " + file.toPath());

//						String id = file.toString().substring(outputDirectory.length()).split("/")[0];

						String code;
						try {
							code = new String(Files.readAllBytes(file.toPath()));

							String filePath = file.toString();
							String additionalJar = ExtendedAUGTypeUsageExamplePredicate.additionalJar.get(API);
							
							CompilationUnit cu = (CompilationUnit) JavaASTUtil.parseSource(code, filePath,
									filePath.substring(filePath.lastIndexOf("/")),
//									additionalJar != null 
//											? new String[] { filePath, additionalJar }
//											: new String[] { filePath }
									null
											);

							Set<String> uniq = new HashSet<>();
							Set<String> clones = new HashSet<>(); // clones can only map to unlabeled

							for (int i = 0; i < cu.types().size(); i++) {
								if (cu.types().get(i) instanceof TypeDeclaration) {
									TypeDeclaration typ = (TypeDeclaration) cu.types().get(i);

									String moreSpecificAPIId = apiToSpecificMethod.get(API);
									String APIClass = GraphBuildingUtils.APIToClass.get(moreSpecificAPIId);
									if (moreSpecificAPIId.contains("init>") && EGroumBuilder.USE_FALLBACK) {
										// is constructor
										// and when using USE_FALLBACK, will match too many things
										// do some kind of unsound filter first
										if (!code.contains(APIClass)) {
											continue;
										}
									}
									
									handleOneTypeDeclaration(API, repoAsId, filePath, uniq, clones, typ, outputDirectory);

								}
							}
							for (String item : uniq) {
								int randomValue = r.nextInt(100);
								if (randomValue < percentageToLabel) { // [0..percentageToLabel-1]
									trainingDataWriter.write(item);
									trainingDataWriter.write(",?\n");
								} else {
									trainingDataWriter.write(item);
									trainingDataWriter.write(",\n");
								}
							}

						} catch (IOException e) {
							e.printStackTrace();
							System.out.println("Unable to parse java file due ot io exception");
							throw new RuntimeException(e);
						} catch (IllegalStateException ise) {
							ise.printStackTrace();
							System.out.println("Unable to parse java file");
//							return;
						} catch (NullPointerException npe) {
							npe.printStackTrace();
							System.out.println("Unable to parse java file");
//							return;
						}

					}
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}

				System.out.println("It's time to start labeling. Fill in " + outputDirectory + "labels.csv");

			}
		}
	}

	private static void handleOneTypeDeclaration(String API, String id, String filePath, Set<String> uniq,
			Set<String> clones, TypeDeclaration typ, String outputDirectory) {
		String moreSpecificAPIId = apiToSpecificMethod.get(API);
		if (moreSpecificAPIId == null ) {
			System.out.println("the original data did not use the API " + API);
			return;
		}
		for (MethodDeclaration md : typ.getMethods()) {	

			if (!EAUGUsageExamplesOf(GraphBuildingUtils.APIToMethodName.get(moreSpecificAPIId),
					GraphBuildingUtils.APIToClass.get(moreSpecificAPIId)).matches(md)) {

				System.out.println("\tno match " + typ.getName() + "#" + md.getName());
				continue;
			}

			if (EAUGUtils.isTooBig(md)) {
				System.out.println("\ttoo big");
				continue;
			}
			
			

			boolean isClone = checkIfCloneOfPreviousAndUpdatePaths(filePath, md);

			String sig = JavaASTUtil.buildSignature(md);
			if (isClone) {
				clones.add(id + " - " + typ.getName().getIdentifier() + "." + sig);
			} else {
				uniq.add(id + " - " + typ.getName().getIdentifier() + "." + sig);
				
				// copy the file out for easy reference
				if (!new File(outputDirectory + "/files").exists()) {
					new File(outputDirectory + "/files").mkdir();
				} 
				File filesForLabeling = new File(outputDirectory + "/files/" + id + "." + typ.getName().getIdentifier() + ".java");
				if (!filesForLabeling.exists()) {
					try {
						Files.copy(new File(filePath).toPath(), filesForLabeling.toPath());
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("failed to copy " + filePath);
						throw new RuntimeException(e);
					}
				}
				
			}
		}

		for (TypeDeclaration innerTyp : typ.getTypes()) {
			handleOneTypeDeclaration(API, id, filePath, uniq, clones, innerTyp, outputDirectory);
		}
	}

	private static boolean checkIfCloneOfPreviousAndUpdatePaths(String path, MethodDeclaration md) {

		Set<String> tokens = new HashSet<>();

		NaiveASTFlattener printer = new NaiveASTFlattener();
		md.accept(printer);
		String body = printer.getResult();
		tokens = Sets.newHashSet(body.split(" "));
		tokens.remove(""); // if there's empty string, just remove it.

		String isCloneOf = null;

		for (Map.Entry<String, Set<String>> entry : pathsAndMethodToTokens.entrySet()) {
			Set<String> tokensInCloneType = entry.getValue();
			float ratio = intersectionRatio(tokensInCloneType, tokens);
			if (ratio > 0.95) { // is clone
				isCloneOf = entry.getKey();
			}
		}

		if (isCloneOf != null) {
			pathsAndMethodToCounts.put(isCloneOf, pathsAndMethodToCounts.get(isCloneOf) + 1);
			System.out.println("\t is clone!");
			return true;
		} else {
			pathsAndMethodToCounts.put(path + "::" + md.getName(), 0);
			pathsAndMethodToTokens.put(path + "::" + md.getName(), tokens);
			System.out.println("\t is not clone!");
			return false;
		}
	}

	private static float intersectionRatio(Set<String> canonicalCopy, Set<String> tokens) {
		Set<String> intersection = new HashSet<>();
		intersection.addAll(canonicalCopy);
		intersection.retainAll(tokens);

		int numerator = intersection.size();
		float denominator = Math.min(canonicalCopy.size(), tokens.size());
		return numerator / denominator;
	}
	
	
	public static void main(String... args) throws FileNotFoundException, IOException {

		preprocess(Arrays.asList(args));
//		
//		File file = new File("ow/src/closure/net.vtst.ow.closure.compiler/src/net/vtst/ow/closure/compiler/util/ListWithoutDuplicates.java");
//		String code = new String(Files.readAllBytes(file.toPath()));
//
//		String filePath = file.toString();
//
//		CompilationUnit cu = (CompilationUnit) JavaASTUtil.parseSource(code, filePath,
//				filePath.substring(filePath.lastIndexOf("/")),
//				null);
////						new String[] { filePath, "/Users/kanghongjin/Downloads/closure-compiler-v20210202.jar" });
//		System.out.println(cu);
	}
}
