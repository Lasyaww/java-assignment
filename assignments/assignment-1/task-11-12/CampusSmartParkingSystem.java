import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Tasks 11 and 12 - Integrated campus smart-parking implementation.
 * Supports jagged floor layouts, vehicle-specific pricing, EV charging,
 * student/staff monthly passes, visitor day passes and delivery-van pricing.
 */
public class CampusSmartParkingSystem {
    enum UserType { STUDENT, STAFF, VISITOR }
    enum VehicleType { TWO_WHEELER, CAR, ELECTRIC_CAR, E_BIKE, DELIVERY_VAN }

    static final class ParkingSlot {
        private final int floor;
        private final int number;
        private String registrationNumber;

        ParkingSlot(int floor, int number) {
            this.floor = floor;
            this.number = number;
        }

        boolean isFree() {
            return registrationNumber == null;
        }

        void occupy(String registrationNumber) {
            if (!isFree()) throw new IllegalStateException("Slot is occupied.");
            this.registrationNumber = registrationNumber;
        }

        void release() {
            registrationNumber = null;
        }

        int getFloor() { return floor; }
        int getNumber() { return number; }
    }

    static final class ParkingPass {
        private final UserType userType;
        private final LocalDate validUntil;
        private final boolean oneDayPass;

        private ParkingPass(UserType userType, LocalDate validUntil,
                           boolean oneDayPass) {
            this.userType = userType;
            this.validUntil = validUntil;
            this.oneDayPass = oneDayPass;
        }

        static ParkingPass monthly(UserType type, LocalDate validUntil) {
            if (type == UserType.VISITOR) {
                throw new IllegalArgumentException(
                        "Monthly pass is only for students or staff.");
            }
            return new ParkingPass(type, validUntil, false);
        }

        static ParkingPass visitorDayPass(LocalDate date) {
            return new ParkingPass(UserType.VISITOR, date, true);
        }

        boolean isValid(LocalDate date) {
            return !date.isAfter(validUntil);
        }

        boolean isFreeParkingPass(LocalDate date) {
            return !oneDayPass && userType != UserType.VISITOR
                    && isValid(date);
        }

        boolean isVisitorDayPass(LocalDate date) {
            return oneDayPass && userType == UserType.VISITOR
                    && isValid(date);
        }
    }

    static final class Vehicle {
        private final String registrationNumber;
        private final VehicleType type;

        Vehicle(String registrationNumber, VehicleType type) {
            if (registrationNumber == null
                    || registrationNumber.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Registration number is required.");
            }
            if (type == null) throw new IllegalArgumentException("Vehicle type is required.");
            this.registrationNumber = registrationNumber.trim().toUpperCase();
            this.type = type;
        }

        String getRegistrationNumber() { return registrationNumber; }
        VehicleType getType() { return type; }
    }

    static final class ParkingTicket {
        private static long nextTicketNumber = 1000;
        private final long ticketNumber;
        private final Vehicle vehicle;
        private final ParkingSlot slot;
        private final UserType userType;
        private final ParkingPass pass;
        private final LocalDateTime entryTime;

        ParkingTicket(Vehicle vehicle, ParkingSlot slot, UserType userType,
                      ParkingPass pass, LocalDateTime entryTime) {
            this.ticketNumber = ++nextTicketNumber;
            this.vehicle = vehicle;
            this.slot = slot;
            this.userType = userType;
            this.pass = pass;
            this.entryTime = entryTime;
        }

        long getTicketNumber() { return ticketNumber; }
        Vehicle getVehicle() { return vehicle; }
        ParkingSlot getSlot() { return slot; }
        LocalDateTime getEntryTime() { return entryTime; }
        UserType getUserType() { return userType; }
        ParkingPass getPass() { return pass; }
    }

    private final ParkingSlot[][] floors;
    private final Map<String, ParkingTicket> activeTickets = new HashMap<>();

    CampusSmartParkingSystem(int[] slotsPerFloor) {
        if (slotsPerFloor == null) {
            throw new IllegalArgumentException("Floor configuration is required.");
        }
        floors = new ParkingSlot[slotsPerFloor.length][];
        for (int floor = 0; floor < slotsPerFloor.length; floor++) {
            if (slotsPerFloor[floor] < 0) {
                throw new IllegalArgumentException("Slot count cannot be negative.");
            }
            floors[floor] = new ParkingSlot[slotsPerFloor[floor]];
            for (int slot = 0; slot < slotsPerFloor[floor]; slot++) {
                floors[floor][slot] = new ParkingSlot(floor, slot);
            }
        }
    }

    public ParkingTicket enter(Vehicle vehicle, UserType userType,
                               ParkingPass pass, LocalDateTime entryTime) {
        if (vehicle == null || userType == null || entryTime == null) {
            throw new IllegalArgumentException("Entry details cannot be null.");
        }
        String regNo = vehicle.getRegistrationNumber();
        if (activeTickets.containsKey(regNo)) {
            throw new IllegalArgumentException("Vehicle is already parked.");
        }

        ParkingSlot slot = findFreeSlot(vehicle.getType());
        if (slot == null) {
            throw new IllegalStateException("No suitable parking slot is available.");
        }

        slot.occupy(regNo);
        ParkingTicket ticket = new ParkingTicket(
                vehicle, slot, userType, pass, entryTime);
        activeTickets.put(regNo, ticket);
        return ticket;
    }

    public double exit(String registrationNumber, LocalDateTime exitTime,
                       int chargingKWh) {
        if (registrationNumber == null || exitTime == null) {
            throw new IllegalArgumentException("Exit details cannot be null.");
        }
        String key = registrationNumber.trim().toUpperCase();
        ParkingTicket ticket = activeTickets.get(key);
        if (ticket == null) {
            throw new IllegalArgumentException("No active ticket for vehicle.");
        }
        if (exitTime.isBefore(ticket.getEntryTime())) {
            throw new IllegalArgumentException("Exit time precedes entry time.");
        }
        if (chargingKWh < 0) {
            throw new IllegalArgumentException("Charging units cannot be negative.");
        }

        double fee = calculateParkingFee(ticket, exitTime);
        if (ticket.getVehicle().getType() == VehicleType.ELECTRIC_CAR) {
            fee += chargingKWh * 12.0;
        }

        ticket.getSlot().release();
        activeTickets.remove(key);
        return fee;
    }

    private ParkingSlot findFreeSlot(VehicleType type) {
        for (ParkingSlot[] floor : floors) {
            for (ParkingSlot slot : floor) {
                if (slot.isFree()) return slot;
            }
        }
        return null;
    }

    private double calculateParkingFee(ParkingTicket ticket,
                                       LocalDateTime exitTime) {
        LocalDate entryDate = ticket.getEntryTime().toLocalDate();
        LocalDate exitDate = exitTime.toLocalDate();
        ParkingPass pass = ticket.getPass();

        if (pass != null && pass.isFreeParkingPass(exitDate)) {
            return 0.0;
        }
        if (pass != null && pass.isVisitorDayPass(entryDate)
                && pass.isVisitorDayPass(exitDate)) {
            return 0.0;
        }

        long minutes = Math.max(0,
                Duration.between(ticket.getEntryTime(), exitTime).toMinutes());
        long hours = (minutes + 59) / 60;
        VehicleType type = ticket.getVehicle().getType();

        switch (type) {
            case TWO_WHEELER:
                return hours * 10.0;
            case CAR:
            case ELECTRIC_CAR:
                return hours * 30.0;
            case E_BIKE:
                return 20.0;
            case DELIVERY_VAN:
                return deliveryVanFee(ticket.getEntryTime(), exitTime);
            default:
                throw new IllegalStateException("Unsupported vehicle type.");
        }
    }

    private double deliveryVanFee(LocalDateTime entry, LocalDateTime exit) {
        double total = 0.0;
        LocalDateTime cursor = entry;
        while (cursor.isBefore(exit)) {
            LocalDateTime nextHour = cursor.plusMinutes(1);
            if (nextHour.isAfter(exit)) nextHour = exit;
            LocalTime time = cursor.toLocalTime();
            boolean peakWindow = !time.isBefore(LocalTime.of(9, 0))
                    && time.isBefore(LocalTime.of(17, 0));
            total += peakWindow ? 60.0 / 60.0 : 30.0 / 60.0;
            cursor = nextHour;
        }
        return Math.ceil(total);
    }

    public int getActiveVehicleCount() {
        return activeTickets.size();
    }

    public static void main(String[] args) {
        CampusSmartParkingSystem system =
                new CampusSmartParkingSystem(new int[]{3, 4, 2, 5, 3});

        LocalDate today = LocalDate.now();
        Vehicle studentBike = new Vehicle("TN01AB1111",
                VehicleType.TWO_WHEELER);
        ParkingPass monthlyPass = ParkingPass.monthly(
                UserType.STUDENT, today.plusDays(30));

        system.enter(studentBike, UserType.STUDENT, monthlyPass,
                today.atTime(9, 0));
        system.exit("TN01AB1111", today.atTime(11, 15), 0);

        Vehicle visitorCar = new Vehicle("TN02CD2222", VehicleType.CAR);
        system.enter(visitorCar, UserType.VISITOR, null,
                today.atTime(10, 0));
        system.exit("TN02CD2222", today.atTime(12, 10), 0);

        Vehicle electricCar = new Vehicle("TN03EF3333",
                VehicleType.ELECTRIC_CAR);
        system.enter(electricCar, UserType.VISITOR, null,
                today.atTime(13, 0));
        system.exit("TN03EF3333", today.atTime(14, 30), 10);
    }
}
