package io.quarkiverse.jef.java.embedded.framework.linux.core.mook;

import java.util.EnumSet;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Fcntl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.IOFlags;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;

public class FcntlMock extends Fcntl {

    @Override
    public boolean isMock() {
        return true;
    }

    @Override
    public int open(String pathname, EnumSet<IOFlags> flags) {
        return 0;
    }

    @Override
    public int open64(String pathname, EnumSet<IOFlags> flags) {
        return 0;
    }

    @Override
    public int close(int fd) {
        return 0;
    }

    @Override
    public int read(FileHandle fd, byte[] buffer, int size) {
        return size;
    }

    @Override
    public int write(FileHandle fd, byte[] buffer, int size) {
        return size;
    }

    @Override
    public int fsync(FileHandle fd) {
        return 0;
    }

    @Override
    public long lseek(FileHandle fd, long offset, Whence whence) {
        return offset;
    }

    @Override
    public int fcntl(FileHandle fd, int cmd, EnumSet<IOFlags> flags) {
        return 0;
    }

    @Override
    public boolean isNativeSupported() {
        return true;
    }
}
