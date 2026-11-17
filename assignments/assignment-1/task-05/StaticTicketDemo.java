/** Task 5 - Static ticket counter shared by all ticket objects. */
public class StaticTicketDemo {
    static class ParkingTicket {
        private static long nextTicketNumber = 1000;
        private final long ticketNumber;
        private final String registrationNumber;

        ParkingTicket(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            this.ticketNumber = ++nextTicketNumber;
        }

        void display() {
            System.out.println("Ticket " + ticketNumber + ": " + registrationNumber);
        }

        static long getTicketsIssued() {
            return nextTicketNumber - 1000;
        }
    }

    public static void main(String[] args) {
        ParkingTicket northGate = new ParkingTicket("TN01AA1111");
        ParkingTicket southGate = new ParkingTicket("TN02BB2222");
        northGate.display();
        southGate.display();
        System.out.println("Tickets issued: " + ParkingTicket.getTicketsIssued());
    }
}
