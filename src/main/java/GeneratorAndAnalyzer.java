import java.util.*;

public class GeneratorAndAnalyzer {
    static final Map<Integer, Integer> sizeToFreq = new TreeMap<>(Integer::compareTo);
    static StateOfMap stateOfMap = new StateOfMap();
    static Object monitor = new Object();

    /**
     * Метод генерирует строку, состоящую из набора символов
     */
    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    /**
     * Метод анализирует строку, выводит на экран ее и сколько определенных сиволов она содержит.
     */
    public static void analyzeRoute(String rout, char letterForAnalysis) {
        int quantity = 0;
        for (int i = 0; i < Main.LENGTHS_OF_THE_ROUT; i++) {
            if (rout.charAt(i) == letterForAnalysis) {
                quantity++;
            }
        }
        synchronized (monitor) {    // этот монитор выстраивает очередь из потоков в списке analyzers
            synchronized (stateOfMap) {     // этот монитор отвечает за переключение между данным потоком и потоком currentHigh
                if (stateOfMap.isUpdate) {
                    stateOfMap.notify();
                    try {
                        stateOfMap.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                synchronized (sizeToFreq) {     // этот монитор блокирует sizeToFreq на всякий случай
                    if (!sizeToFreq.containsKey(quantity)) {
                        sizeToFreq.put(quantity, 1);
                    } else {
                        sizeToFreq.put(quantity, sizeToFreq.get(quantity) + 1);
                    }
                    System.out.println("Путь: " + rout + "; Количество \"" + Main.LETTER_FOR_ANALYSIS + "\" = " + quantity);
                }
                stateOfMap.isUpdate = true;
                stateOfMap.notify();
            }
        }
    }
}

class StateOfMap {
    Boolean isUpdate = false;
}