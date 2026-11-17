package parking.app;

import parking.model.ParkingVehicle;

/** Demonstrates access to a validated model through its public methods. */
public class EncapsulationDemo {
    public static void main(String[] args) {
        ParkingVehicle vehicle = new ParkingVehicle("tn01ab1234");
        vehicle.setParkedMinutes(90);
        System.out.println(vehicle.getRegistrationNumber());
        System.out.println(vehicle.getParkedMinutes());
    }
}
