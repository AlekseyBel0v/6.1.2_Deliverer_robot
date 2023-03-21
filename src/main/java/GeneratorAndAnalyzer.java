import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class GeneratorAndAnalyzer {
    static final Map<Integer, Integer> sizeToFreq = new TreeMap<>(Integer::compareTo);

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static void analyzeRoute(String rout, char letterForAnalysis) {
        int quantity = 0;
        for (int i = 0; i < Main.LENGTHS_OF_THE_ROUT; i++) {
            if (rout.charAt(i) == letterForAnalysis) {
                quantity++;
            }
        }
        synchronized(sizeToFreq) {
            if (!sizeToFreq.containsKey(quantity)) {
                sizeToFreq.put(quantity, 1);
            } else {
                sizeToFreq.put(quantity, sizeToFreq.get(quantity) + 1);
            }
        }
        System.out.println("Путь: " + rout + "; Количество \"" + Main.LETTER_FOR_ANALYSIS + "\" = " + quantity);
    }
}