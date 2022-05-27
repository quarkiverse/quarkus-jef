
package io.quarkiverse.jef.java.embedded.framework.linux.gpio;

import static io.quarkiverse.jef.java.embedded.framework.linux.core.IOFlags.O_CLOEXEC;
import static io.quarkiverse.jef.java.embedded.framework.linux.core.IOFlags.O_RDONLY;

import java.io.IOException;
import java.util.EnumSet;

import io.quarkiverse.jef.java.embedded.framework.linux.core.Fcntl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.Ioctl;
import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.core.io.FileHandle;

@SuppressWarnings("unused")
public interface GpioPin extends AutoCloseable {
    int getPinNumber();

    String getName();

    String getConsumer();

    Direction getDirection();

    int getFlags();

    State read() throws IOException;

    void write(State state) throws IOException;

    void setDirection(Direction direction) throws IOException;

    public enum State {
        LOW(0),
        HIGH(1);

        private final int value;

        State(int value) {
            this.value = value;
        }

        public static State valueOf(int value) {
            for (State st : State.values()) {
                if (st.value == value) {
                    return st;
                }
            }
            return null;
        }

        public int getValue() {
            return value;
        }
    }

    public enum Direction {
        INPUT,
        OUTPUT
    }
}
