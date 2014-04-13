package kajitsu.core.middleware;

import kajitsu.core.Request;
import kajitsu.core.Response;
import kajitsu.core.Use;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>Keep output stream away from the underlaying stream (normally the socket stream)
 * and calculate the <code>Content-Length</code> header automatically.</p>
 *
 * <p>Notice that the output is handled in-memory, so this could not be used for
 * large download files. If the header is already set this step will be skipped.</p>
 */
public class CalculateContentLength implements Use {
    @Override
    public void use(Request request, Response response, Runnable next) {
        if (response.containsHeaderKey("Content-Length")) {
            next.run();
            return;
        }

        AtomicReference<OutputStream> originalOutputStreamReference = new AtomicReference<>();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        response.wrap((outputStream) -> {
            originalOutputStreamReference.set(outputStream);
            return byteArrayOutputStream;
        });

        next.run();

        response.wrap((outputStream) -> {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return originalOutputStreamReference.get();
        });

        if (!response.containsHeaderKey("Content-Length")) {
            response.setHeader("Content-Length", Integer.toString(byteArrayOutputStream.size()));
        }

        try {
            response.getOutputStream().write(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
