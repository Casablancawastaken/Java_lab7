package java_labs.Lab7.ex2;

public class MultiThreadedMaxInMatrix {
    public static void main(String[] args) {
        int[][] matrix = {
                {11, 50, 33, 56},
                {73, 83, 24, 57},
                {41, 93, 65, 58},
        };

        int numRows = matrix.length;

        int[] maxInRows = new int[numRows];

        Thread[] threads = new Thread[numRows];

        for (int i = 0; i < numRows; i++) {
            final int rowIndex = i;
            threads[i] = new Thread(() -> {
                int max = findMaxInRow(matrix[rowIndex]);
                maxInRows[rowIndex] = max;
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int overallMax = findMaxInArray(maxInRows);

        System.out.println("Наибольший элемент в матрице: " + overallMax);
    }


    private static int findMaxInRow(int[] row) {
        int max = row[0];
        for (int num : row) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    private static int findMaxInArray(int[] array) {
        int max = array[0];
        for (int num : array) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }
}