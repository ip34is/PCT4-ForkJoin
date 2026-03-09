package ForkJoin;

import java.util.concurrent.RecursiveAction;

public class MatrixTask extends RecursiveAction {
    private final double[][] A, B, C;
    private final int startRow, endRow;
    private static final int THRESHOLD = 64;

    public MatrixTask(double[][] A, double[][] B, double[][] C, int startRow, int endRow) {
        this.A = A; this.B = B; this.C = C;
        this.startRow = startRow; this.endRow = endRow;
    }

    @Override
    protected void compute() {
        if (endRow - startRow <= THRESHOLD) {
            int n = A.length;
            for (int i = startRow; i < endRow; i++) {
                for (int k = 0; k < n; k++) {
                    double tempA = A[i][k];
                    for (int j = 0; j < n; j++) {
                        C[i][j] += tempA * B[k][j];
                    }
                }
            }
        } else {
            int mid = (startRow + endRow) / 2;
            invokeAll(
                    new MatrixTask(A, B, C, startRow, mid),
                    new MatrixTask(A, B, C, mid, endRow)
            );
        }
    }
}