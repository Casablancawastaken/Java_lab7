package java_labs.Lab7.ex1;

public class MultiThreadedSum {
    public static void main(String[] args) {
        int[] array = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        int mid = array.length / 2;

        SumCalculator sum1 = new SumCalculator(array, 0, mid);
        SumCalculator sum2 = new SumCalculator(array, mid, array.length);

        Thread thread1 = new Thread(sum1);
        Thread thread2 = new Thread(sum2);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int totalSum = sum1.getSum() + sum2.getSum();

        System.out.println("Общая сумма элементов массива: " + totalSum);
    }
}

class SumCalculator implements Runnable {
    private final int[] array;
    private final int start;
    private final int end;
    private int sum;

    public SumCalculator(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            sum += array[i];
        }
    }

    public int getSum() {
        return sum;
    }
}
