package java_labs.Lab7.ex3;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class WarehouseTransfer {
    static class Warehouse {
        private final Queue<Integer> items = new LinkedList<>();
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition notEmpty = lock.newCondition();
        private final Condition notFull = lock.newCondition();
        private static final int MAX_CAPACITY = 150;
        private int currentLoad = 0;

        public void addItem(int weight) throws InterruptedException {
            lock.lock();
            try {
                while (currentLoad + weight > MAX_CAPACITY) {
                    System.out.println("Грузчики ждут, чтобы освободить место для товара весом " + weight + " кг.");
                    notFull.await();
                }
                items.add(weight);
                currentLoad += weight;
                System.out.println("Добавлен товар весом " + weight + " кг. Текущий вес: " + currentLoad + " кг.");
                notEmpty.signalAll();
            } finally {
                lock.unlock();
            }
        }

        public int loadItem() throws InterruptedException {
            lock.lock();
            try {
                while (items.isEmpty()) {
                    System.out.println("Грузчики ждут товары.");
                    notEmpty.await();
                }
                int weight = items.poll();
                currentLoad -= weight;
                System.out.println("Грузчики перенесли товар весом " + weight + " кг. Текущий вес: " + currentLoad + " кг.");
                notFull.signalAll();
                return weight;
            } finally {
                lock.unlock();
            }
        }
    }

    static class Loader implements Runnable {
        private final Warehouse warehouse;

        public Loader(Warehouse warehouse) {
            this.warehouse = warehouse;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    int weight = warehouse.loadItem();
                    System.out.println("Грузчик отправил товар весом " + weight + " кг на другой склад.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Грузчик завершил работу.");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Warehouse warehouse = new Warehouse();

        for (int i = 0; i < 3; i++) {
            new Thread(new Loader(warehouse)).start();
        }

        int[] items = {50, 30, 70, 40, 60, 20, 80, 10, 90};
        for (int weight : items) {
            warehouse.addItem(weight);
        }
    }
}