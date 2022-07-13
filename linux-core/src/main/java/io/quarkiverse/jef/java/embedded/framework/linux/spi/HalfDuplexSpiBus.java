package io.quarkiverse.jef.java.embedded.framework.linux.spi;

import java.io.IOException;
import java.nio.ByteBuffer;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;

@SuppressWarnings("unused")
public class HalfDuplexSpiBus extends AbstractSpiBus {
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
    public int readByteData(SpiInputParams inputParams) throws IOException {
        return 0;
    }

    @Override
    public void writeByteData(SpiInputParams inputParams) throws NativeIOException {

    }

    @Override
    public ByteBuffer readArray(SpiInputParams inputParams, int outputSize) throws NativeIOException {
        return null;
    }

    @Override
    public ByteBuffer readWriteData(ByteBuffer input, int outputSize) throws NativeIOException {
        return null;
    }
}
