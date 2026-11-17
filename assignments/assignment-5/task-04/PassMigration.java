import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** B4 - Migrate validated CSV pass rows in batches and inspect JDBC metadata. */
public class PassMigration {
    static void migrate(Path input, Connection connection, Path failures)
            throws IOException, SQLException {
        List<String[]> batch = new ArrayList<>();
        List<String> rejected = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(input)) {
            String line;
            int lineNo = 0;
            while ((line = reader.readLine()) != null) {
                lineNo++;
                String[] fields = line.split(",", -1);
                try {
                    if (fields.length != 5 || fields[0].isEmpty()
                            || fields[1].isEmpty() || fields[2].isEmpty()) {
                        throw new IllegalArgumentException("Invalid field count or empty value");
                    }
                    Integer.parseInt(fields[3]);
                    Double.parseDouble(fields[4]);
                    batch.add(fields);
                } catch (RuntimeException ex) {
                    rejected.add("Line " + lineNo + ": " + ex.getMessage() + " | " + line);
                }
            }
        }
        try {
            PassLoader.loadAll(connection, batch);
        } catch (SQLException ex) {
            rejected.add("Database migration failed: " + ex.getMessage());
            throw ex;
        } finally {
            Files.write(failures, rejected);
        }
        System.out.println("Valid records: " + batch.size());
        System.out.println("Rejected records: " + rejected.size());
    }

    static void printMetadata(Connection connection) throws SQLException {
        DatabaseMetaData md = connection.getMetaData();
        System.out.println("Database: " + md.getDatabaseProductName());
        System.out.println("Driver: " + md.getDriverName() + " " + md.getDriverVersion());
        try (java.sql.ResultSet tables = md.getTables(null, null, "%", new String[]{"TABLE"})) {
            while (tables.next()) System.out.println("Table: " + tables.getString("TABLE_NAME"));
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java PassMigration passes.csv");
            return;
        }
        try (Connection connection = DriverManager.getConnection(
                System.getenv().getOrDefault("JAVA_DB_URL", "jdbc:mysql://localhost:3306/college"),
                System.getenv().getOrDefault("JAVA_DB_USER", "root"),
                System.getenv().getOrDefault("JAVA_DB_PASSWORD", ""))) {
            printMetadata(connection);
            migrate(Path.of(args[0]), connection, Path.of("rejected-passes.txt"));
        }
    }
}
