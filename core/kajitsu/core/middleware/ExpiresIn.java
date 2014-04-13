package kajitsu.core.middleware;

import kajitsu.core.Request;
import kajitsu.core.Response;
import kajitsu.core.Use;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;

/**
 * <p>Add HTTP <code>Expire</code> header with the configuration set
 * in with the constructor.</p>
 */
public class ExpiresIn implements Use {

    private long amountToAdd;
    private TemporalUnit unit;

    public ExpiresIn(long amountToAdd, TemporalUnit unit) {
        this.amountToAdd = amountToAdd;
        this.unit = unit;
    }

    @Override
    public void use(Request request, Response response, Runnable next) {
        LocalDateTime expires = LocalDateTime.now().plus(amountToAdd, unit);
        response.setHeader("Expire", expires.toString());
        next.run();
    }
}
