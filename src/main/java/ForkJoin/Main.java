package ForkJoin;

import java.io.File;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();

        List<String> text = Arrays.asList("Java", "is", "a", "powerful", "language", "for", "concurrency");
        Map<Integer, Integer> stats = pool.invoke(new WordLengthTask(text));
        System.out.println("Статистика довжини слів: " + stats);

        double avg = stats.entrySet().stream()
                .mapToDouble(e -> e.getKey() * e.getValue()).sum() / text.size();
        System.out.println("Середня довжина слова: " + avg);

        int size = 3000;
        double[][] A = MatrixUtils.generate(size);
        double[][] B = MatrixUtils.generate(size);
        double[][] C = new double[size][size];

        long startSeq = System.currentTimeMillis();
        MatrixUtils.sequentialMultiply(A, B);
        long endSeq = System.currentTimeMillis();
        double tSeq = (endSeq - startSeq) / 1000.0;

        ForkJoinPool pool1 = new ForkJoinPool();
        long startFJ = System.currentTimeMillis();
        pool1.invoke(new MatrixTask(A, B, C, 0, size));
        long endFJ = System.currentTimeMillis();
        double tFJ = (endFJ - startFJ) / 1000.0;

        double speedup = tSeq / tFJ;

        System.out.println("Результати:");
        System.out.printf("Час послідовного (T_posl): %.4f с\n", tSeq);
        System.out.printf("Час ForkJoin (T_fj):       %.4f с\n", tFJ);
        System.out.printf("Прискорення (S):           %.2fx\n", speedup);

        List<String> doc1 = Arrays.asList("apple", "banana", "cherry");
        List<String> doc2 = Arrays.asList("banana", "cherry", "dragonfruit");
        Set<String> set1 = pool.invoke(new CommonWordsTask(doc1));
        Set<String> set2 = pool.invoke(new CommonWordsTask(doc2));
        set1.retainAll(set2);
        System.out.println("Спільні слова: " + set1);

        File dir = new File("src/main/java/test_docs");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
            if (files != null) {
                ITSearchTask itTask = new ITSearchTask(files, 0, files.length);
                List<File> itDocuments = pool.invoke(itTask);

                System.out.println("Результати IT Search");
                System.out.println("Знайдено IT-документів: " + itDocuments.size());
                for (File f : itDocuments) {
                    System.out.println("Документ: " + f.getName());
                }
            }
        } else {
            System.out.println("Папка test_docs не знайдена.");
        }
    }
}