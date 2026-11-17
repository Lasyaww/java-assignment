import java.util.Comparator;
import java.util.PriorityQueue;

/** Q6: Emergency triage - severity 1 is most urgent; ties use arrival order. */
public class Triage {
    static final class Patient {
        final String name;
        final int severity;
        final long arrival;

        Patient(String name, int severity, long arrival) {
            if (severity < 1 || severity > 5) {
                throw new IllegalArgumentException("Severity must be from 1 to 5.");
            }
            this.name = name;
            this.severity = severity;
            this.arrival = arrival;
        }

        @Override
        public String toString() {
            return name + "(S" + severity + ")";
        }
    }

    private long counter = 0;
    private final PriorityQueue<Patient> queue = new PriorityQueue<>(
            Comparator.comparingInt((Patient p) -> p.severity)
                    .thenComparingLong(p -> p.arrival));

    void admit(String name, int severity) {
        queue.add(new Patient(name, severity, counter++));
    }

    Patient next() {
        return queue.poll();
    }

    public static void main(String[] args) {
        Triage er = new Triage();
        er.admit("Ravi", 3);
        er.admit("Meena", 1);
        er.admit("John", 3);
        er.admit("Asha", 5);
        er.admit("Kumar", 1);
        er.admit("Lata", 2);

        StringBuilder order = new StringBuilder();
        Patient patient;
        while ((patient = er.next()) != null) {
            if (order.length() > 0) order.append(' ');
            order.append(patient);
        }
        System.out.println(order);
    }
}
