import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static List<Integer> myArr = new ArrayList<>();
    public static final int ARRAY_SIZE = 100;
    public static final int MIN_ARR_VALUE = 1;
    public static final int MAX_ARR_VALUE = 10;
    public static long START_TIME = 0;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        createArray();
        START_TIME = System.currentTimeMillis();
        singleStreamArraySum();
        multipleStreamsArraySum();
    }

    public static void createArray() {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            myArr.add(getRandomNumber(MIN_ARR_VALUE, MAX_ARR_VALUE));
        }
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static int singleStreamArraySum() {
        int res = 0;
        for (int i = 0; i < myArr.size(); i++) {
           res += myArr.get(i);
        }
        System.out.printf("Сумма элементов массива - %d. Время выполнения одним потоком %d\n", res, System.currentTimeMillis() - START_TIME);
        return res;
    }

    public static int multipleStreamsArraySum() throws ExecutionException, InterruptedException {
        switch (myArr.size()) {
            case 0: return 0;
            case 1: return myArr.get(0);
            case 2: return myArr.get(0) + myArr.get(1);
            default: return forkTasksAndGetResult();
        }
    }

    public static int forkTasksAndGetResult() throws ExecutionException, InterruptedException {
        final int middle = myArr.size() / 2;
        ArraySumTask task1 = new ArraySumTask(0, middle, myArr);
        ArraySumTask task2 = new ArraySumTask(middle, myArr.size(), myArr);
        final ExecutorService threadPool = Executors.newFixedThreadPool(2);

        List<Callable<Integer>> callablesList = List.of(
                task1,
                task2
        );

        List<Future<Integer>> callables = null;
        try {
            callables = threadPool.invokeAll(callablesList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadPool.shutdown();

        int res = 0;
        for (Future<Integer> task : callables) {
            try {
                res += task.get();
            } catch (ExecutionException | InterruptedException e) {
                throw e;
            }
        }
        System.out.printf("Сумма элементов массива - %d. Время выполнения двумя потоками %d\n", res, System.currentTimeMillis() - START_TIME);
        return res;
    }
}