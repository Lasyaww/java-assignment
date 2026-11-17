import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** Q3: Demonstrates why equals and hashCode must use the same identity fields. */
public class Attendance {
    static final class Student {
        private final String regNo;
        private final String name;

        Student(String regNo, String name) {
            this.regNo = Objects.requireNonNull(regNo);
            this.name = Objects.requireNonNull(name);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof Student)) return false;
            Student student = (Student) other;
            return regNo.equals(student.regNo);
        }

        @Override
        public int hashCode() {
            return regNo.hashCode();
        }

        @Override
        public String toString() {
            return regNo + " " + name;
        }
    }

    // Deliberately incorrect version: equals uses regNo but hashCode is inherited.
    static final class BrokenStudent {
        private final String regNo;
        private final String name;

        BrokenStudent(String regNo, String name) {
            this.regNo = regNo;
            this.name = name;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof BrokenStudent
                    && regNo.equals(((BrokenStudent) other).regNo);
        }
    }

    public static void main(String[] args) {
        Set<BrokenStudent> broken = new HashSet<>();
        broken.add(new BrokenStudent("192311001", "Arun"));
        broken.add(new BrokenStudent("192311002", "Divya"));
        broken.add(new BrokenStudent("192311001", "Arun"));
        System.out.println("Broken contract set size: " + broken.size());

        Set<Student> present = new HashSet<>();
        present.add(new Student("192311001", "Arun"));
        present.add(new Student("192311002", "Divya"));
        present.add(new Student("192311001", "Arun"));
        System.out.println("Corrected unique students: " + present.size());

        Student first = new Student("192311003", "Kavi");
        Student duplicate = new Student("192311003", "Kavi");
        System.out.println("Equal objects have same hash: "
                + (first.hashCode() == duplicate.hashCode()));
    }
}
