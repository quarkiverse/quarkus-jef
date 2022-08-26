
package io.quarkiverse.jef.java.embedded.framework.linux.core;

import java.io.IOException;

public class NativeIOException extends IOException {
    private final int code;

    public NativeIOException(String message) {
        super(message);
        code = Errno.getInstance().ierrno();
    }

    public NativeIOException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + code + ":" + Errno.getInstance().strerror(code) + ")";
    }
}
