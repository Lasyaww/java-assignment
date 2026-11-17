import java.util.Arrays;

/**
 * Task 3 - Design the control logic.
 * A jagged array represents floors that may have different slot counts.
 */
public class ParkingFacility {
    // slots[floor][slot] contains a vehicle number, or null when free.
    private final String[][] slots;

    public ParkingFacility(int[] slotsPerFloor) {
        if (slotsPerFloor == null) {
            throw new IllegalArgumentException("Floor configuration cannot be null.");
        }

        slots = new String[slotsPerFloor.length][];
        for (int floor = 0; floor < slotsPerFloor.length; floor++) {
            if (slotsPerFloor[floor] < 0) {
                throw new IllegalArgumentException("Slot count cannot be negative.");
            }
            slots[floor] = new String[slotsPerFloor[floor]];
        }
    }

    /** Returns {floor, slot} for the first free slot, or null when full. */
    public int[] allocate(String vehicleNo) {
        if (vehicleNo == null || vehicleNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle number is required.");
        }

        for (int floor = 0; floor < slots.length; floor++) {
            for (int slot = 0; slot < slots[floor].length; slot++) {
                if (slots[floor][slot] == null) {
                    slots[floor][slot] = vehicleNo;
                    return new int[]{floor, slot};
                }
            }
        }
        return null;
    }

    /** Frees a slot. Returns false if its position is invalid or already free. */
    public boolean release(int floor, int slot) {
        if (floor < 0 || floor >= slots.length) {
            return false;
        }
        if (slot < 0 || slot >= slots[floor].length) {
            return false;
        }
        if (slots[floor][slot] == null) {
            return false;
        }

        slots[floor][slot] = null;
        return true;
    }

    public static void main(String[] args) {
        ParkingFacility facility = new ParkingFacility(new int[]{2, 1});
        System.out.println(Arrays.toString(facility.allocate("TN01AB1111")));
        System.out.println(Arrays.toString(facility.allocate("TN02CD2222")));
        System.out.println(Arrays.toString(facility.allocate("TN03EF3333")));
        System.out.println(Arrays.toString(facility.allocate("TN04GH4444")));
        System.out.println(facility.release(0, 1) + " "
                + facility.release(0, 1) + " " + facility.release(5, 0));
        System.out.println(Arrays.toString(facility.allocate("TN04GH4444")));
    }
}
