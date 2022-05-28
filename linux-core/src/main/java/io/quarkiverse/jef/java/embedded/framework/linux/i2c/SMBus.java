package io.quarkiverse.jef.java.embedded.framework.linux.i2c;

import java.io.IOException;
import java.nio.ByteBuffer;

import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;

public interface SMBus {

    void usePEC(boolean usePEC) throws IOException;

    void writeQuick(boolean isWrite) throws IOException;

    @SuppressWarnings("UnusedReturnValue")
    int readByte() throws IOException;

    int readByteData(int command) throws IOException;

    int readWordData(int command) throws IOException;

    ByteBuffer readBlockData(int command) throws IOException;

    void writeByte(int b) throws IOException;

    void writeByteData(int command, int b) throws IOException;

    void writeWordData(int command, int word) throws IOException;

    void writeBlockData(int command, ByteBuffer buf) throws IOException;

    void i2cSmbusAccess(byte readWrite,
            long command,
            int size,
            byte[] data) throws IOException;

    I2CInterface getInterface();

    FileHandle fd();

}
