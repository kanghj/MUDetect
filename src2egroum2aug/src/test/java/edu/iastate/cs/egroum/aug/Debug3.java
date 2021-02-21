package edu.iastate.cs.egroum.aug;

import de.tu_darmstadt.stg.mudetect.aug.model.APIUsageExample;
import de.tu_darmstadt.stg.mudetect.aug.model.APIUsageGraph;
import de.tu_darmstadt.stg.mudetect.aug.model.dot.DisplayAUGDotExporter;
import edu.iastate.cs.egroum.dot.DotGraph;
import smu.hongjin.EnhancedAUG;
import smu.hongjin.EnhancedAUGAsGraph;
import smu.hongjin.LiteralsUtils;
import smu.hongjin.SubgraphMiningFormatter;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static de.tu_darmstadt.stg.mudetect.aug.AUGTestUtils.exportAUGsAsPNG;
import static edu.iastate.cs.egroum.aug.AUGBuilderTestUtils.buildAUGsForClasses;

public class Debug3 {
	
	String mapExample1 = "public class MultiMap<A, B> implements Iterable<Entry<A,B>> {\n" + 
			"  private Map<A, Set<B>> relation;\n"
			+ "public boolean contains(Entry<A, B> e) {\n" + 
			"    Set<B> test = relation.get(e.getKey());\n" + 
			"    \n" + 
			"    if (test == null) {\n" + 
			"      return false;\n" + 
			"    } else {\n" + 
			"      return test.contains(e.getValue());\n" + 
			"    }\n" + 
			"  }";
	
	String mapExample2 = "public abstract class AbstractDataCache<T extends AbstractData>\n" + 
			"{"
			+ "protected final Class<T> type;\n" + 
			"	protected final Map<String, T> cache;\n" + 
			"\n" + 
			"	protected final PlayerData main;\n" + 
			"	protected final JavaPlugin plugin;\n" 
			+ "public final T getData(String key)\n" + 
			"	{\n" + 
			"		// Check cache first\n" + 
			"		T data = cache.get(key);\n" + 
			"		if (data == null)\n" + 
			"		{\n" + 
			"			// Attempt to load it\n" + 
			"			data = loadData(key);\n" + 
			"			if (data == null)\n" + 
			"			{\n" + 
			"				// Corrupt data :(\n" + 
			"				return null;\n" + 
			"			}\n" + 
			"\n" + 
			"			// Cache it\n" + 
			"			cache.put(key, data);\n" + 
			"		}\n" + 
			"\n" + 
			"		return data;\n" + 
			"	}"
			+ ""
			+ "}";
	
	String mapExample3 = "import java.io.IOException;\n" + 
			"import java.io.InputStream;\n" + 
			"import java.util.HashMap;\n" + 
			"import java.util.Iterator;\n" + 
			"import java.util.Map;\n" + 
			"import java.util.Map.Entry;\n" + 
			"import java.util.Properties;\n" + 
			"import java.util.StringTokenizer;\n" + 
			"public class ConfigUtil {\n" +
			"public static Map<String, String> PropMap = new HashMap<String, String>();" + 
			"	public String getValue(String key) {\n" + 
			"		String value = PropMap.get(key);\n" + 
			"		if (value == null) {\n" + 
			"			init();\n" + 
			"			value = PropMap.get(key);\n" + 
			"		}\n" + 
			"		return value;\n" + 
			"	}"
			+ "}";
	
	String badMapExample = "import java.util.ArrayList;\n" + 
			"import java.util.LinkedList;\n" + 
			"import java.util.Collection;\n" + 
			"import java.util.LinkedHashMap;\n" + 
			"import java.util.Map;\n" +
			"public class CollectionTest {" 
			+ "public static void main(String[] args) {"
			+ "Map<String, Collection<String>> map = new LinkedHashMap<>();\n" 
			+ "for (String collection : map.keySet()) {" 
			+ "System.out.println(map.get(collection));" 
			+ "}\n"
			+ "}";
	
    @Test
    public void debug() {
    	String code = badMapExample;
//    	String code =
//        		"import java.util.Collection;\n" + 
//        		"import java.util.Collections;\n" + 
//        		"import java.util.HashMap;\n" + 
//        		"import java.util.Map;\n" + 
//        		"import java.util.Set;\n" + 
//        		"import java.util.concurrent.ConcurrentMap;\n" + 
//        		"public class CopyOnWriteMap<K,V> implements ConcurrentMap<K, V> {\n" + 
//        		"	private volatile Map<K, V> delegate = Collections.emptyMap();\n" + 
//        		"\n" + 
//        		"\n" + 
//        		"	@Override\n" + 
//        		"    public V putIfAbsent(K k, V v) { "
//        		+ "	synchronized(this) { "
//        		+ "if (!containsKey(k)) "
//        		+ "return put(k, v); else "
//        		+ "return get(k); } }"
//        		+ "    //must be called under lock\n" + 
//        		"public V get(K k) { this.internalMap.get(k); }" +
//        		"}";
//    	String code ="V existing = delegate.get(key);\n" + 
//    			"if(existing != null) {\n" + 
//    			"    return existing;\n" + 
//    			"}";
        LiteralsUtils.isTestTime = true;
        ArrayList<APIUsageExample> augs = buildAUGsForClasses(new String[]{code});
        exportAUGsAsPNG(augs, "./output/", "Debug-aug");
        
        
        Set<EnhancedAUG> enhanced = EnhancedAUG.buildEnhancedAugs(new HashSet<>(augs));
        Map<String, Integer> map1 = new HashMap<>();
		Map<String, Integer> map2 = new HashMap<>();
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("./output/" + "debug" + "_formatted.txt"));
				BufferedWriter mappingwriter = new BufferedWriter(new FileWriter("./output/" + "debug" + "_mapping.txt"))) {
			Map<String, String> labels = new HashMap<>();
			for (EnhancedAUG eaug : enhanced) {
				String labelId = "id-" + " - " + eaug.aug.name;
				labels.put(labelId, "U");
			}
			
			SubgraphMiningFormatter.convert(enhanced, EnhancedAUG.class, 0, map1, map2, "id-", labels, 1, "", writer, mappingwriter);
			
			
			
			System.out.println("will write to \"./output/\" + API + \"_vertmap.txt\""+ " = ./output/" + "debug" +  "_vertmap.txt");
			try (BufferedWriter writerVertex = new BufferedWriter(new FileWriter("./output/" + "debug" +  "_vertmap.txt"))) {
				for (Entry<String, Integer> entry1 : map1.entrySet()) {
					writerVertex.write(entry1.getKey() + "," + entry1.getValue() + "\n");
				}
			}
			System.out.println("will write to \"./output/\" + API + \"_edgemap.txt\"");
			try (BufferedWriter writerEdge = new BufferedWriter(new FileWriter("./output/" + "debug" +  "_edgemap.txt"))) {
				for (Entry<String, Integer> entry1 : map2.entrySet()) {
					writerEdge.write(entry1.getKey() + "," + entry1.getValue() + "\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (EnhancedAUG one : enhanced) {
			EnhancedAUGAsGraph renderableGraph = new EnhancedAUGAsGraph();
			renderableGraph.build(one);
			try {
				System.out.println("will write to \"./output/debug3.png");
				print(renderableGraph);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}
		

//        Collection<EGroumGraph> egroums = buildEGroumsForClasses(new String[] {code});
//        exportEGroumsAsPNG(egroums, "./output", "Debug-egroum");
    }

    private void print(EnhancedAUGAsGraph groum) throws IOException, InterruptedException {
        DisplayAUGDotExporter exporter = new DisplayAUGDotExporter();
        exporter.toPNGFile(groum, new File("output", "debug_map_bad.png"));
	}
    
    private Collection<EGroumGraph> buildEGroumsForClasses(String[] sources) {
        return Arrays.stream(sources)
                .flatMap(source -> buildEGroumsForClass(source).stream())
                .collect(Collectors.toList());
    }

    private ArrayList<EGroumGraph> buildEGroumsForClass(String source) {
        String projectName = "test";
        String basePath = AUGBuilderTestUtils.class.getResource("/").getFile() + projectName;
        return new EGroumBuilder(new AUGConfiguration()).buildGroums(source, basePath, projectName, null);
    }

    private void exportEGroumsAsPNG(Collection<EGroumGraph> egroums, String pathname, String name) {
        Iterator<EGroumGraph> it = egroums.iterator();
        for (int i = 0; it.hasNext(); i++) {
            EGroumGraph egroum = it.next();
            new DotGraph(egroum).toPNG(new File(pathname), name + "-" + i);
        }
    }
}
