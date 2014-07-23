import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class JettyClient {

    private final CountDownLatch closeLatch;
    private int id;

    @SuppressWarnings("unused")
    private Session session;

    public JettyClient(int id) {
        this.id = id;
        this.closeLatch = new CountDownLatch(1);
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        onConnected(this.session);

        // TODO: move this to some thread and send futures async
        try {
            Future<Void> fut;
            fut = session.getRemote().sendStringByFuture("Random int from id: " + id + " " + Util.randomInt());
            fut.get(2, TimeUnit.SECONDS);
            session.close(StatusCode.NORMAL, "I'm done");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        printId();
        onConnectionClose(statusCode, reason);

        this.session = null;
        this.closeLatch.countDown();
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        printId();
        onReceiveMessage(message);
    }

    private void onWait() {
        System.out.println("waiting for connection of device with id: " + id);
    }

    private void printId() {
        System.out.println("ID: " + id);
    }

    private void onConnectionClose(int statusCode, String reason) {
        System.out.printf("Connection closed. Code: " + statusCode + " reason: " + reason);
    }

    private void onReceiveMessage(String message) {
        System.out.println("Got message: " + message);
    }

    private void onConnected(Session session) {
        System.out.println(session);
    }
}

