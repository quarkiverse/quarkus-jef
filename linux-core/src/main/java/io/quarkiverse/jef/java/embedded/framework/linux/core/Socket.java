
package io.quarkiverse.jef.java.embedded.framework.linux.core;

import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("unused")
@Deprecated
public abstract class Socket implements FeatureSupport {
    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    private static Socket instance = null;

    public static Socket getInstance() {
        if (instance == null && !initialized.get()) {
            synchronized (Socket.class) {
                if (instance == null && !initialized.get()) {
                    instance = NativeBeanLoader.createContent(Socket.class);
                    initialized.set(true);
                }
            }
        }
        return instance;
    }

    public abstract int getsockopt(int sockfd, int level, int optname, Bluetooth.HciFilter optval);

    public abstract int setsockopt(int sockfd, int level, int optname, Bluetooth.HciFilter optval);
}
