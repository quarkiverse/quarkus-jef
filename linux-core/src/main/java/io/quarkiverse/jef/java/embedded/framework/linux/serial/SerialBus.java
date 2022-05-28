package io.quarkiverse.jef.java.embedded.framework.linux.serial;

import java.io.InputStream;
import java.io.OutputStream;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;

public interface SerialBus {
    InputStream getInputStream();

    FileHandle getHandle();

    OutputStream getOutputStream();

    void close();

    static SerialBus create(String path, SerialBaudRate rate) throws NativeIOException {
        return new SerialPort(path, rate);
    }
}
