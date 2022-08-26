
package io.quarkiverse.jef.java.embedded.framework.linux.core.io;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Fcntl;

public class FileHandle implements AutoCloseable {
    private final int handle;

    FileHandle(int handle) {
        this.handle = handle;
    }

    public int getHandle() {
        return handle;
    }

    @Override
    public void close() {
        Fcntl.getInstance().close(handle);
    }

    public static FileHandle create(int fd) {
        FileHandle result = new FileHandle(fd);
        FileHandlerCleaner.register(result);
        return result;
    }

    @Override
    public String toString() {
        return "FileHandle{" +
                "handle=" + handle +
                '}';
    }
}
