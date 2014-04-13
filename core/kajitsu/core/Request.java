package kajitsu.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * <p>Wrapper for the HTTP request.</p>
 */
public class Request {
    protected Logger LOGGER = Logger.getLogger(getClass().getName());

    private final InputStream inputStream;

    private final Map<String, String> headers = new LinkedHashMap<>();

    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;

        readStatus();
        readHeader();
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    protected void readStatus() throws IOException {
        String status = readLine();
        LOGGER.fine("Request status: " + status);
    }

    protected void readHeader() throws IOException {
        String key, value;
        for (String line = readLine(); !line.isEmpty(); line = readLine()) {
            if (line.contains(": ")) {
                key = line.substring(0, line.indexOf(": "));
                value = line.substring(line.indexOf(": ") + 2);
            } else {
                key = line;
                value = null;
            }
            LOGGER.finer("Request header: " + key + ": " + value);
            headers.put(key, value);
        }
    }

    public String readLine() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = inputStream.read(); i != -1 && i != 10 && i != 13; i = inputStream.read()) {
            baos.write(i);
        }
        inputStream.read();
        return new String(baos.toByteArray(), "ASCII");
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
