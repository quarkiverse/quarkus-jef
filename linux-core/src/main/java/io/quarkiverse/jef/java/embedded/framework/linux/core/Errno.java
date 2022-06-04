
package io.quarkiverse.jef.java.embedded.framework.linux.core;

import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("unused")
public abstract class Errno implements FeatureSupport {
    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    private static Errno instance = null;

    public abstract int ierrno();

    public abstract String strerror(int errnum);

    public abstract int perror(String err);

    public abstract String strerror();

    public static Errno getInstance() {
        if (instance == null && !initialized.get()) {
            synchronized (Errno.class) {
                if (instance == null && !initialized.get()) {
                    instance = NativeBeanLoader.createContent(Errno.class);
                    initialized.set(true);
                }
            }
        }
        return instance;
    }
}
