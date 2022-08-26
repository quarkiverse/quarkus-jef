package io.quarkiverse.jef.java.embedded.framework.devices.library.dummy;

import java.io.*;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;
import io.quarkiverse.jef.java.embedded.framework.linux.serial.SerialBaudRate;
import io.quarkiverse.jef.java.embedded.framework.linux.serial.SerialBus;

/**
 * Some dummy device for tests
 */
public class PingPongDevice {
    private final Ponger ponger;
    private SerialBusWrapper bus;

    public PingPongDevice(SerialBus serial) {
        ponger = () -> {
            try {
                this.bus = new SerialBusWrapper(serial);
                bus.getOutputStream().write("ping".getBytes());
                InputStream inputStream = bus.getInputStream();
                int available = inputStream.available();
                byte[] b = new byte[available];
                int received = inputStream.read(b);
                if (received != available) {
                    throw new IOException("Received " + received + " bytes but expected " + available);
                }
                return new String(b);
            } catch (IOException e) {
                throw new NativeIOException(e.getMessage());
            }
        };
    }

    public String ping() throws NativeIOException {
        return ponger.pong();
    }

    private interface Ponger {
        String pong() throws NativeIOException;
    }

    private static class SerialBusWrapper implements SerialBus {
        private final SerialBus delegate;
        private final byte[] buf = "pong".getBytes();

        public SerialBusWrapper(SerialBus serial) {
            this.delegate = serial;
        }

        @Override
        public String path() {
            return delegate.path();
        }

        @Override
        public SerialBaudRate serialBaudRate() {
            return delegate.serialBaudRate();
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(buf);
        }

        @Override
        public FileHandle getHandle() {
            return delegate.getHandle();
        }

        @Override
        public OutputStream getOutputStream() {
            return new ByteArrayOutputStream(4);
        }

        @Override
        public void close() throws IOException {
            delegate.close();
        }
    }
}
