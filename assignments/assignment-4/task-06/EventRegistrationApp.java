import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/** Task 6 - Layered Swing/JDBC event registration with async loading and duplicate prevention. */
public class EventRegistrationApp extends JFrame {
    private final JTextField studentId = new JTextField(10);
    private final JTextField studentName = new JTextField(12);
    private final JComboBox<String> event = new JComboBox<>(new String[]{"TechFest", "CulturalNight", "SportsMeet"});
    private final JComboBox<String> filter = new JComboBox<>(new String[]{"All", "TechFest", "CulturalNight", "SportsMeet"});
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Name", "Event"}, 0);
    private final JTable table = new JTable(model);
    private final RegistrationService service = new RegistrationService();

    public EventRegistrationApp() {
        super("Campus Event Registration");
        setLayout(new BorderLayout());
        JPanel form = new JPanel();
        form.add(new JLabel("Student ID")); form.add(studentId);
        form.add(new JLabel("Name")); form.add(studentName);
        form.add(event);
        JButton register = new JButton("Register");
        JButton applyFilter = new JButton("Filter");
        form.add(register); form.add(filter); form.add(applyFilter);
        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem refresh = new JMenuItem("Refresh");
        JMenuItem exit = new JMenuItem("Exit");
        file.add(refresh); file.add(exit); menu.add(file); setJMenuBar(menu);
        add(form, BorderLayout.NORTH); add(new JScrollPane(table), BorderLayout.CENTER);
        register.addActionListener(e -> registerAsync());
        applyFilter.addActionListener(e -> loadAsync((String) filter.getSelectedItem()));
        refresh.addActionListener(e -> loadAsync("All"));
        exit.addActionListener(e -> dispose());
        loadAsync("All");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(760, 420);
    }

    private void registerAsync() {
        String id = studentId.getText().trim(), name = studentName.getText().trim();
        String selectedEvent = (String) event.getSelectedItem();
        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student ID and name are required."); return;
        }
        new SwingWorker<Void, Void>() {
            protected Void doInBackground() throws Exception {
                service.register(id, name, selectedEvent); return null;
            }
            protected void done() {
                try { get(); loadAsync("All"); JOptionPane.showMessageDialog(EventRegistrationApp.this, "Registration saved."); }
                catch (Exception ex) { JOptionPane.showMessageDialog(EventRegistrationApp.this,
                        ex.getCause() == null ? ex.getMessage() : ex.getCause().getMessage(),
                        "Registration failed", JOptionPane.ERROR_MESSAGE); }
            }
        }.execute();
    }

    private void loadAsync(String selected) {
        new SwingWorker<java.util.List<String[]>, Void>() {
            protected java.util.List<String[]> doInBackground() throws Exception { return service.find(selected); }
            protected void done() {
                try { model.setRowCount(0); for (String[] row : get()) model.addRow(row); }
                catch (Exception ex) { JOptionPane.showMessageDialog(EventRegistrationApp.this, "Load failed: " + ex.getMessage()); }
            }
        }.execute();
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new EventRegistrationApp().setVisible(true)); }
}

class RegistrationService {
    private final RegistrationDao dao = new RegistrationDao();
    void register(String id, String name, String event) throws SQLException {
        if (id.isEmpty() || name.isEmpty()) throw new IllegalArgumentException("Required fields missing.");
        dao.insert(id, name, event);
    }
    java.util.List<String[]> find(String event) throws SQLException { return dao.find(event); }
}

class RegistrationDao {
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(
                System.getenv().getOrDefault("JAVA_DB_URL", "jdbc:mysql://localhost:3306/college"),
                System.getenv().getOrDefault("JAVA_DB_USER", "root"),
                System.getenv().getOrDefault("JAVA_DB_PASSWORD", ""));
    }
    void insert(String id, String name, String event) throws SQLException {
        String sql = "INSERT INTO event_registration(student_id, student_name, event_name) VALUES (?, ?, ?)";
        try (Connection c = connect(); PreparedStatement check = c.prepareStatement(
                "SELECT 1 FROM event_registration WHERE student_id=? AND event_name=?")) {
            check.setString(1, id); check.setString(2, event);
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) throw new SQLException("Student is already registered for this event.");
            }
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, id); ps.setString(2, name); ps.setString(3, event); ps.executeUpdate();
            }
        }
    }
    java.util.List<String[]> find(String event) throws SQLException {
        java.util.List<String[]> rows = new java.util.ArrayList<>();
        String sql = "SELECT student_id, student_name, event_name FROM event_registration"
                + ("All".equals(event) ? "" : " WHERE event_name=?") + " ORDER BY student_id";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (!"All".equals(event)) ps.setString(1, event);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rows.add(new String[]{rs.getString(1), rs.getString(2), rs.getString(3)});
            }
        }
        return rows;
    }
}
