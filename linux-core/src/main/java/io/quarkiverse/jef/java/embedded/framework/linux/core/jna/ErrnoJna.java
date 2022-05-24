
package io.quarkiverse.jef.java.embedded.framework.linux.core.jna;

import com.sun.jna.Native;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Errno;

public class ErrnoJna extends Errno {
    @Override
    public int ierrno() {
        return Native.getLastError();
    }

    @Override
    public String strerror(int errnum) {
        return Delegate.strerror(errnum);
    }

    @Override
    public int perror(String err) {
        return Delegate.perror(err);
    }

    @Override
    public String strerror() {
        return strerror(ierrno());
    }

    @Override
    public boolean isNativeSupported() {
        return false;
    }

    static class Delegate {
        public static native int perror(String s);

        public static native String strerror(int errnum);

        static {
            Native.register("c");
        }
    }
}
