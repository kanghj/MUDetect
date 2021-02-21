package smu.hongjin;

import java.util.HashMap;
import java.util.Map;

public class LiteralsUtils {
	
	public static boolean isTestTime = false;
	public static boolean TURN_OFF_EXTENSIONS = false; // // used for FSE rebutal. Answers "Do the extensions really help quantitatively?".
	// remmeber to look in SubgraphMiningFormatter as well, when adjusting this value

	public static Map<String, Integer> counter = new HashMap<>();
	
	public static void increaseFreq(String literalString) {
		counter.putIfAbsent(literalString, 0);
		counter.put(literalString, counter.get(literalString) + 1);
		
	}
	
	public static int getFreq(String literalString) {
		if (TURN_OFF_EXTENSIONS) {
			return 0;
		}
		if (!counter.containsKey(literalString)) {
			if (!isTestTime) {
				throw new RuntimeException("Odd. Found a literal string we did not see in the first pass");
			} else {
				return 0;
			}
		}
		return counter.containsKey(literalString) ? counter.get(literalString) : 0;
	}
}
