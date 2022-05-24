
package io.quarkiverse.jef.java.embedded.framework.linux.core.io;

import java.io.IOException;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Fcntl;

public class FileHandle implements AutoCloseable {
    private final int handle;

    public FileHandle(int handle) {
        this.handle = handle;
    }

    public int getHandle() {
        return handle;
    }

    @Override
    public void close() {
        try {
            Fcntl.getInstance().close(handle);
        } catch (IOException ignored) {

        }
    }

    @Override
    public String toString() {
        return "FileHandle{" +
                "handle=" + handle +
                '}';
    }
}
