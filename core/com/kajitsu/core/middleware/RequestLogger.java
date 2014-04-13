package kajitsu.core.middleware;

import kajitsu.core.Request;
import kajitsu.core.Response;
import kajitsu.core.Use;

import java.util.logging.Logger;

/**
 * <p>Really simple request logger.</p>
 *
 * TODO Add request path to the log output.
 */
public class RequestLogger implements Use {
    protected Logger LOGGER = Logger.getLogger(getClass().getName());

    @Override
    public void use(Request request, Response response, Runnable next) {
        long startTime = System.currentTimeMillis();
        next.run();
        LOGGER.info("Request proceeded in " + (System.currentTimeMillis() - startTime) + "ms.");
    }
}
