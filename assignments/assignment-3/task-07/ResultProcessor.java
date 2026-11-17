import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/** Q7 - Concurrent fault-tolerant file processor using Callable, Future and ExecutorService. */
public class ResultProcessor {
    static final class Outcome {
        final String file;
        final boolean success;
        final String message;
        Outcome(String file, boolean success, String message) {
            this.file = file; this.success = success; this.message = message;
        }
    }

    static Callable<Outcome> task(Path file) {
        return () -> {
            try {
                if (!Files.exists(file)) throw new IOException("File is missing");
                String text = Files.readString(file).trim();
                if (text.isEmpty()) throw new IOException("File is empty or corrupt");
                int score = Integer.parseInt(text);
                if (score < 0 || score > 100) throw new IOException("Score outside 0-100");
                return new Outcome(file.toString(), true, "Result=" + score);
            } catch (Exception e) {
                return new Outcome(file.toString(), false, e.getMessage());
            }
        };
    }

    static void process(List<Path> files, int poolSize) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        ConcurrentLinkedQueue<Outcome> outcomes = new ConcurrentLinkedQueue<>();
        try {
            List<Future<Outcome>> futures = new ArrayList<>();
            for (Path file : files) futures.add(executor.submit(task(file)));
            for (Future<Outcome> future : futures) outcomes.add(future.get());
        } finally {
            executor.shutdown();
        }

        int successes = 0, failures = 0;
        for (Outcome outcome : outcomes) {
            if (outcome.success) successes++;
            else failures++;
        }
        System.out.println("Threads: " + poolSize + ", successes: "
                + successes + ", failures: " + failures);
    }

    public static void main(String[] args) throws Exception {
        List<Path> files = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Path file = Path.of("score" + i + ".txt");
            if (i % 4 == 0) Files.writeString(file, "corrupt");
            else if (i % 5 != 0) Files.writeString(file, String.valueOf(60 + i));
            Files.deleteIfExists(i % 5 == 0 ? file : Path.of("never-delete"));
            files.add(file);
        }
        for (int pool : new int[]{1, 2, 4, 8}) process(files, pool);
    }
}
