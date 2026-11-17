/** Task 9 - Abstract class and interface for charging capability. */
public class AbstractionDemo {
    interface Chargeable {
        double chargingFee(int kWh);
    }

    static abstract class Vehicle {
        private final String registrationNumber;

        Vehicle(String registrationNumber) {
            this.registrationNumber = registrationNumber;
        }

        String getRegistrationNumber() { return registrationNumber; }
        abstract double ratePerHour();

        double parkingFee(int hours) {
            if (hours < 0) throw new IllegalArgumentException("Hours cannot be negative.");
            return hours * ratePerHour();
        }
    }

    static class ElectricCar extends Vehicle implements Chargeable {
        ElectricCar(String registrationNumber) {
            super(registrationNumber);
        }

        @Override
        double ratePerHour() { return 30; }

        @Override
        public double chargingFee(int kWh) {
            if (kWh < 0) throw new IllegalArgumentException("kWh cannot be negative.");
            return kWh * 12.0;
        }
    }

    public static void main(String[] args) {
        ElectricCar car = new ElectricCar("TN01EV1234");
        System.out.println("Parking fee: Rs " + car.parkingFee(2));
        System.out.println("Charging fee: Rs " + car.chargingFee(5));
    }
}
