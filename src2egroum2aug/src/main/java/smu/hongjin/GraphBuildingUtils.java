package smu.hongjin;

import static edu.iastate.cs.egroum.aug.TypeUsageExamplePredicate.usageExamplesOf;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import de.tu_darmstadt.stg.mudetect.aug.model.APIUsageExample;
import edu.iastate.cs.egroum.aug.AUGBuilder;
import edu.iastate.cs.egroum.aug.AUGConfiguration;

public class GraphBuildingUtils {
	public static int i;
	public static int fileCounts;

	public static Map<String, String> APIToClass = new HashMap<>();
	public static Map<String, String> APIToMethodName = new HashMap<>();

	static {
		APIToClass.put("java.lang.Long__parseLong__1", "java.lang.Long");
		APIToMethodName.put("java.lang.Long__parseLong__1", "parseLong");
		
		APIToClass.put("java.io.ObjectOutputStream__writeObject__1", "java.io.ObjectOutputStream");
		APIToMethodName.put("java.io.ObjectOutputStream__writeObject__1", "writeObject");
		
		APIToClass.put("java.io.ByteArrayOutputStream__toByteArray__0", "java.io.ByteArrayOutputStream");
		APIToMethodName.put("java.io.ByteArrayOutputStream__toByteArray__0", "toByteArray");
		
		APIToClass.put("java.util.Map__get__1", "java.util.Map");
		APIToMethodName.put("java.util.Map__get__1", "get");
		
		APIToClass.put("java.sql.PreparedStatement__executeUpdate__0", "java.sql.PreparedStatement");
		APIToMethodName.put("java.sql.PreparedStatement__executeUpdate__0", "executeUpdate");
		
		
		APIToClass.put("java.util.StringTokenizer__nextToken__0", "java.util.StringTokenizer");
		APIToMethodName.put("java.util.StringTokenizer__nextToken__0", "nextToken");
		
		APIToClass.put("javax.crypto.Cipher__init__2", "javax.crypto.Cipher");
		APIToMethodName.put("javax.crypto.Cipher__init__2", "init");
	}
	


	public static void readCounts(String directory, Map<String, Integer> quantities) {
		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(directory + "metadata/metadata.csv"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Couldn't read labels");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		for (String line : lines) {
			String[] splitted = line.split(",");
			String id = splitted[0];
			int quantity = Integer.parseInt(splitted[1]);

			quantities.put(id, quantity);
		}
	}

	public static void readLabels(String directory, Map<String, String> labels) {
		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(directory + "labels.csv"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Couldn't read labels");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		int i = 0;
		for (String line : lines) {
			if (i == 0) {
				i += 1;
				continue;
			}
			String[] splitted = line.split(",");
			String location = splitted[0];
			String label;
			if (splitted.length < 2) {
				label = "U";
			} else {
				label = splitted[1];
			}

			i += 1;
			labels.put(location, label);
		}
	}

	public static Collection<EnhancedAUG> buildAUGsForClassFromSomewhereElse(String classCode, String elseWhere,
			String project, AUGConfiguration configuration) {
		AUGBuilder builder = new AUGBuilder(configuration);
		String basePath = elseWhere;
		Collection<APIUsageExample> aug = builder.build(classCode, basePath, project, null);
		return EnhancedAUG.buildEnhancedAugs(new HashSet<>(aug));
	}
}
