package io.quarkiverse.jef.java.embedded.framework.mcu.core.boards.rpi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.quarkiverse.jef.java.embedded.framework.linux.i2c.I2CBus;
import io.quarkiverse.jef.java.embedded.framework.linux.i2c.I2CBusImpl;
import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiBus;
import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiBusImpl;
import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiMode;
import io.quarkiverse.jef.java.embedded.framework.mcu.core.boards.BoardPin;

class RaspberryPi40PinsBoard extends RaspberryPiAbstractBoard {
    public RaspberryPi40PinsBoard() throws IOException {
        super();
    }

    @Override
    protected List<BoardPin> initGPIO() throws IOException {
        return RaspberryPi40Pins.createPins();
    }

    @Override
    protected List<I2CBus> initI2C() throws IOException {
        final List<I2CBus> i2cs = new ArrayList<>();
        if (new File("/dev/i2c-1").exists()) {
            i2cs.add(
                    new I2CBusImpl("/dev/i2c-1"));
        }
        return Collections.unmodifiableList(i2cs);
    }

    protected List<SpiBus> initSPI() throws IOException {
        List<SpiBus> ss = new ArrayList<>();
        if (new File("/dev/spidev0.0").exists()) {
            ss.add(
                    new SpiBusImpl("/dev/spidev0.0", 500000, SpiMode.SPI_MODE_3, 8, 0));
        }

        if (new File("/dev/spidev0.1").exists()) {
            ss.add(
                    new SpiBusImpl("/dev/spidev0.1", 500000, SpiMode.SPI_MODE_3, 8, 0));
        }

        return Collections.unmodifiableList(ss);
    }
}
