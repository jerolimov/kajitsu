package kajitsu.examples;

import kajitsu.core.*;
import kajitsu.core.middleware.*;

import java.io.IOException;
import java.time.temporal.ChronoUnit;

/**
 * Created by christoph on 12.04.14.
 */
public class Example1 {
    public static void main(String... args) {

        App app = new App();

        app.use(new RequestLogger());
        app.use(new ExpiresIn(10, ChronoUnit.MINUTES));
        app.use(new CalculateContentLength());
        app.use(new Compress());

        app.use((request, response, next) -> {
            response.setHeader("Server", "Example1");
            response.setHeader("Content-Type", "text/plain");

            try {
                response.getOutputStream().write("Hallo\n".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            next.run();
        });

        app.listen(5000);
    }
}
