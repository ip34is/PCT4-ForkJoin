package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ForkJoin.MatrixUtils;
import ForkJoin.MatrixTask;

import java.util.concurrent.ForkJoinPool;

public class MatrixTest {

    @Test
    @DisplayName("Перевірка коректності паралельного множення ForkJoin проти послідовного")
    public void testMultiplicationCorrectness() {
        int size = 3000;
        double[][] A = MatrixUtils.generate(size);
        double[][] B = MatrixUtils.generate(size);
        double[][] actualC = new double[size][size];

        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new MatrixTask(A, B, actualC, 0, size));

        double[][] expectedC = MatrixUtils.sequentialMultiply(A, B);

        for (int i = 0; i < size; i++) {
            Assertions.assertArrayEquals(expectedC[i], actualC[i], 1e-9,
                    "Невідповідність у рядку " + i);
        }

        System.out.println("✅ Тест пройдено успішно: Паралельний результат збігається з еталоном.");
    }

    @Test
    @DisplayName("Перевірка на порожніх або мінімальних матрицях")
    public void testSmallMatrix() {
        int size = 1;
        double[][] A = {{5.0}};
        double[][] B = {{2.0}};
        double[][] C = new double[1][1];

        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new MatrixTask(A, B, C, 0, 1));

        Assertions.assertEquals(10.0, C[0][0], 1e-9);
    }
}