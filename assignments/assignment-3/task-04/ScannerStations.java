/** Q4 - Thread creation using Thread and Runnable, lifecycle methods. */
public class ScannerStations {
    static class Station extends Thread {
        private final int scripts;
        Station(String name, int scripts) { super(name); this.scripts = scripts; }
        @Override public void run() {
            try {
                Thread.sleep(100L * scripts);
                System.out.println(getName() + " scanned " + scripts);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class RunnableStation implements Runnable {
        private final String name;
        private final int scripts;
        RunnableStation(String name, int scripts) {
            this.name = name; this.scripts = scripts;
        }
        @Override public void run() {
            try {
                Thread.sleep(100L * scripts);
                System.out.println(name + " scanned " + scripts);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] stations = {
            new Station("Station-1", 2),
            new Thread(new RunnableStation("Station-2", 3)),
            new Station("Station-3", 1),
            new Thread(new RunnableStation("Station-4", 4))
        };
        int total = 0;
        for (Thread station : stations) {
            station.start();
            System.out.println(station.getName() + " alive: " + station.isAlive());
        }
        for (Thread station : stations) station.join();
        System.out.println("All scanner stations completed.");
    }
}
