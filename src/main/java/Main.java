import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    static final int QUANTITY_OF_ROUTS_VARIANTS = 1000;     // каждый вариант обрабабтывает отдельный поток
    static final String LETTERS = "RLRFR";                  // из чего состоит "путь"
    static final int LENGTHS_OF_THE_ROUT = 100;             // длина пути
    static final char LETTER_FOR_ANALYSIS = 'R';            // на эту букву будет проанализирован путь
    static Thread currentHigh = new Thread(() -> {
        while (true) {
            while (!GeneratorAndAnalyzer.mapIsUpdated) {
            }
            synchronized (GeneratorAndAnalyzer.sizeToFreq) {
                getMax();
                GeneratorAndAnalyzer.mapIsUpdated = false;
                GeneratorAndAnalyzer.sizeToFreq.notify();
            }
        }
    });

    public static void main(String[] args) {
        currentHigh.setDaemon(true);
        final List<Thread> analyzers = new ArrayList<>();
        ThreadGroup threads = new ThreadGroup("threads");
        for (int i = 0; i < QUANTITY_OF_ROUTS_VARIANTS; i++) {
            analyzers.add(new Thread(threads, () -> {
                String newRout = GeneratorAndAnalyzer.generateRoute(LETTERS, LENGTHS_OF_THE_ROUT);
                GeneratorAndAnalyzer.analyzeRoute(newRout, LETTER_FOR_ANALYSIS);
            }));
            analyzers.get(i).start();
        }

        currentHigh.start();

        while (threads.activeCount() > 0) {
        }
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> entry : GeneratorAndAnalyzer.sizeToFreq.entrySet()) {
            System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
        }
    }

    static void getMax() {
        Map.Entry<Integer, Integer> entryMax = null;
        for (Map.Entry<Integer, Integer> entry : GeneratorAndAnalyzer.sizeToFreq.entrySet()) {
            if (entryMax == null) {
                entryMax = entry;
            }
            if (entry.getValue() > entryMax.getValue()) {
                entryMax = entry;
            }
        }
        System.out.println("Самое частое количество повторений на данный момет " + entryMax.getKey() + "(встретилось " + entryMax.getValue() + " раз)");
    }
}
