import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/** Task 2 - Event-driven outpatient appointment desk. */
public class AppointmentDesk extends Frame {
    private final TextField patientId = new TextField(14);
    private final Choice department = new Choice();
    private final Choice doctor = new Choice();
    private final Choice slot = new Choice();
    private final Label status = new Label("Complete the form.");
    private boolean bookingInProgress = false;
    private final Map<String, List<String>> doctors = new HashMap<>();

    public AppointmentDesk() {
        super("Outpatient Appointment Desk");
        doctors.put("Cardiology", Arrays.asList("Dr Rao", "Dr Mehta"));
        doctors.put("Dermatology", Arrays.asList("Dr Iyer", "Dr Das"));
        doctors.put("Orthopaedics", Arrays.asList("Dr Kumar", "Dr Shah"));
        setLayout(new BorderLayout(8, 8));
        Panel form = new Panel(new GridLayout(4, 2, 6, 6));
        department.add("Cardiology"); department.add("Dermatology"); department.add("Orthopaedics");
        for (String s : Arrays.asList("09:00", "10:00", "11:00", "14:00")) slot.add(s);
        form.add(new Label("Patient ID")); form.add(patientId);
        form.add(new Label("Department")); form.add(department);
        form.add(new Label("Doctor")); form.add(doctor);
        form.add(new Label("Time slot")); form.add(slot);
        Button book = new Button("Book appointment");
        add(form, BorderLayout.CENTER); add(book, BorderLayout.SOUTH); add(status, BorderLayout.NORTH);
        department.addItemListener(e -> refreshDoctors());
        book.addActionListener(e -> book());
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                if (bookingInProgress && new DialogChoice(AppointmentDesk.this).confirm()) return;
                dispose();
            }
        });
        refreshDoctors(); pack();
    }

    private void refreshDoctors() {
        doctor.removeAll();
        for (String name : doctors.get(department.getSelectedItem())) doctor.add(name);
    }

    private void book() {
        String id = patientId.getText().trim();
        if (id.isEmpty()) { status.setText("Patient ID is required."); return; }
        bookingInProgress = true;
        try {
            status.setText("Appointment confirmed: " + id + ", "
                    + department.getSelectedItem() + ", " + doctor.getSelectedItem()
                    + ", " + slot.getSelectedItem());
        } finally { bookingInProgress = false; }
    }

    static class DialogChoice {
        private final Frame owner;
        DialogChoice(Frame owner) { this.owner = owner; }
        boolean confirm() {
            return !java.awt.GraphicsEnvironment.isHeadless()
                    && javax.swing.JOptionPane.showConfirmDialog(owner,
                    "A booking is in progress. Close anyway?", "Confirm",
                    javax.swing.JOptionPane.YES_NO_OPTION) != javax.swing.JOptionPane.YES_OPTION;
        }
    }

    public static void main(String[] args) { EventQueue.invokeLater(() -> new AppointmentDesk().setVisible(true)); }
}
