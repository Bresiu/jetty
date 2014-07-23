import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class MockDevices {

    int i;

    public MockDevices() {
        init();
    }

    private void init() {
        for (i = 0; i < Constants.NUMER_OF_DEVICES; i++) {
            startNewClient(i);
        }
    }

    private void startNewClient(int id) {
        WebSocketClient client = new WebSocketClient();
        JettyClient socket = new JettyClient(id);
        try {
            client.start();
            URI uri = new URI(Constants.URI);
            ClientUpgradeRequest request = new ClientUpgradeRequest();

            System.out.println("Connecting to: " + uri);
            client.connect(socket, uri, request);

            socket.awaitClose(5, TimeUnit.SECONDS);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                System.out.println("client Stop");
                client.stop();
            } catch (Exception e) {
                System.out.println("exception: " + e);
                e.printStackTrace();
            }
        }
    }
}
