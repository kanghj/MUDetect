package smu.hongjin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.tu_darmstadt.stg.mudetect.aug.model.APIUsageExample;
import de.tu_darmstadt.stg.mudetect.aug.model.APIUsageGraph;

public class EnhancedAUG {

	public APIUsageGraph aug;
	
	Set<APIUsageGraph> related = new HashSet<>();
	Set<String> interfaces = new HashSet<>();
	
	public EnhancedAUG(APIUsageGraph aug, Set<APIUsageGraph>  related, Set<String> interfaces) {
		this.aug = aug;
		this.related = related;
		this.interfaces = interfaces;
	}
	
	public static Set<EnhancedAUG> buildEnhancedAugs(Set<APIUsageGraph> augs) {
		
		Map<String, APIUsageGraph> fieldInit = new HashMap<>();
		
		for (APIUsageGraph aug : augs) {
			if (aug instanceof APIUsageExample) {
				boolean isFieldInit = ((APIUsageExample)aug).getLocation().getMethodSignature().contains("__FieldOfClass__");
				if (!isFieldInit) continue;
				
				String name = ((APIUsageExample)aug).getLocation().getMethodSignature().split("#")[0];
				
				fieldInit.put(name, aug);
			}
			
		}
		
		for (APIUsageGraph aug : augs) {
			if (aug instanceof APIUsageExample) {
				boolean isCtor = aug.isCtor;
				if (!isCtor) continue;
				
				for (String field : aug.fieldsUsed) {
					fieldInit.put(field, aug);
				}
			}
		}
		
		Set<EnhancedAUG> result = new HashSet<>();
		Iterator<APIUsageGraph> iter = augs.iterator();
		while (iter.hasNext()) {
			APIUsageGraph aug = iter.next();
			
			Set<String> fieldsUsed = aug.fieldsUsed;
			Set<APIUsageGraph> relat = new HashSet<>();
			for (String field : fieldsUsed) {
				if (!fieldInit.containsKey(field)) continue;
				
				relat.add(fieldInit.get(field));
			}
			boolean isFieldInit = ((APIUsageExample)aug).getLocation().getMethodSignature().contains("__FieldOfClass__");
			if (isFieldInit) {
				continue;
			}
			
			result.add(new EnhancedAUG(aug, relat, aug.interfaces));
		}
		
		return result;
	}
	
}
