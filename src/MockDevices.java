import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class MockDevices {
    private URI uri;

    private ClientUpgradeRequest request = new ClientUpgradeRequest();

    private WebSocketClient client;// = new WebSocketClient();

    public MockDevices() {
        client = new WebSocketClient();
        try {
            uri = new URI(Constants.URI);
            client.start();
        } catch (Exception e) {

        }
        thread1.run();
        thread2.run();
    }

    Thread thread1 = new Thread() {
        @Override
        public void run() {
            startNewClient(1);
        }
    };

    Thread thread2 = new Thread() {
        @Override
        public void run() {
            startNewClient(2);
        }
    };

    private void startNewClient(int id) {
        JettyClient socket = new JettyClient(id);
        try {
            System.out.println("Connecting to: " + uri);
            client.connect(socket, uri, request);

            socket.awaitClose(5, TimeUnit.SECONDS);
        } catch (Throwable t) {
            t.printStackTrace();
        } /*finally {
            try {
                System.out.println("client Stop");
                client.stop();
            } catch (Exception e) {
                System.out.println("exception: " + e);
                e.printStackTrace();
            }
        }*/
    }
}
