package kajitsu.core;

import kajitsu.core.io.WriteHttpHeadersOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Wrapper for the HTTP response.</p>
 */
public class Response {
    protected Logger LOGGER = Logger.getLogger(getClass().getName());

    private final WriteHttpHeadersOutputStream originalOutputStream;
    private OutputStream currentOutputStream;

    private String httpPrefix = "HTTP/";
    private String httpVersion = "1.1";
    private String statusCode = "200";
    private String statusMessage = "OK";

    private final Map<String, String> headers = new LinkedHashMap<>();

    public Response(OutputStream outputStream) {
        this.originalOutputStream = new WriteHttpHeadersOutputStream(outputStream, () -> {
            try {
                writeHeaders();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error while writing headers: " + e, e);
            }
        });
        this.currentOutputStream = this.originalOutputStream;
    }

    public void setHeader(String key, String value) {
        if (originalOutputStream.isHeaderAlreadyWritten()) {
            throw new IllegalStateException("Header is already written!");
        }

        LOGGER.info("Response header: " + key + ": " + value);
        headers.put(key, value);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public boolean containsHeaderKey(String key) {
        return headers.containsKey(key);
    }

    private void writeHeaders() throws IOException {
        StringWriter writer = new StringWriter();
        writer.write(httpPrefix + httpVersion + " " + statusCode + " " + statusMessage + "\n");
        headers.forEach((key, value) -> {
            writer.write(key + ": " + value + "\n");
        });
        writer.write("\n");

        originalOutputStream.write(writer.toString().getBytes());
        originalOutputStream.flush();
    }

    public OutputStream getOutputStream() {
        return currentOutputStream;
    }

    public void wrap(UnaryOperator<OutputStream> mapper) {
        currentOutputStream = mapper.apply(currentOutputStream);
    }

    public void flush() throws IOException {
        if (!originalOutputStream.isHeaderAlreadyWritten()) {
            originalOutputStream.flush();
        }

        currentOutputStream.flush();
    }

    public void close() throws IOException {
        if (!originalOutputStream.isHeaderAlreadyWritten()) {
            originalOutputStream.flush();
        }

        currentOutputStream.close();

        if (currentOutputStream != originalOutputStream) {
            originalOutputStream.close();
        }
    }
}
