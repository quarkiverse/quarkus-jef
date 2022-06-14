package io.quarkiverse.jef.java.embedded.framework.linux.serial;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.quarkiverse.jef.java.embedded.framework.devices.library.dummy.PingPongDevice;
import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SerialBusTest {

    @Test
    void getInputStream() throws NativeIOException {
        SerialBus bus = getBus();
        InputStream is = bus.getInputStream();
        Assertions.assertNotNull(is);
    }

    @Test
    void getHandle() throws NativeIOException {
        SerialBus bus = getBus();
        FileHandle handle = bus.getHandle();
        Assertions.assertNotNull(handle);
        Assertions.assertNotEquals(-1, handle.getHandle());
    }

    @Test
    void getOutputStream() throws NativeIOException {
        SerialBus bus = getBus();
        OutputStream os = bus.getOutputStream();
        Assertions.assertNotNull(os);
    }

    @Test
    void close() throws IOException {
        SerialBus bus = getBus();
        bus.close();
        Assertions.assertThrowsExactly(IOException.class, bus::close);
    }

    @Test
    void testReadWrite() throws NativeIOException {
        PingPongDevice device = new PingPongDevice(getBus());
        assertEquals("pong", device.ping());
    }

    private SerialBus getBus() throws NativeIOException {
        return SerialBus.create("dummypath", SerialBaudRate.B0);
    }

}
