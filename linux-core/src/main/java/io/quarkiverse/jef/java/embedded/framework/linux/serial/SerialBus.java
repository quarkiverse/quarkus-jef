package io.quarkiverse.jef.java.embedded.framework.linux.serial;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;

import java.io.InputStream;
import java.io.OutputStream;

public interface SerialBus {
    InputStream getInputStream();

    FileHandle getHandle();

    OutputStream getOutputStream();

    void close();

    static SerialBus create(String path, SerialBaudRate rate) throws NativeIOException {
        return new SerialPort(path, rate);
    }
}
