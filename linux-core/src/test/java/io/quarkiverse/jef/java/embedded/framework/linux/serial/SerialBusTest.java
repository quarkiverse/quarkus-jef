package io.quarkiverse.jef.java.embedded.framework.linux.serial;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.Sys;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SerialBusTest {
    private SerialBus bus;

    @BeforeAll
    public final void setUp() throws NativeIOException {
        System.setProperty("java.embedded.framework.mode", "test");

        Sys instance = Sys.getInstance();
        System.out.println("instance = " + instance);

        System.out.println("Setup");
        bus = SerialBus.create("dummypath", SerialBaudRate.B0);
        System.out.println("bus = " + bus);
    }

    @Test
    void getInputStream() {
    }

    @Test
    void getHandle() {
    }

    @Test
    void getOutputStream() {
    }

    @Test
    void close() {
    }

    @Test
    void create() {
    }
}
