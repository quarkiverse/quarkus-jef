package io.quarkiverse.jef.java.embedded.framework.runtime.serial;

import io.quarkiverse.jef.java.embedded.framework.linux.serial.SerialBus;

public interface SerialBusManager {
    SerialBus getBus(String name);
}
