package io.quarkiverse.jef.java.embedded.framework.runtime.spi;

import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiBus;

public interface SPIBusManager {
    SpiBus getBus(String name);
}
