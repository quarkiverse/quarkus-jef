package io.quarkiverse.jef.java.embedded.framework.runtime.serial;

import java.util.List;

import io.quarkiverse.jef.java.embedded.framework.linux.serial.SerialBus;

public interface SerialBusManager {
    SerialBus getBus(String name);

    List<SerialBus> getAll();
}
