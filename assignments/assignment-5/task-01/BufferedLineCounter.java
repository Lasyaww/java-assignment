import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/** B1/A2 - Count line-feed bytes using a reusable byte buffer. */
public class BufferedLineCounter {
    static long countLines(Path path, int bufferSize) throws IOException {
        long lines = 0;
        try (InputStream in = new BufferedInputStream(Files.newInputStream(path), bufferSize)) {
            byte[] buffer = new byte[bufferSize];
            int count;
            while ((count = in.read(buffer)) != -1) {
                for (int i = 0; i < count; i++) if (buffer[i] == '\n') lines++;
            }
        }
        return lines;
    }

    public static void main(String[] args) throws IOException {
        Path file = Path.of(args.length == 0 ? "attendance.log" : args[0]);
        System.out.println("Lines: " + countLines(file, 8192));
    }
}
