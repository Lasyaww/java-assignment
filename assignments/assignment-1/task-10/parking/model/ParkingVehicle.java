package parking.model;

/** Task 10 - Encapsulated vehicle model in the parking.model package. */
public class ParkingVehicle {
    private final String registrationNumber;
    private int parkedMinutes;

    public ParkingVehicle(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number is required.");
        }
        this.registrationNumber = registrationNumber.trim().toUpperCase();
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public int getParkedMinutes() {
        return parkedMinutes;
    }

    public void setParkedMinutes(int parkedMinutes) {
        if (parkedMinutes < 0) {
            throw new IllegalArgumentException("Parked minutes cannot be negative.");
        }
        this.parkedMinutes = parkedMinutes;
    }
}
