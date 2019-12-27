package edu.iastate.cs.egroum.aug;

import de.tu_darmstadt.stg.mudetect.aug.model.APIUsageExample;
import edu.iastate.cs.egroum.dot.DotGraph;
import smu.hongjin.EnhancedAUG;
import smu.hongjin.SubgraphMiningFormatter;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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

public class Debug {
    @Test
    public void debug() throws IOException {
        String code = new String(Files.readAllBytes(
        		Paths.get("/Users/kanghongjin/Downloads/github-code-search/java.io.ByteArrayOutputStream__toByteArray__0/755/com/ftkj/util/ByteUtil.java")));

        ArrayList<APIUsageExample> augs = buildAUGsForClasses(new String[]{code});
        exportAUGsAsPNG(augs, "./output/", "Debug-aug");
        
        Set<EnhancedAUG> enhanced = EnhancedAUG.buildEnhancedAugs(new HashSet<>(augs));
//        Map<String, Integer> map1 = new HashMap<>();
//		Map<String, Integer> map2 = new HashMap<>();
//		try (BufferedWriter writer = new BufferedWriter(new FileWriter("./output/" + "debug" + "_formatted.txt"))) {
//			SubgraphMiningFormatter.convert(enhanced, EnhancedAUG.class, 0, map1, map2, writer);
//			System.out.println("will write to \"./output/\" + API + \"_vertmap.txt\""+ " = ./output/" + "debug" +  "_vertmap.txt");
//			try (BufferedWriter writerVertex = new BufferedWriter(new FileWriter("./output/" + "debug" +  "_vertmap.txt"))) {
//				for (Entry<String, Integer> entry1 : map1.entrySet()) {
//					writerVertex.write(entry1.getKey() + "," + entry1.getValue() + "\n");
//				}
//			}
//			System.out.println("will write to \"./output/\" + API + \"_edgemap.txt\"");
//			try (BufferedWriter writerEdge = new BufferedWriter(new FileWriter("./output/" + "debug" +  "_edgemap.txt"))) {
//				for (Entry<String, Integer> entry1 : map2.entrySet()) {
//					writerEdge.write(entry1.getKey() + "," + entry1.getValue() + "\n");
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		

        Collection<EGroumGraph> egroums = buildEGroumsForClasses(new String[] {code});
        exportEGroumsAsPNG(egroums, "./output", "Debug-egroum");
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

interface CI {

	void get(InputStream is) throws IOException;
	
}

class C implements CI {
//	private final String HMM = "1";
//	public List<String> hellos = Arrays.asList("1","2","3");
//	
	public C() {
//		hellos = Arrays.asList("5","6","7");
	}
	
	@Override
    public void get(java.io.InputStream is) throws IOException {
		java.io.BufferedReader reader = null;
        try {
        	reader = new java.io.BufferedReader(new java.io.FileReader("file.txt"));
//        	if (hellos.isEmpty()) return;
            is.read();
            int i = 0;
            System.out.println(i);
        } catch (IOException e) {
            // ignore
        	return;
        } finally {
        	reader.close();
        }
        is.close();
    }
}