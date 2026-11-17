import java.time.LocalDateTime;

/** Task 4 - Constructors, constructor chaining and object creation. */
public class ParkingTicketDemo {
    static class ParkingTicket {
        private final long ticketNumber;
        private final String registrationNumber;
        private final LocalDateTime entryTime;

        ParkingTicket(String registrationNumber) {
            this(0, registrationNumber, LocalDateTime.now());
        }

        ParkingTicket(long ticketNumber, String registrationNumber) {
            this(ticketNumber, registrationNumber, LocalDateTime.now());
        }

        ParkingTicket(long ticketNumber, String registrationNumber,
                      LocalDateTime entryTime) {
            if (registrationNumber == null || registrationNumber.trim().isEmpty()
                    || entryTime == null) {
                throw new IllegalArgumentException("Invalid ticket details.");
            }
            this.ticketNumber = ticketNumber;
            this.registrationNumber = registrationNumber;
            this.entryTime = entryTime;
        }

        ParkingTicket(ParkingTicket other) {
            this(other.ticketNumber, other.registrationNumber, other.entryTime);
        }

        void display() {
            System.out.println(ticketNumber + " | " + registrationNumber
                    + " | " + entryTime);
        }
    }

    public static void main(String[] args) {
        ParkingTicket first = new ParkingTicket(1001, "TN01AB1234");
        ParkingTicket copy = new ParkingTicket(first);
        first.display();
        copy.display();
    }
}
