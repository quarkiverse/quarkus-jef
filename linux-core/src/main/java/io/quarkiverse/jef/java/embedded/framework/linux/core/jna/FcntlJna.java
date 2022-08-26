
package io.quarkiverse.jef.java.embedded.framework.linux.core.jna;

import static io.quarkiverse.jef.java.embedded.framework.linux.core.util.StringUtils.dump;

import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.jna.Native;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Fcntl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.IOFlags;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;
import io.quarkiverse.jef.java.embedded.framework.linux.core.util.StringUtils;

public class FcntlJna extends Fcntl {
    private static final Logger log = Logger.getLogger(FcntlJna.class.getName());

    @Override
    public int open(String pathname, EnumSet<IOFlags> flags) {
        int mask = IOFlagsMask(flags);

        log.log(Level.FINEST, () -> String.format("fctl.jna open %s with flags %d", pathname, mask));

        return Delegate.open(pathname, mask);
    }

    @Override
    public int open64(String pathname, EnumSet<IOFlags> flags) {
        int mask = IOFlagsMask(flags);

        log.log(Level.FINEST, () -> String.format("fctl.jna open64 %s with flags %d", pathname, mask));

        return Delegate.open64(pathname, mask);
    }

    @Override
    public int close(int fd) {
        log.log(Level.FINEST, () -> String.format("fctl.jna close descriptor '%d'", fd));
        return Delegate.close(fd);
    }

    @Override
    public int read(FileHandle fd, byte[] buffer, int size) {
        log.log(Level.FINEST, () -> String.format("fctl.jna read from '%d' length '%d'", fd.getHandle(), size));
        int result = Delegate.read(fd.getHandle(), buffer, size);
        log.log(Level.FINEST, () -> StringUtils.dump(buffer));
        return result;
    }

    @Override
    public int write(FileHandle fd, byte[] buffer, int size) {
        log.log(Level.FINEST, () -> String.format("fctl.jna write to '%d' amount '%d'", fd.getHandle(), size));
        log.log(Level.FINEST, () -> StringUtils.dump(buffer));
        return Delegate.write(fd.getHandle(), buffer, size);
    }

    @Override
    public int fsync(FileHandle fd) {
        return Delegate.fsync(fd.getHandle());
    }

    @Override
    public long lseek(FileHandle fd, long offset, Whence whence) {
        return Delegate.lseek(fd.getHandle(), offset, whence.getValue());
    }

    @Override
    public int fcntl(FileHandle fd, int cmd, EnumSet<IOFlags> flags) {
        int mask = IOFlagsMask(flags);
        return Delegate.fcntl(fd.getHandle(), cmd, mask);
    }

    @Override
    public boolean isNativeSupported() {
        return false;
    }

    static class Delegate {
        static {
            Native.register("c");
        }

        // IO
        public static native int open(String pathname, int flags);

        public static native int open64(String path, int flags);

        public static native int close(int fd);

        public static native int fsync(int fd);

        public static native int read(int fd, byte[] buffer, int size);

        public static native int write(int fd, byte[] buffer, int size);

        public static native long lseek(int fd, long offset, int whence);

        public static native int fcntl(int fd, int cmd, int mask);
    }
}
