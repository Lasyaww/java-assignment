/** Task 6 - Vehicle inheritance hierarchy and constructor chaining. */
public class InheritanceDemo {
    static class Vehicle {
        protected final String registrationNumber;

        Vehicle(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            System.out.println("Vehicle constructor");
        }

        double ratePerHour() { return 0; }
    }

    static class Car extends Vehicle {
        Car(String registrationNumber) {
            super(registrationNumber);
            System.out.println("Car constructor");
        }

        @Override
        double ratePerHour() { return 30; }
    }

    static class ElectricCar extends Car {
        ElectricCar(String registrationNumber) {
            super(registrationNumber);
            System.out.println("ElectricCar constructor");
        }
    }

    public static void main(String[] args) {
        ElectricCar vehicle = new ElectricCar("TN01EV1234");
        System.out.println("Rate per hour: Rs " + vehicle.ratePerHour());
    }
}
