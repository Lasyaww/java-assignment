import java.time.LocalDateTime;

/** Task 1 - Candidate objects and their responsibilities in the parking model. */
public class ParkingModel {
    enum VehicleType { TWO_WHEELER, CAR, ELECTRIC_CAR }
    enum UserType { STUDENT, STAFF, VISITOR }

    static class Vehicle {
        private final String registrationNumber;
        private final VehicleType type;

        Vehicle(String registrationNumber, VehicleType type) {
            if (registrationNumber == null || registrationNumber.trim().isEmpty()
                    || type == null) {
                throw new IllegalArgumentException("Valid vehicle details are required.");
            }
            this.registrationNumber = registrationNumber.trim().toUpperCase();
            this.type = type;
        }

        String getRegistrationNumber() { return registrationNumber; }
        VehicleType getType() { return type; }
    }

    static class ParkingSlot {
        private final int floorNumber;
        private final int slotNumber;
        private Vehicle parkedVehicle;

        ParkingSlot(int floorNumber, int slotNumber) {
            this.floorNumber = floorNumber;
            this.slotNumber = slotNumber;
        }

        boolean isAvailable() { return parkedVehicle == null; }

        void park(Vehicle vehicle) {
            if (!isAvailable()) throw new IllegalStateException("Slot is occupied.");
            parkedVehicle = vehicle;
        }

        Vehicle removeVehicle() {
            Vehicle removed = parkedVehicle;
            parkedVehicle = null;
            return removed;
        }
    }

    static class ParkingTicket {
        private static long nextNumber = 1000;
        private final long ticketNumber = ++nextNumber;
        private final Vehicle vehicle;
        private final LocalDateTime entryTime;

        ParkingTicket(Vehicle vehicle, LocalDateTime entryTime) {
            this.vehicle = vehicle;
            this.entryTime = entryTime;
        }

        long getTicketNumber() { return ticketNumber; }
        Vehicle getVehicle() { return vehicle; }
        LocalDateTime getEntryTime() { return entryTime; }
    }

    public static void main(String[] args) {
        Vehicle vehicle = new Vehicle("TN01AB1234", VehicleType.CAR);
        ParkingSlot slot = new ParkingSlot(0, 1);
        slot.park(vehicle);
        ParkingTicket ticket = new ParkingTicket(vehicle, LocalDateTime.now());

        System.out.println("Ticket: " + ticket.getTicketNumber());
        System.out.println("Vehicle: " + ticket.getVehicle().getRegistrationNumber());
        System.out.println("Slot available: " + slot.isAvailable());
    }
}
