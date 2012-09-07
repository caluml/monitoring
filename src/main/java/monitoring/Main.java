package monitoring;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {

    private static List<Monitor> monitors = new CopyOnWriteArrayList<Monitor>();

    /**
     * @param args
     */
    public static void main(String[] args) {
        // create monitors here
        runChecks();
    }

    private static void runChecks() {
        while (true) {
            for (Monitor monitor : monitors) {
                try {
                    monitor.check();
                } catch (CheckFailedException e) {
                    System.err.println(e.getMessage());
                    System.err.println("Down since " + monitor.getLastSuccess());
                }
            }
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
