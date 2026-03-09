package ForkJoin;

import java.util.Random;

public class MatrixUtils {
    public static double[][] generate(int n) {
        double[][] matrix = new double[n][n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = rand.nextDouble() * 10;
            }
        }
        return matrix;
    }

    public static double[][] sequentialMultiply(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                double tempA = A[i][k];
                for (int j = 0; j < n; j++) {
                    C[i][j] += tempA * B[k][j];
                }
            }
        }
        return C;
    }

    public static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double val : row) {
                System.out.printf("%8.2f ", val);
            }
            System.out.println();
        }
    }

    public static boolean compare(double[][] m1, double[][] m2, double delta) {
        if (m1.length != m2.length || m1[0].length != m2[0].length) return false;
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                if (Math.abs(m1[i][j] - m2[i][j]) > delta) return false;
            }
        }
        return true;
    }
}