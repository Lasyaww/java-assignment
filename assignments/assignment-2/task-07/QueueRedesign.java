import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Q7: Day-4 redesign.
 * Original design repeatedly sorts a complete waiting list before every service.
 * Revised design maintains the next patient in a PriorityQueue.
 */
public class QueueRedesign {
    static final class Patient {
        final String name;
        final int severity;
        final long arrival;

        Patient(String name, int severity, long arrival) {
            this.name = name;
            this.severity = severity;
            this.arrival = arrival;
        }
    }

    private static Patient[] createPatients(int count) {
        Patient[] patients = new Patient[count];
        for (int i = 0; i < count; i++) {
            patients[i] = new Patient("Patient" + i,
                    (i % 5) + 1, i);
        }
        return patients;
    }

    private static long originalRepeatedSort(Patient[] patients) {
        Queue<Patient> waiting = new ArrayDeque<>();
        for (Patient patient : patients) waiting.add(patient);
        long start = System.nanoTime();
        while (!waiting.isEmpty()) {
            Patient[] copy = waiting.toArray(new Patient[0]);
            java.util.Arrays.sort(copy,
                    java.util.Comparator.comparingInt((Patient p) -> p.severity)
                            .thenComparingLong(p -> p.arrival));
            waiting.clear();
            for (int i = 1; i < copy.length; i++) waiting.add(copy[i]);
        }
        return System.nanoTime() - start;
    }

    private static long redesignedPriorityQueue(Patient[] patients) {
        PriorityQueue<Patient> waiting = new PriorityQueue<>(
                java.util.Comparator.comparingInt((Patient p) -> p.severity)
                        .thenComparingLong(p -> p.arrival));
        long start = System.nanoTime();
        for (Patient patient : patients) waiting.add(patient);
        while (!waiting.isEmpty()) waiting.poll();
        return System.nanoTime() - start;
    }

    public static void main(String[] args) {
        int[] sizes = {100, 500, 1000};
        for (int size : sizes) {
            Patient[] data = createPatients(size);
            // The same generated data is used by both approaches.
            System.out.println("Patients: " + size);
            System.out.println("Repeated full sort ns: "
                    + originalRepeatedSort(data));
            System.out.println("PriorityQueue ns: "
                    + redesignedPriorityQueue(data));
        }
    }
}
