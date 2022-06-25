package io.quarkiverse.jef.java.embedded.framework.linux.core;

import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;

import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("UnusedReturnValue")
public class OneWireDevice {

    private final String path;
    private final InputStream is;

    public OneWireDevice(String path) throws NativeIOException {
        this.path = path;
        this.is = new OneWireInputStream();
    }

    public InputStream getInputStream() {
        return is;
    }

    private class OneWireInputStream extends InputStream {
        private final Fcntl fcntl;

        public OneWireInputStream() throws NativeIOException {
            this.fcntl = Fcntl.getInstance();

        }

        @Override
        public int read() throws IOException {
            byte[] buffer = new byte[1];
            try(FileHandle handle = fcntl.open(OneWireDevice.this.path, IOFlags.O_RDONLY)) {
                int length = fcntl.read(handle, buffer, buffer.length);
                return (length == 0) ? -1 : buffer[0];
            }
        }

        @Override
        public int read(byte[] buffer) throws IOException {
            try(FileHandle handle = fcntl.open(OneWireDevice.this.path, IOFlags.O_RDONLY)) {
                int length = fcntl.read(handle, buffer, buffer.length);
                return length;
            }
        }
    }
}
