package edu.iastate.cs.egroum.aug;

import de.tu_darmstadt.stg.mudetect.aug.model.APIUsageExample;
import edu.iastate.cs.egroum.dot.DotGraph;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

import static de.tu_darmstadt.stg.mudetect.aug.AUGTestUtils.exportAUGsAsPNG;
import static edu.iastate.cs.egroum.aug.AUGBuilderTestUtils.buildAUGsForClasses;

public class Debug2 {
    @Test
    public void debug() {
        String code = "import java.util.ArrayList;\n" + 
        		"import java.util.HashMap;\n" + 
        		"import java.util.List;\n" + 
        		"import java.util.Map;\n" + 
        		"\n" + 
        		"public class Test {\n" + 
        		"	public static void main() {\n" + 
        		"		Map<String, Integer> map = new HashMap<>();\n" + 
        		"		map.entrySet().iterator().next();\n" + 
        		"		map.entrySet().iterator().next();\n" + 
        		"		\n" + 
        		"		List<String> hellos = new ArrayList<>();\n" + 
        		"		List<String> hellos1 = new ArrayList<>();\n" + 
        		"		hellos.add(\"hihi\");\n" + 
        		"		for (int i = 0; i < hellos.size(); i++) {\n" + 
        		"			hellos1.add(\"hihi\");\n" + 
        		"		}\n" + 
        		"	}\n" + 
        		"\n" + 
        		"}\n" + 
        		"";

        ArrayList<APIUsageExample> augs = buildAUGsForClasses(new String[]{code, code});
        exportAUGsAsPNG(augs, "./output/", "Debug-aug");

        Collection<EGroumGraph> egroums = buildEGroumsForClasses(new String[] {code, code});
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

