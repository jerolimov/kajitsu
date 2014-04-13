package kajitsu.core;

/**
 * <p>Request processor which will be stacked.</p>
 *
 * <p>The next operation must be called!</p>
 */
@FunctionalInterface
public interface Use {
    void use(Request request, Response response, Runnable next);
}
