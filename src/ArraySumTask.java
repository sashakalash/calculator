import java.util.List;
import java.util.concurrent.Callable;

public class ArraySumTask implements Callable {
    public int start;
    public int end;
    public List<Integer> arr;

    public ArraySumTask(int start, int end, List<Integer> arr) {
        this.start = start;
        this.end = end;
        this.arr = arr;
    }

    @Override
    public Object call() {
        int res = 0;
        for (int i = start; i < end; i++) {
            res += arr.get(i);
        }
        return res;
    }
}