package com.inred.library.filehttputil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by inred on 2015/9/11.
 */
class VelocityMultipartEntity implements HttpEntity {

    private HttpEntity httpEntity;
    private ProgressListener listener;

    VelocityMultipartEntity(HttpEntity httpEntity, ProgressListener listener) {
        if (httpEntity == null) {
            throw new IllegalArgumentException
                    ("httpEntity must not be null");
        }
        this.httpEntity = httpEntity;
        this.listener = listener;
    }

    @Override
    public boolean isRepeatable() {
        return httpEntity.isRepeatable();
    }

    @Override
    public boolean isChunked() {
        return httpEntity.isChunked();
    }

    @Override
    public long getContentLength() {
        return httpEntity.getContentLength();
    }

    @Override
    public Header getContentType() {
        return httpEntity.getContentType();
    }

    @Override
    public Header getContentEncoding() {
        return httpEntity.getContentEncoding();
    }

    @Override
    public InputStream getContent() throws IOException, IllegalStateException {
        return httpEntity.getContent();
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        new VelocityOutPutStream(outputStream, this.listener);
        httpEntity.writeTo(outputStream);
    }

    @Override
    public boolean isStreaming() {
        return httpEntity.isStreaming();
    }

    @Override
    public void consumeContent() throws IOException {
        httpEntity.consumeContent();
    }

    interface ProgressListener {
        void transferred(long num, int progress);
    }

    class VelocityOutPutStream extends FilterOutputStream {

        private ProgressListener listener;
        private long transferred;

        /**
         * Constructs a new {@code FilterOutputStream} with {@code out} as its
         * target stream.
         *
         * @param out the target stream that this stream writes to.
         */
        public VelocityOutPutStream(OutputStream out, ProgressListener listener) {
            super(out);
            this.listener = listener;
            transferred = 0;
        }


        @Override
        public void write(byte[] buffer, int offset, int length) throws IOException {
            super.write(buffer, offset, length);
            this.transferred += length;
            if (listener != null)
                listener.transferred(transferred, (int) (transferred / getContentLength() * 100));
        }

        @Override
        public void write(int oneByte) throws IOException {
            super.write(oneByte);
            this.transferred++;
            if (listener != null)
                listener.transferred(transferred, (int) (transferred / getContentLength() * 100));
        }
    }

}
