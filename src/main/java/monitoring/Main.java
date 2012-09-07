package monitoring;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import monitoring.checks.DNSCheck;
import monitoring.checks.HttpCheck;
import monitoring.checks.TcpPortOpenCheck;

import org.joda.time.LocalDateTime;

public class Main {

    private static List<Monitor> monitors = new CopyOnWriteArrayList<Monitor>();

    public static void main(final String[] args) {
        /*
         * Some example monitors. Hopefully these hosts are high-volume enough not to be affected :)
         */

        monitors.add(new TcpPortOpenCheck("www.microsoft.com", 443, 1));

        monitors.add(new HttpCheck("www.google.com", 80, "/", 1));

        monitors.add(new DNSCheck("github.com", "A", new String[] { "207.97.227.239" }));

        runChecks();
    }

    private static void runChecks() {
        while (true) {
            for (final Monitor monitor : monitors) {
                try {
                    monitor.check();
                } catch (final CheckFailedException e) {
                    final StringBuilder message = new StringBuilder();
                    message.append(monitor);
                    message.append(" ");
                    message.append(e.getMessage());
                    message.append(" ");
                    message.append("Down since " + monitor.getLastSuccess());
                    System.err.println(new LocalDateTime() + " " + message.toString());
                }
            }
            try {
                Thread.sleep(15000);
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
