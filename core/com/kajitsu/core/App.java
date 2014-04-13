package kajitsu.core;

import kajitsu.core.io.HttpServer;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.logging.Logger;

/**
 * <p>Application instance provide methods to register stack based request handlers.</p>
 */
public class App {
    protected Logger LOGGER = Logger.getLogger(getClass().getName());

    private final Map<String, Supplier> factoryMap = new LinkedHashMap<>();

    private final List<Use> useList = new LinkedList<>();

    public App() {
        // Add default rule which will close the response/socket later.
        use((request, response, next) -> {
            next.run();
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void factory(String key, Supplier supplier) {
        factoryMap.put(key, supplier);
    }

    public <T> T get(String key) {
        return (T) factoryMap.get(key).get();
    }

    public void use(Use use) {
        useList.add(use);
    }

    public void listen(int port) {
        HttpServer httpServer = new HttpServer();
        httpServer.listen(port, (socket) -> {
            LOGGER.info("Process: " + socket);
            try {
                Request request = new Request(new BufferedInputStream(socket.getInputStream()));
                Response response = new Response(new BufferedOutputStream(socket.getOutputStream()));

                process(request, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void process(Request request, Response response) {
        process(request, response, 0);
    }

    public void process(Request request, Response response, int i) {
        LOGGER.entering("kajitsu.core.App", "process", i);

        Use use = useList.get(i);
        useList.get(i).use(request, response, () -> {
            if (i + 1 < useList.size()) {
                process(request, response, i + 1);
            }
        });

        LOGGER.exiting("kajitsu.core.App", "process");
    }
}
