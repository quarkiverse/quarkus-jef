package io.quarkiverse.jef.java.embedded.framework.linux.core.natives;

import static io.quarkiverse.jef.java.embedded.framework.linux.core.natives.CUtil.toCString;
import static io.quarkiverse.jef.java.embedded.framework.linux.core.util.StringUtils.dump;

import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.graalvm.nativeimage.PinnedObject;
import org.graalvm.nativeimage.c.CContext;
import org.graalvm.nativeimage.c.function.CFunction;
import org.graalvm.nativeimage.c.function.CLibrary;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.word.PointerBase;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Fcntl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.IOFlags;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;

@CContext(FcntlNativeHeaders.class)
@CLibrary("c")
public class FcntlNative extends Fcntl {
    private static final Logger log = Logger.getLogger(FcntlNative.class.getName());

    @Override
    public int open(String pathname, EnumSet<IOFlags> flags) {
        int mask = IOFlagsMask(flags);

        log.log(Level.FINEST, () -> String.format("fctl.native open %s with flags %d", pathname, mask));

        return Delegate.open(toCString(pathname), mask);
    }

    @Override
    public int open64(String pathname, EnumSet<IOFlags> flags) {
        int mask = IOFlagsMask(flags);

        log.log(Level.FINEST, () -> String.format("fctl.native open64 %s with flags %d", pathname, mask));

        return Delegate.open64(toCString(pathname), mask);
    }

    @Override
    public int close(int fd) {
        log.log(Level.FINEST, () -> String.format("fctl.native close descriptor '%d'", fd));
        return Delegate.close(fd);
    }

    @Override
    public int read(FileHandle fd, byte[] buffer, int size) {
        log.log(Level.FINEST, () -> String.format("fctl.native read from '%d' length '%d'", fd.getHandle(), size));
        try (PinnedObject pin = PinnedObject.create(buffer)) {
            CCharPointer rawData = pin.addressOfArrayElement(0);
            //SizeOf.get(rawData.getClass())
            int result = Delegate.read(fd.getHandle(), rawData, size);
            log.log(Level.FINEST, () -> dump(buffer));
            return result;
        }
    }

    @Override
    public int write(FileHandle fd, byte[] buffer, int size) {
        try (PinnedObject pin = PinnedObject.create(buffer)) {
            CCharPointer rawData = pin.addressOfArrayElement(0);
            return Delegate.write(fd.getHandle(), rawData, size);
        }
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
        return Delegate.fcntl(fd.getHandle(), mask);
    }

    @Override
    public boolean isNativeSupported() {
        return true;
    }

    private static class Delegate {
        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        private static native int open(CCharPointer path, int flags);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int open64(CCharPointer path, int flags);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int close(int fd);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int read(int fd, CCharPointer buffer, int size);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int write(int fd, CCharPointer buffer, int size);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int fsync(int fd);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int ioctl(int fd, long command, long arg);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int ioctl(int fd, long command, PointerBase p);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int fcntl(int fd, int cmd);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native long lseek(int fd, long offset, int whence);
    }
}
