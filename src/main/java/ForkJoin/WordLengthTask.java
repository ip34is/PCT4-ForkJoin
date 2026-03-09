package ForkJoin;

import java.util.*;
import java.util.concurrent.RecursiveTask;

public class WordLengthTask extends RecursiveTask<Map<Integer, Integer>> {
    private final List<String> words;
    private static final int THRESHOLD = 2000;

    public WordLengthTask(List<String> words) {
        this.words = words;
    }

    @Override
    protected Map<Integer, Integer> compute() {
        if (words.size() <= THRESHOLD) {
            Map<Integer, Integer> localMap = new HashMap<>();
            for (String word : words) {
                int length = word.replaceAll("[^a-zA-Zа-яА-Я0-9]", "").length();
                if (length > 0) {
                    localMap.merge(length, 1, Integer::sum);
                }
            }
            return localMap;
        }

        int mid = words.size() / 2;
        WordLengthTask left = new WordLengthTask(words.subList(0, mid));
        WordLengthTask right = new WordLengthTask(words.subList(mid, words.size()));

        left.fork();
        Map<Integer, Integer> rightResult = right.compute();
        Map<Integer, Integer> leftResult = left.join();

        leftResult.forEach((k, v) -> rightResult.merge(k, v, Integer::sum));
        return rightResult;
    }
}