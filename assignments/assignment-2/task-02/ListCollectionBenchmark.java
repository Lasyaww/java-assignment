import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Deque;

/** Q2: Basic ArrayList, LinkedList and ArrayDeque operation comparison. */
public class ListCollectionBenchmark {
    private static volatile long sink;

    private static long timeNanos(Runnable operation) {
        long start = System.nanoTime();
        operation.run();
        return System.nanoTime() - start;
    }

    private static void run(int size) {
        System.out.println("\nCollection size: " + size);
        List<Integer> array = new ArrayList<>();
        List<Integer> linked = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            array.add(i);
            linked.add(i);
        }

        System.out.println("ArrayList append ns: " + timeNanos(() -> array.add(-1)));
        System.out.println("LinkedList append ns: " + timeNanos(() -> linked.add(-1)));
        System.out.println("ArrayList front insert ns: " + timeNanos(() -> array.add(0, -2)));
        System.out.println("LinkedList front insert ns: " + timeNanos(() -> linked.add(0, -2)));
        System.out.println("ArrayList middle remove ns: "
                + timeNanos(() -> array.remove(array.size() / 2)));
        System.out.println("LinkedList middle remove ns: "
                + timeNanos(() -> linked.remove(linked.size() / 2)));
        System.out.println("ArrayList get middle ns: "
                + timeNanos(() -> sink += array.get(array.size() / 2)));
        System.out.println("LinkedList get middle ns: "
                + timeNanos(() -> sink += linked.get(linked.size() / 2)));
        System.out.println("ArrayList iteration ns: " + timeNanos(() -> {
            for (int value : array) sink += value;
        }));
        System.out.println("LinkedList iteration ns: " + timeNanos(() -> {
            for (int value : linked) sink += value;
        }));

        Deque<Integer> upNext = new ArrayDeque<>();
        System.out.println("ArrayDeque add both ends ns: " + timeNanos(() -> {
            upNext.addFirst(1);
            upNext.addLast(2);
            upNext.removeFirst();
            upNext.removeLast();
        }));
    }

    public static void main(String[] args) {
        run(1000);
        run(10000);
        run(50000);
        System.out.println("Measurement sink: " + sink);
    }
}
