package io.quarkiverse.jef.java.embedded.framework.linux.i2c;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;

import java.nio.ByteBuffer;

public interface I2CInterface {
    SMBus getSmBus();

    void read(ByteBuffer buf) throws NativeIOException;

    void read(ByteBuffer buf, int length) throws NativeIOException;

    void write(ByteBuffer buf) throws NativeIOException;

    void write(ByteBuffer buf, int length) throws NativeIOException;

    Object synchLock();

    void synchSelect() throws NativeIOException;

    FileHandle getFD();

    String getPath();

    I2CBus getI2CBus();

    int getAddress();
}
