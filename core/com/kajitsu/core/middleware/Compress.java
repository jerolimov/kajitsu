package kajitsu.core.middleware;

import kajitsu.core.Request;
import kajitsu.core.Response;
import kajitsu.core.Use;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * <p>Add GZIP output encoding and a HTTP <code>Content-Encoding: gzip</code> header
 * if the client accepts <code>gzip</code> encoding (<code>Accept-Encoding</code> header).</p>
 *
 * TODO Improve <code>Accept-*</code> header validation.
 */
public class Compress implements Use {
    @Override
    public void use(Request request, Response response, Runnable next) {

        if (request.getHeader("Accept-Encoding") != null && request.getHeader("Accept-Encoding").contains("gzip")) {

            response.setHeader("Content-Encoding", "gzip");
            response.wrap((outputStream) -> {
                try {
                    return new GZIPOutputStream(outputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        }

        next.run();
    }
}
