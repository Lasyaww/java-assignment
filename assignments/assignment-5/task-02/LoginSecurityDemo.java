import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** B2 - Demonstrates unsafe SQL concatenation versus parameterized login. */
public class LoginSecurityDemo {
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(
                System.getenv().getOrDefault("JAVA_DB_URL", "jdbc:mysql://localhost:3306/college"),
                System.getenv().getOrDefault("JAVA_DB_USER", "root"),
                System.getenv().getOrDefault("JAVA_DB_PASSWORD", ""));
    }

    static boolean unsafeLogin(String id, String password) throws SQLException {
        String sql = "SELECT 1 FROM login_test WHERE user_id='" + id
                + "' AND user_password='" + password + "'";
        try (Connection c = connect(); Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next();
        }
    }

    static boolean safeLogin(String id, String password) throws SQLException {
        String sql = "SELECT 1 FROM login_test WHERE user_id=? AND user_password=?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public static void main(String[] args) throws SQLException {
        String id = args.length > 0 ? args[0] : "student";
        String password = args.length > 1 ? args[1] : "' OR '1'='1";
        System.out.println("Unsafe login accepted: " + unsafeLogin(id, password));
        System.out.println("PreparedStatement accepted: " + safeLogin(id, password));
    }
}
