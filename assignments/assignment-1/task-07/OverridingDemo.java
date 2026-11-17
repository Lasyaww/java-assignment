/** Task 7 - Method overriding, final fields and Object method overrides. */
public class OverridingDemo {
    static class Vehicle {
        private final String registrationNumber;

        Vehicle(String registrationNumber) {
            this.registrationNumber = registrationNumber;
        }

        public double calculateFee(int hours) {
            return hours * 20.0;
        }

        @Override
        public String toString() {
            return "Vehicle[" + registrationNumber + "]";
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof Vehicle)) return false;
            Vehicle vehicle = (Vehicle) other;
            return registrationNumber.equals(vehicle.registrationNumber);
        }

        @Override
        public int hashCode() {
            return registrationNumber.hashCode();
        }
    }

    static class Car extends Vehicle {
        Car(String registrationNumber) {
            super(registrationNumber);
        }

        @Override
        public double calculateFee(int hours) {
            return hours * 30.0;
        }
    }

    public static void main(String[] args) {
        Vehicle vehicle = new Car("TN01AB1234");
        System.out.println(vehicle.calculateFee(2));
        System.out.println(vehicle);
        System.out.println(vehicle.equals(new Car("TN01AB1234")));
    }
}
