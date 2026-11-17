import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/** Q4: Compare maps for fast lookup, sorted reports and insertion-order reports. */
public class PharmacyInventory {
    private static void run(int count) {
        Map<String, Integer> hash = new HashMap<>();
        Map<String, Integer> tree = new TreeMap<>();
        Map<String, Integer> linked = new LinkedHashMap<>();

        for (int i = count - 1; i >= 0; i--) {
            String code = String.format("MED%06d", i);
            hash.put(code, i + 10);
            tree.put(code, i + 10);
            linked.put(code, i + 10);
        }

        String target = String.format("MED%06d", count / 2);
        long start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            Integer stock = hash.get(target);
            if (stock == null) throw new IllegalStateException();
        }
        long hashLookup = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            Integer stock = tree.get(target);
            if (stock == null) throw new IllegalStateException();
        }
        long treeLookup = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            Integer stock = linked.get(target);
            if (stock == null) throw new IllegalStateException();
        }
        long linkedLookup = System.nanoTime() - start;

        start = System.nanoTime();
        String firstCodeOrder = tree.keySet().iterator().next();
        long sortedReport = System.nanoTime() - start;

        start = System.nanoTime();
        String firstInsertionOrder = linked.keySet().iterator().next();
        long insertionReport = System.nanoTime() - start;

        System.out.println("\nMedicines: " + count);
        System.out.println("100,000 HashMap lookups ns: " + hashLookup);
        System.out.println("100,000 TreeMap lookups ns: " + treeLookup);
        System.out.println("100,000 LinkedHashMap lookups ns: " + linkedLookup);
        System.out.println("First code-sorted medicine: " + firstCodeOrder
                + " (iterator start ns: " + sortedReport + ")");
        System.out.println("First inserted medicine: " + firstInsertionOrder
                + " (iterator start ns: " + insertionReport + ")");
        System.out.println("HashMap, TreeMap and LinkedHashMap sizes: "
                + Arrays.asList(hash.size(), tree.size(), linked.size()));
    }

    public static void main(String[] args) {
        run(100);
        run(1000);
        run(10000);
    }
}
