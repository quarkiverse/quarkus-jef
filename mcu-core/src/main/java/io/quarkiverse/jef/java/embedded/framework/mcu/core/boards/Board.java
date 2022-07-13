package io.quarkiverse.jef.java.embedded.framework.mcu.core.boards;

import java.util.List;

import io.quarkiverse.jef.java.embedded.framework.linux.i2c.I2CBus;
import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiBus;

@SuppressWarnings("unused")
public abstract class Board {
    public abstract String getBoardInfo();

    public abstract int getPinCount();

    public abstract BoardPin getPin(int index);

    public abstract List<BoardPin> getPins();

    public abstract List<SpiBus> getSpiBuses();

    public abstract List<I2CBus> getI2CBuses();
}
