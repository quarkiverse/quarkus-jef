
package io.quarkiverse.jef.java.embedded.framework.linux.core;

import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicBoolean;

import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;

@SuppressWarnings("unused")
public abstract class Fcntl implements FeatureSupport {
    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    private static Fcntl instance = null;
    public static int F_DUPFD = 0; /* dup */
    public static int F_GETFD = 1; /* get close_on_exec */
    public static int F_SETFD = 2; /* set/clear close_on_exec */
    public static int F_GETFL = 3; /* get file->f_flags */
    public static int F_SETFL = 4; /* set file->f_flags */
    public static int F_GETLK = 5;
    public static int F_SETLK = 6;
    public static int F_SETLKW = 7;

    protected static int IOFlagsMask(EnumSet<IOFlags> flags) {
        int result = 0;

        for (IOFlags flag : flags) {
            result = result | flag.value;
        }

        return result;
    }

    public static Fcntl getInstance() {
        if (instance == null && !initialized.get()) {
            synchronized (Fcntl.class) {
                if (instance == null && !initialized.get()) {
                    instance = NativeBeanLoader.createContent(Fcntl.class);
                    initialized.set(true);
                }
            }
        }
        return instance;
    }

    public int open(String pathname, IOFlags flag) {
        return open(pathname, EnumSet.of(flag));
    }

    public abstract int open(String pathname, EnumSet<IOFlags> flags);

    public abstract int open64(String pathname, EnumSet<IOFlags> flags);

    public int close(FileHandle fd) {
        return close(fd.getHandle());
    }

    public abstract int close(int fd);

    public abstract int read(FileHandle fd, byte[] buffer, int size);

    public abstract int write(FileHandle fd, byte[] buffer, int size);

    public abstract int fsync(FileHandle fd);

    public abstract long lseek(FileHandle fd, long offset, Whence whence);

    public abstract int fcntl(FileHandle fd, int cmd, EnumSet<IOFlags> flags);

    public enum Whence {
        SEEK_SET(0),
        SEEK_CUR(1),
        SEEK_END(2);

        private final int value;

        Whence(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
