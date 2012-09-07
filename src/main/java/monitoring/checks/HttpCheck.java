package monitoring.checks;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import monitoring.CheckFailedException;
import monitoring.State;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

public class HttpCheck extends AbstractCheck {

    private final String host;
    private final int port;
    private final String path;

    public HttpCheck(String host, int port, String path, int timeout) {
        this.host = host;
        this.port = port;
        this.path = path;
        this.timeout = timeout;
    }

    @Override
    public void check() throws CheckFailedException {
        try {
            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Integer.valueOf(1000 * this.timeout));
            //            HttpHost proxy = new HttpHost(proxyHost, port, "http");
            //            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            URIBuilder uriBuilder = new URIBuilder();
            URI uri = uriBuilder.setScheme("http").setHost(this.host).setPort(this.port).setPath(this.path).build();
            HttpUriRequest req = new HttpGet(uri);
            HttpResponse httpResponse = client.execute(req);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode > 399) {
                this.lastFailed = System.currentTimeMillis();
                this.state = State.DOWN;
                throw new CheckFailedException(uri + " returned a " + statusCode);
            }
        } catch (ClientProtocolException e) {
            markDown(e);
        } catch (IOException e) {
            markDown(e);
        } catch (URISyntaxException e) {
            markDown(e);
        } catch (RuntimeException e) {
            markDown(e);
        } finally {
            // cleanup
        }
    }
}
