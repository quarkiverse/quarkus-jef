package io.quarkiverse.jef.java.embedded.framework.linux.core.mook;

import java.util.EnumSet;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Fcntl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.IOFlags;
import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;

public class FcntlMock extends Fcntl {
    private final static FileHandle HANDLE = new FileHandle(0);

    @Override
    public boolean isMock() {
        return true;
    }

    @Override
    public FileHandle open(String pathname, EnumSet<IOFlags> flags) throws NativeIOException {
        return HANDLE;
    }

    @Override
    public FileHandle open64(String pathname, EnumSet<IOFlags> flags) throws NativeIOException {
        return HANDLE;
    }

    @Override
    public void close(FileHandle fd) throws NativeIOException {

    }

    @Override
    public void close(int fd) throws NativeIOException {

    }

    @Override
    public int read(FileHandle fd, byte[] buffer, int size) throws NativeIOException {
        return size;
    }

    @Override
    public void write(FileHandle fd, byte[] buffer, int size) throws NativeIOException {
    }

    @Override
    public void fsync(FileHandle fd) throws NativeIOException {

    }

    @Override
    public long lseek(FileHandle fd, long offset, Whence whence) {
        return offset;
    }

    @Override
    public int fcntl(FileHandle fd, int cmd, EnumSet<IOFlags> flags) throws NativeIOException {
        return 0;
    }

    @Override
    public boolean isNativeSupported() {
        return true;
    }
}
