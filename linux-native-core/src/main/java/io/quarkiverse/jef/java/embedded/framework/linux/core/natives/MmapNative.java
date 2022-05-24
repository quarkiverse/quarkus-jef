package io.quarkiverse.jef.java.embedded.framework.linux.core.natives;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.EnumSet;

import org.graalvm.nativeimage.c.function.CFunction;
import org.graalvm.nativeimage.c.type.CTypeConversion;
import org.graalvm.word.Pointer;
import org.graalvm.word.PointerBase;
import org.graalvm.word.WordFactory;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Errno;
import io.quarkiverse.jef.java.embedded.framework.linux.core.Mmap;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;

public class MmapNative extends Mmap {
    @Override
    public boolean isNativeSupported() {
        return true;
    }

    @Override
    public ByteBuffer mmap(FileHandle handle, Mmap.MemoryProtection protection, Mmap.MemoryFlag flags, long offset, int size)
            throws IOException {
        return mmap(handle, EnumSet.of(protection), flags, offset, size);
    }

    @Override
    public ByteBuffer mmap(FileHandle handle, EnumSet<Mmap.MemoryProtection> protection, Mmap.MemoryFlag flags, long offset,
            int size) throws IOException {
        Pointer p = WordFactory.pointer(0);

        PointerBase result = Delegate.mmap(
                p,
                size,
                memoryProtectionFlag(protection),
                flags.getValue(),
                handle.getHandle(),
                offset);

        if (result.rawValue() == -1) {
            Errno err = Errno.getInstance();
            System.out.println(err.strerror());
            throw new IOException("mmap failed: " + err.strerror());
        }

        return CTypeConversion.asByteBuffer(result, size);
    }

    private static class Delegate {
        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native PointerBase mmap(PointerBase addr,
                long len,
                int prot,
                int flags,
                int fd,
                long off);
    }
}
