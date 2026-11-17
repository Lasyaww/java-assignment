import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/** Task 1 - UI delegates validation and payment to FeeService. */
public class FeeWindow extends Frame {
    private final TextField regNo = new TextField(18);
    private final TextField amount = new TextField(12);
    private final Label status = new Label("Enter fee details.");
    private final FeeService service = new FeeService();

    public FeeWindow() {
        super("Student Fee Payment");
        setLayout(new BorderLayout(8, 8));
        Panel fields = new Panel(new GridLayout(2, 2, 6, 6));
        fields.add(new Label("Register number")); fields.add(regNo);
        fields.add(new Label("Amount (INR)")); fields.add(amount);
        Panel actions = new Panel(new FlowLayout());
        Button pay = new Button("Pay");
        Button clear = new Button("Clear");
        Button receipt = new Button("Receipt");
        actions.add(pay); actions.add(clear); actions.add(receipt);
        add(fields, BorderLayout.NORTH);
        add(actions, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
        pay.addActionListener(e -> pay());
        clear.addActionListener(e -> { regNo.setText(""); amount.setText(""); status.setText("Cleared."); });
        receipt.addActionListener(e -> status.setText(service.lastReceipt()));
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        pack();
    }

    private void pay() {
        try {
            double value = Double.parseDouble(amount.getText().trim());
            status.setText(service.pay(regNo.getText().trim(), value));
        } catch (NumberFormatException ex) {
            status.setText("Enter a valid amount.");
        } catch (SQLException ex) {
            status.setText("Payment could not be saved.");
        }
    }

    public static void main(String[] args) { EventQueue.invokeLater(() -> new FeeWindow().setVisible(true)); }
}

class FeeService {
    private String receipt = "No payment made.";
    String pay(String regNo, double amount) throws SQLException {
        if (regNo == null || regNo.isEmpty() || amount <= 0)
            throw new IllegalArgumentException("Register number and positive amount are required.");
        // Configure JDBC URL/user/password for your local database.
        String url = System.getenv().getOrDefault("JAVA_DB_URL", "jdbc:mysql://localhost:3306/college");
        String user = System.getenv().getOrDefault("JAVA_DB_USER", "root");
        String password = System.getenv().getOrDefault("JAVA_DB_PASSWORD", "");
        try (Connection c = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO fees(reg_no, amount) VALUES (?, ?)")) {
            ps.setString(1, regNo);
            ps.setDouble(2, amount);
            ps.executeUpdate();
        }
        receipt = "Receipt: " + regNo + " | Rs " + amount;
        return "Payment saved.";
    }
    String lastReceipt() { return receipt; }
}
