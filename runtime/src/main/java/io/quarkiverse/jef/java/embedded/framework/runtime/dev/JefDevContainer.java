package io.quarkiverse.jef.java.embedded.framework.runtime.dev;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.quarkiverse.jef.java.embedded.framework.linux.core.OneWireDevice;
import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiBus;
import io.quarkiverse.jef.java.embedded.framework.mcu.core.boards.Board;
import io.quarkiverse.jef.java.embedded.framework.mcu.core.boards.BoardManager;
import io.quarkiverse.jef.java.embedded.framework.runtime.gpio.GPIOManager;
import io.quarkiverse.jef.java.embedded.framework.runtime.i2c.I2CBusManager;
import io.quarkiverse.jef.java.embedded.framework.runtime.onewire.OneWireManager;
import io.quarkiverse.jef.java.embedded.framework.runtime.serial.SerialBusManager;
import io.quarkiverse.jef.java.embedded.framework.runtime.spi.SPIBusManager;
import io.quarkus.arc.Arc;
import io.quarkus.arc.InstanceHandle;

@SuppressWarnings("unused")
public class JefDevContainer {
    private final GPIOManager gpioManager;
    private final SerialBusManager serialManager;
    private final I2CBusManager i2cManager;
    private final SPIBusManager spiManager;
    private final OneWireManager wireManager;

    public JefDevContainer() {
        try (InstanceHandle<GPIOManager> h = Arc.container().instance(GPIOManager.class)) {
            gpioManager = h.get();
        }

        try (InstanceHandle<SerialBusManager> h = Arc.container().instance(SerialBusManager.class)) {
            serialManager = h.get();
        }

        try (InstanceHandle<I2CBusManager> h = Arc.container().instance(I2CBusManager.class)) {
            i2cManager = h.get();
        }

        try (InstanceHandle<SPIBusManager> h = Arc.container().instance(SPIBusManager.class)) {
            spiManager = h.get();
        }

        try (InstanceHandle<OneWireManager> h = Arc.container().instance(OneWireManager.class)) {
            wireManager = h.get();
        }
    }

    public int getGpioCount() {
        return gpioManager.getBuses().size();
    }

    public int getSerialCount() {
        return serialManager.getAll().size();
    }

    public int getI2cCount() {
        return i2cManager.getAll().size();
    }

    public int getSpiCount() {
        return spiManager.getAll().size();
    }

    public int getOneWireCount() {
        return wireManager.getAll().size();
    }

    public String getBoard() {
        try {
            Board board = BoardManager.getBoard();
            return board.getBoardInfo();
        } catch (IOException e) {
            return "Unknown";
        }
    }

    public I2CMapHolder getI2cMap() {
        return new I2CMapHolder(i2cManager);
    }

    public List<OneWireRecord> getOneWireRecords() {
        List<OneWireRecord> result = new ArrayList<>();

        Map<String, OneWireDevice> all = wireManager.getAll();
        for (Map.Entry<String, OneWireDevice> item : all.entrySet()) {
            result.add(new OneWireRecord(item.getKey(), item.getValue()));
        }

        return result;
    }

    public List<SpiRecord> getSpiRecords() {
        List<SpiRecord> result = new ArrayList<>();
        Map<String, SpiBus> all = spiManager.getAll();
        for (Map.Entry<String, SpiBus> item : all.entrySet()) {
            result.add(new SpiRecord(item.getKey(), item.getValue()));
        }
        return result;
    }

    public BoardHolder getBoardHolder() {
        try {
            Board board = BoardManager.getBoard();
            return new BoardHolder(board);
        } catch (IOException e) {
            return null;
        }
    }

}
