package io.quarkiverse.jef.java.embedded.framework.linux.spi;

import java.io.IOException;
import java.nio.ByteBuffer;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;

/**
 * Debug:
 * cd /sys/kernel/debug/tracing
 * echo 'spi:*' > /sys/kernel/debug/tracing/set_event
 * less trace
 */
public interface SpiBus {
    String getBus();

    FileHandle getFd();

    int getClockFrequency();

    void setClockFrequency(int value) throws NativeIOException;

    SpiMode getClockMode();

    void setClockMode(SpiMode clockMode) throws NativeIOException;

    int getWordLength();

    void setWordLength(int wordLength) throws NativeIOException;

    int getBitOrdering();

    void reload() throws NativeIOException;

    void setBitOrdering(int bitOrdering) throws NativeIOException;

    int readByteData(SpiInputParams inputParams) throws IOException;

    void writeByteData(SpiInputParams inputParams) throws NativeIOException;

    ByteBuffer readArray(SpiInputParams inputParams, int outputSize) throws NativeIOException;

    ByteBuffer readWriteData(ByteBuffer input, int outputSize) throws NativeIOException;

    static SpiBus createFullDuplex(String path) throws NativeIOException {
        return new FullDuplexSpiBus(path);
    }

    static SpiBus createHalfDuplex(String path) throws NativeIOException {
        return new HalfDuplexSpiBus(path);
    }
}
