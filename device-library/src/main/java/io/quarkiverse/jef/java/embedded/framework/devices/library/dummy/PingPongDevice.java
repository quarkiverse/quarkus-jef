package io.quarkiverse.jef.java.embedded.framework.devices.library.dummy;

import java.io.IOException;
import java.io.InputStream;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.serial.SerialBus;

/**
 * Some dummy device for tests
 */
public class PingPongDevice {
    private final Ponger ponger;

    public PingPongDevice(SerialBus serial) {
        ponger = () -> {
            try {
                serial.getOutputStream().write("ping".getBytes());
                InputStream inputStream = serial.getInputStream();
                int available = inputStream.available();
                byte[] b = new byte[available];
                int received = inputStream.read(b);
                if (received != available) {
                    throw new IOException("Received " + received + " bytes but expected " + available);
                }
                return new String(b);
            } catch (IOException e) {
                throw new NativeIOException(e, -1);
            }
        };
    }

    public String ping() throws NativeIOException {
        return ponger.pong();
    }

    private interface Ponger {
        String pong() throws NativeIOException;
    }
}
