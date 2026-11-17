import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Generates sample pass rows and measures transactional batch loading. */
public class PassLoaderDemo {
    public static void main(String[] args) throws SQLException {
        int count = args.length == 0 ? 1000 : Integer.parseInt(args[0]);
        List<String[]> passes = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            passes.add(new String[]{"P" + i, "REG" + i, "Student" + i,
                    String.valueOf((i % 20) + 1), "4500.0"});
        }
        try (Connection con = DriverManager.getConnection(
                System.getenv().getOrDefault("JAVA_DB_URL", "jdbc:mysql://localhost:3306/college"),
                System.getenv().getOrDefault("JAVA_DB_USER", "root"),
                System.getenv().getOrDefault("JAVA_DB_PASSWORD", ""))) {
            long start = System.nanoTime();
            int loaded = PassLoader.loadAll(con, passes);
            System.out.println("Loaded rows: " + loaded);
            System.out.println("Elapsed ms: " + (System.nanoTime() - start) / 1_000_000.0);
        }
    }
}
