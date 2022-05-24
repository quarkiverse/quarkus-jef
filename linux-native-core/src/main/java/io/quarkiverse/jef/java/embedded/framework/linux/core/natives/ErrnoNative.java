package io.quarkiverse.jef.java.embedded.framework.linux.core.natives;

import org.graalvm.nativeimage.c.CContext;
import org.graalvm.nativeimage.c.constant.CConstant;
import org.graalvm.nativeimage.c.function.CFunction;
import org.graalvm.nativeimage.c.function.CLibrary;
import org.graalvm.nativeimage.c.type.CCharPointer;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Errno;

@CContext(ErrnoNativeHeaders.class)
@CLibrary("c")
public class ErrnoNative extends Errno {
    @Override
    public int ierrno() {
        return Delegate.errno();
    }

    @Override
    public String strerror(int number) {
        return CUtil.fromCString(Delegate.strerror(number));
    }

    @Override
    public int perror(String err) {
        return Delegate.perror(CUtil.toCString(err));
    }

    @Override
    public String strerror() {
        return strerror(ierrno());
    }

    @Override
    public boolean isNativeSupported() {
        return true;
    }

    private static class Delegate {
        @CConstant
        public static native int errno();

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native CCharPointer strerror(int errnum);

        @CFunction(transition = CFunction.Transition.NO_TRANSITION)
        public static native int perror(CCharPointer s);

    }
}
