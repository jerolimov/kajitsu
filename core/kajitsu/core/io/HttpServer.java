package kajitsu.core.io;

import java.io.*;
import java.net.*;
import java.util.logging.*;
import java.util.function.Consumer;

/**
 * <p>A really simple AND incomplete http server!</p>
 *
 * <p>This prototype run each now connection in a new thread.</p>
 *
 * TODO Use NIO and a threadpool instead.
 */
public class HttpServer {
    protected Logger LOGGER = Logger.getLogger(getClass().getName());

    public void listen(int port, Consumer<Socket> socketConsumer) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            LOGGER.info("Listen: " + serverSocket);

            new Thread(() -> {
                try {
                    while (true) {
                        final Socket socket = serverSocket.accept();
                        new Thread(() -> {
                            try {
                                socketConsumer.accept(socket);
                            } catch (Exception e) {
                                LOGGER.log(Level.WARNING, "Error while processing socket: " + e, e);
                            }
                            try {
                                if (!socket.isClosed()) {
                                    socket.close();
                                }
                            } catch (IOException e) {
                                LOGGER.log(Level.WARNING, "Error while closing socket: " + e, e);
                            }
                        }).start();
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error while accepting server socket: " + e, e);
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
