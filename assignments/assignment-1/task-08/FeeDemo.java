interface Chargeable {
    double chargingFee(int kWh);
}

abstract class Vehicle {
    private final String regNo;

    protected Vehicle(String regNo) {
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number is required.");
        }
        this.regNo = regNo;
    }

    public String getRegNo() {
        return regNo;
    }

    protected abstract double ratePerHour();

    /** Every started hour is charged in full. */
    public double calculateFee(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Minutes cannot be negative.");
        }
        int billableHours = (minutes + 59) / 60;
        return billableHours * ratePerHour();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Vehicle)) return false;
        Vehicle vehicle = (Vehicle) other;
        return regNo.equalsIgnoreCase(vehicle.regNo);
    }

    @Override
    public int hashCode() {
        return regNo.toUpperCase().hashCode();
    }
}

class TwoWheeler extends Vehicle {
    TwoWheeler(String regNo) {
        super(regNo);
    }

    @Override
    protected double ratePerHour() {
        return 10;
    }
}

class Car extends Vehicle {
    Car(String regNo) {
        super(regNo);
    }

    @Override
    protected double ratePerHour() {
        return 30;
    }
}

class ElectricCar extends Car implements Chargeable {
    ElectricCar(String regNo) {
        super(regNo);
    }

    @Override
    public double chargingFee(int kWh) {
        if (kWh < 0) {
            throw new IllegalArgumentException("Energy usage cannot be negative.");
        }
        return kWh * 12.0;
    }
}

public class FeeDemo {
    public static void main(String[] args) {
        Vehicle[] parked = {
            new TwoWheeler("TN01A1"),
            new Car("TN02B2"),
            new ElectricCar("TN03C3")
        };

        for (Vehicle vehicle : parked) {
            double fee = vehicle.calculateFee(130);
            if (vehicle instanceof Chargeable) {
                fee += ((Chargeable) vehicle).chargingFee(10);
            }
            System.out.println(vehicle.getClass().getSimpleName() + " "
                    + vehicle.getRegNo() + " -> Rs " + fee);
        }

        System.out.println(new Car("TN02B2")
                .equals(new ElectricCar("TN02B2")));
    }
}
