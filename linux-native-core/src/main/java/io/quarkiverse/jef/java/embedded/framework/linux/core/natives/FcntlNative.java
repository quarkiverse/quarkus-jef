package io.quarkiverse.jef.java.embedded.framework.linux.core.natives;

import static io.quarkiverse.jef.java.embedded.framework.linux.core.LinuxUtils.checkIOResult;
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
import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandlerCleaner;

@CContext(FcntlNativeHeaders.class)
@CLibrary("c")
public class FcntlNative extends Fcntl {
    private static final Logger log = Logger.getLogger(FcntlNative.class.getName());

    @Override
    public FileHandle open(String pathname, EnumSet<IOFlags> flags) throws NativeIOException {
        int mask = IOFlagsMask(flags);

        log.log(Level.FINEST, () -> String.format("fctl.native open %s with flags %d", pathname, mask));

        int result = Delegate.open(toCString(pathname), mask);
        checkIOResult("open", result);

        FileHandle handle = new FileHandle(result);
        FileHandlerCleaner.register(handle);

        return handle;
    }

    @Override
    public FileHandle open64(String pathname, EnumSet<IOFlags> flags) throws NativeIOException {
        int mask = IOFlagsMask(flags);

        log.log(Level.FINEST, () -> String.format("fctl.native open64 %s with flags %d", pathname, mask));

        int result = Delegate.open64(toCString(pathname), mask);
        checkIOResult("open64", result);

        FileHandle handle = new FileHandle(result);
        FileHandlerCleaner.register(handle);

        return handle;
    }

    @Override
    public void close(FileHandle fd) throws NativeIOException {
        log.log(Level.FINEST, () -> String.format("fctl.native close descriptor '%d'", fd.getHandle()));
        int result = Delegate.close(fd.getHandle());
        checkIOResult("close", result);
    }

    @Override
    public void close(int fd) throws NativeIOException {
        log.log(Level.FINEST, () -> String.format("fctl.native close descriptor '%d'", fd));
        int result = Delegate.close(fd);
        checkIOResult("close", result);
    }

    @Override
    public int read(FileHandle fd, byte[] buffer, int size) throws NativeIOException {
        log.log(Level.FINEST, () -> String.format("fctl.native read from '%d' length '%d'", fd.getHandle(), size));
        try (PinnedObject pin = PinnedObject.create(buffer)) {
            CCharPointer rawData = pin.addressOfArrayElement(0);
            //SizeOf.get(rawData.getClass())
            int result = Delegate.read(fd.getHandle(), rawData, size);
            log.log(Level.FINEST, () -> dump(buffer));
            checkIOResult("read", result);
            return result;
        }
    }

    @Override
    public void write(FileHandle fd, byte[] buffer, int size) throws NativeIOException {
        try (PinnedObject pin = PinnedObject.create(buffer)) {
            CCharPointer rawData = pin.addressOfArrayElement(0);
            int result = Delegate.write(fd.getHandle(), rawData, size);
            checkIOResult("write", result);
        }
    }

    @Override
    public long lseek(FileHandle fd, long offset, Whence whence) {
        return Delegate.lseek(fd.getHandle(), offset, whence.getValue());
    }

    @Override
    public int fcntl(FileHandle fd, int cmd, EnumSet<IOFlags> flags) throws NativeIOException {
        int mask = IOFlagsMask(flags);
        int result = Delegate.fcntl(fd.getHandle(), mask);
        checkIOResult("fcntl", result);
        return result;
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
        public static native int ioctl(int fd, long command, long arg);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int ioctl(int fd, long command, PointerBase p);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int fcntl(int fd, int cmd);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native long lseek(int fd, long offset, int whence);
    }
}
