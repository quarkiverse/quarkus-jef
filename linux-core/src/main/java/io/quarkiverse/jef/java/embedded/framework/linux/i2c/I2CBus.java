package io.quarkiverse.jef.java.embedded.framework.linux.i2c;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;

import java.util.List;

public interface I2CBus {
    enum Status {
        /**
         * Device in address available
         */
        AVAILABLE,
        /**
         * Device in address is busy
         */
        NOT_AVAILABLE,
        /**
         * Device is busy at this moment
         */
        BUSY,
        /**
         * Skipped address because I2C bus not supporting read/write operations for this address
         */
        SKIP,
        /**
         * Device status is unknown
         */
        UNKNOWN
    }

    I2CInterface select(int address, boolean force, boolean isTenBit) throws NativeIOException;

    boolean support(I2CFunctionality functionality);

    int getRetries();

    void setRetries(int retries) throws NativeIOException;

    int getTimeout();

    void setTimeout(int timeoutMs) throws NativeIOException;

    I2CInterface select(int address) throws NativeIOException;

    void selectSlave(int address, boolean force) throws NativeIOException;

    void setTenBits(boolean isTenBit) throws NativeIOException;

    List<Status> enumerate();

    String getPath();

    static I2CBus create(String path) throws NativeIOException {
        return new I2CBusImpl(path);
    }
}
