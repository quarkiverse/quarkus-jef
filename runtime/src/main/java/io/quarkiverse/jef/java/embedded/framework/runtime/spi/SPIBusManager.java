package io.quarkiverse.jef.java.embedded.framework.runtime.spi;

import java.util.Map;

import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiBus;

public interface SPIBusManager {
    SpiBus getBus(String name);

    Map<String, SpiBus> getAll();
}
