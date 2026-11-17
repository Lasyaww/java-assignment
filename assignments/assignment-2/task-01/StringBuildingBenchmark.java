import java.util.StringJoiner;

/** Q1: Compare String concatenation, StringBuilder and StringBuffer. */
public class StringBuildingBenchmark {
    private static volatile int sink;

    interface Builder {
        String build(int fields);
    }

    static String buildWithString(int fields) {
        String result = "";
        for (int i = 0; i < fields; i++) result += "FIELD" + i + "|";
        return result;
    }

    static String buildWithStringBuilder(int fields) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < fields; i++) result.append("FIELD").append(i).append('|');
        return result.toString();
    }

    static String buildWithStringBuffer(int fields) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < fields; i++) result.append("FIELD").append(i).append('|');
        return result.toString();
    }

    static String buildWithStringJoiner(int fields) {
        StringJoiner joiner = new StringJoiner("|");
        for (int i = 0; i < fields; i++) joiner.add("FIELD" + i);
        return joiner.toString();
    }

    private static long measure(Builder builder, int fields, int repetitions) {
        long start = System.nanoTime();
        for (int i = 0; i < repetitions; i++) {
            String value = builder.build(fields);
            sink += value.length();
        }
        return (System.nanoTime() - start) / repetitions;
    }

    private static void runWorkload(String name, int fields, int repetitions) {
        System.out.println("\n" + name + " (fields=" + fields
                + ", repetitions=" + repetitions + ")");
        // Warm-up before measuring.
        for (int i = 0; i < 3; i++) {
            buildWithString(fields);
            buildWithStringBuilder(fields);
            buildWithStringBuffer(fields);
        }
        System.out.println("String average ns: "
                + measure(StringBuildingBenchmark::buildWithString, fields, repetitions));
        System.out.println("StringBuilder average ns: "
                + measure(StringBuildingBenchmark::buildWithStringBuilder, fields, repetitions));
        System.out.println("StringBuffer average ns: "
                + measure(StringBuildingBenchmark::buildWithStringBuffer, fields, repetitions));
        System.out.println("StringJoiner average ns: "
                + measure(StringBuildingBenchmark::buildWithStringJoiner, fields, repetitions));
    }

    public static void main(String[] args) {
        runWorkload("Short ticket", 6, 2000);
        runWorkload("Many fields", 50, 500);
        runWorkload("Large ticket", 500, 100);
        System.out.println("\nPrevented dead-code elimination: " + sink);
    }
}
