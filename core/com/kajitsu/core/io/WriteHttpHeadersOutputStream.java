package kajitsu.core.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>OutputStream facade which submit the HTTP headers when required.</p>
 */
public class WriteHttpHeadersOutputStream extends OutputStream {

    private final OutputStream delegate;
    private final Runnable writeHeaders;
    private boolean isHeaderAlreadyWritten;

    public WriteHttpHeadersOutputStream(OutputStream delegate, Runnable writeHeaders) {
        this.delegate = delegate;
        this.writeHeaders = writeHeaders;
    }

    public boolean isHeaderAlreadyWritten() {
        return isHeaderAlreadyWritten;
    }

    private void assertHeaderIsWrittenOnce() {
        if (!isHeaderAlreadyWritten) {
            isHeaderAlreadyWritten = true;
            writeHeaders.run();
        }
    }

    @Override
    public void write(int b) throws IOException {
        assertHeaderIsWrittenOnce();
        delegate.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        assertHeaderIsWrittenOnce();
        delegate.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        assertHeaderIsWrittenOnce();
        delegate.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        assertHeaderIsWrittenOnce();
        delegate.flush();
    }

    @Override
    public void close() throws IOException {
        assertHeaderIsWrittenOnce();
        delegate.close();
    }
}