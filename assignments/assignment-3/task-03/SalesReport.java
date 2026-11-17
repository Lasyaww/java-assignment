import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/** Q3 - Manual resource closing versus try-with-resources. */
public class SalesReport {
    static void generateManual(List<Path> inputs, Path output) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(output);
        try {
            for (Path file : inputs) {
                BufferedReader reader = Files.newBufferedReader(file);
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                } finally {
                    reader.close();
                }
            }
        } finally {
            writer.close();
        }
    }

    static void generateSafe(List<Path> inputs, Path output) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            for (Path file : inputs) {
                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Path first = Path.of("store1.txt");
        Path second = Path.of("store2.txt");
        Path manual = Path.of("manual-report.txt");
        Path safe = Path.of("safe-report.txt");
        Files.write(first, Arrays.asList("Store 1: 1200", "Store 1: 800"));
        Files.write(second, Arrays.asList("Store 2: 1500"));
        List<Path> stores = Arrays.asList(first, second);
        generateManual(stores, manual);
        generateSafe(stores, safe);
        System.out.println("Manual report lines: " + Files.readAllLines(manual).size());
        System.out.println("Safe report lines: " + Files.readAllLines(safe).size());
    }
}
