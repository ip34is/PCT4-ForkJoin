package ForkJoin;

import java.util.*;
import java.util.concurrent.RecursiveTask;

public class CommonWordsTask extends RecursiveTask<Set<String>> {
    private final List<String> lines;

    public CommonWordsTask(List<String> lines) {
        this.lines = lines;
    }

    @Override
    protected Set<String> compute() {
        if (lines.size() <= 50) {
            Set<String> words = new HashSet<>();
            for (String line : lines) {
                String[] parts = line.toLowerCase().split("\\s+");
                for (String p : parts) {
                    String clean = p.replaceAll("[^a-zа-я]", "");
                    if (!clean.isEmpty()) words.add(clean);
                }
            }
            return words;
        }

        int mid = lines.size() / 2;
        CommonWordsTask t1 = new CommonWordsTask(lines.subList(0, mid));
        CommonWordsTask t2 = new CommonWordsTask(lines.subList(mid, lines.size()));

        t1.fork();
        Set<String> res2 = t2.compute();
        Set<String> res1 = t1.join();

        res1.addAll(res2);
        return res1;
    }
}