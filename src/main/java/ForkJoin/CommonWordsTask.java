package ForkJoin;

import java.util.*;
import java.util.concurrent.RecursiveTask;

public class CommonWordsTask extends RecursiveTask<Set<String>> {
    private final List<List<String>> documents;

    public CommonWordsTask(List<List<String>> documents) {
        this.documents = documents;
    }

    @Override
    protected Set<String> compute() {
        if (documents.size() == 1) {
            Set<String> words = new HashSet<>();
            for (String line : documents.get(0)) {
                String[] parts = line.toLowerCase().split("\\s+");
                for (String p : parts) {
                    String clean = p.replaceAll("[^a-zа-я]", "");
                    if (!clean.isEmpty()) words.add(clean);
                }
            }
            return words;
        }
        int mid = documents.size() / 2;
        CommonWordsTask t1 = new CommonWordsTask(documents.subList(0, mid));
        CommonWordsTask t2 = new CommonWordsTask(documents.subList(mid, documents.size()));

        t1.fork();
        Set<String> res2 = t2.compute();
        Set<String> res1 = t1.join();

        res1.retainAll(res2);
        return res1;
    }
}