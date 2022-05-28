package io.quarkiverse.jef.java.embedded.framework.linux.spi;

import java.io.IOException;
import java.nio.ByteBuffer;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;

public interface SpiBus {
    String getBus();

    FileHandle getFd();

    int getClockFrequency() throws NativeIOException;

    void setClockFrequency(int value) throws NativeIOException;

    SpiMode getClockMode() throws NativeIOException;

    void setClockMode(SpiMode clockMode) throws NativeIOException;

    int getWordLength() throws NativeIOException;

    void setWordLength(int wordLength) throws NativeIOException;

    int getBitOrdering() throws NativeIOException;

    void setBitOrdering(int bitOrdering) throws NativeIOException;

    int readByteData(SpiInputParams inputParams) throws IOException;

    void writeByteData(SpiInputParams inputParams) throws NativeIOException;

    ByteBuffer readArray(SpiInputParams inputParams, int outputSize) throws NativeIOException;

    ByteBuffer readWriteData(ByteBuffer input, int outputSize) throws NativeIOException;

    static SpiBus create(String path) throws NativeIOException {
        return new SpiBusImpl(path);
    }
}
