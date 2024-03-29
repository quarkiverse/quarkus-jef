package io.quarkiverse.jef.java.embedded.framework.linux.spi;

import java.nio.ByteBuffer;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Fcntl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;

@SuppressWarnings("unused")
public class HalfDuplexSpiBus extends AbstractSpiBus {
    private final Fcntl fcntl = Fcntl.getInstance();

    public HalfDuplexSpiBus(int busNumber) throws NativeIOException {
        super(busNumber);
    }

    public HalfDuplexSpiBus(String bus) throws NativeIOException {
        super(bus);
    }

    public HalfDuplexSpiBus(int busNumber, int clockFrequency, SpiMode clockMode, int wordLength, int bitOrdering)
            throws NativeIOException {
        super(busNumber, clockFrequency, clockMode, wordLength, bitOrdering);
    }

    public HalfDuplexSpiBus(String bus, int clockFrequency, SpiMode clockMode, int wordLength, int bitOrdering)
            throws NativeIOException {
        super(bus, clockFrequency, clockMode, wordLength, bitOrdering);
    }

    @Override
    public ByteBuffer readWriteData(ByteBuffer input, int outputSize) throws NativeIOException {
        input.position(0);
        byte[] in = new byte[input.capacity()];
        byte[] out = new byte[outputSize];
        input.get(in);
        int write = fcntl.write(fd, in, in.length);
        if (write < 0) {
            throw new NativeIOException("Unable to write to serial port: " + getBus());
        }
        //fcntl.fsync(fd);
        if (fcntl.read(fd, out, outputSize) < 0) {
            throw new NativeIOException("Unable to read from spi bus: " + this.getBus());
        }
        return ByteBuffer.wrap(out);
    }
}
