/** Task 2 - Data types and jagged-array representation of floors and slots. */
public class ParkingArrayDemo {
    public static void main(String[] args) {
        // Each row represents a floor; rows can have different lengths.
        String[][] slots = new String[3][];
        slots[0] = new String[20];
        slots[1] = new String[15];
        slots[2] = new String[25];

        int slotNumber = 12;             // whole-number identifier
        String registrationNumber = "TN01AB1234";
        long entryTimeMillis = System.currentTimeMillis();
        double feePerHour = 30.0;
        slots[1][slotNumber] = registrationNumber;

        int parkedMinutes = 95;
        int billableHours = (parkedMinutes + 59) / 60;
        double fee = billableHours * feePerHour;

        // Integer division would truncate 95 / 60 to 1; ceiling arithmetic gives 2.
        System.out.println("Vehicle: " + slots[1][slotNumber]);
        System.out.println("Entry timestamp: " + entryTimeMillis);
        System.out.println("Billable hours: " + billableHours);
        System.out.println("Parking fee: Rs " + fee);
    }
}
