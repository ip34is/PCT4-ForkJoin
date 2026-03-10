package ForkJoin;

import java.io.File;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        List<String> text = Arrays.asList(
                "Java", "is", "a", "powerful", "language", "for", "concurrency",
                "ForkJoin", "framework", "helps", "to", "process", "tasks", "parallel"
        );
        Map<Integer, Integer> stats = pool.invoke(new WordLengthTask(text));

        int totalWords = stats.values().stream().mapToInt(Integer::intValue).sum();
        double avg = stats.entrySet().stream()
                .mapToDouble(e -> e.getKey() * e.getValue()).sum() / totalWords;

        double variance = stats.entrySet().stream()
                .mapToDouble(e -> Math.pow(e.getKey() - avg, 2) * e.getValue()).sum() / totalWords;
        double sd = Math.sqrt(variance);

        System.out.println("Статистика довжини слів");
        System.out.println("Гістограма частот (Довжина=Кількість): " + stats);
        System.out.printf("Середнє значення: %.2f\n", avg);
        System.out.printf("Середньо-квадратичне відхилення: %.2f\n\n", sd);

        int size = 3000;
        double[][] A = MatrixUtils.generate(size);
        double[][] B = MatrixUtils.generate(size);
        double[][] C = new double[size][size];

        long s1 = System.nanoTime();
        MatrixUtils.sequentialMultiply(A, B);
        long e1 = System.nanoTime();
        double tSeq = (e1 - s1) / 1_000_000_000.0;

        long s2 = System.nanoTime();
        pool.invoke(new MatrixTask(A, B, C, 0, size));
        long e2 = System.nanoTime();
        double tFJ = (e2 - s2) / 1_000_000_000.0;

        double speedup = tSeq / tFJ;
        int procs = Runtime.getRuntime().availableProcessors();
        double efficiency = speedup / procs;

        System.out.println("Ефективність ForkJoin");
        System.out.printf("Послідовно: %.4f с | ForkJoin: %.4f с\n", tSeq, tFJ);
        System.out.printf("Прискорення (S): %.2fx | Ефективність (E): %.2f\n\n", speedup, efficiency);


        List<List<String>> docs = Arrays.asList(
                Arrays.asList("apple", "banana", "cherry", "java"),
                Arrays.asList("banana", "cherry", "dragonfruit", "java"),
                Arrays.asList("cherry", "java", "kiwi", "banana")
        );
        Set<String> commonWords = pool.invoke(new CommonWordsTask(docs));
        System.out.println("Спільні слова\n" + commonWords + "\n");


        File dir = new File("test_docs");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
            if (files != null) {
                ITSearchTask itTask = new ITSearchTask(files, 0, files.length);
                List<File> found = pool.invoke(itTask);
                System.out.println("IT Search\nЗнайдено IT-документів: " + found.size());
            }
        }
    }
}