package monitoring.checks;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import monitoring.CheckFailedException;

public class TcpPortOpenCheck extends AbstractCheck {

    private final String host;
    private final int port;

    public TcpPortOpenCheck(String host, int port, int timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    @Override
    public void check() throws CheckFailedException {
        this.lastRan = System.currentTimeMillis();
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getByName(this.host), this.port);
            socket.setSoTimeout(1000 * this.timeout);
            markUp();
        } catch (UnknownHostException e) {
            markDown(e);
        } catch (IOException e) {
            markDown(e);
        } catch (RuntimeException e) {
            markDown(e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TcpOpenCheck [host=");
        builder.append(this.host);
        builder.append(", port=");
        builder.append(this.port);
        builder.append(", timeout=");
        builder.append(this.timeout);
        builder.append("]");
        return builder.toString();
    }
}
