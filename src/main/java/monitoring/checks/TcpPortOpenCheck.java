package monitoring.checks;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import monitoring.CheckFailedException;

public class TcpPortOpenCheck extends AbstractCheck {

    private final String host;
    private final int port;

    public TcpPortOpenCheck(final String host, final int port, final int timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    @Override
    public void check() throws CheckFailedException {
        this.lastRan = System.currentTimeMillis();
        Socket socket = null;
        try {
            final SocketAddress sockaddr = new InetSocketAddress(this.host, this.port);
            socket = new Socket();
            socket.connect(sockaddr, 1000 * this.timeout);
            markUp();
        } catch (final UnknownHostException e) {
            markDown(e);
        } catch (final IOException e) {
            markDown(e);
        } catch (final RuntimeException e) {
            markDown(e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
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
