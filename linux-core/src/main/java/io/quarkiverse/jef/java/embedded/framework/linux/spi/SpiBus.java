package io.quarkiverse.jef.java.embedded.framework.linux.spi;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Ioctl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.IntReference;
import io.quarkiverse.jef.java.embedded.framework.linux.core.types.SpiIocTransfer;
import io.quarkiverse.jef.java.embedded.framework.linux.core.util.StringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;

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
