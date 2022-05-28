
package io.quarkiverse.jef.java.embedded.framework.linux.gpio;

import java.io.IOException;

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
