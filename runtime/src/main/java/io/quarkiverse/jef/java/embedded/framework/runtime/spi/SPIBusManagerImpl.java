package io.quarkiverse.jef.java.embedded.framework.runtime.spi;

import java.util.HashMap;
import java.util.Map;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiBus;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.SPIBusConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.SPIBusesConfig;

public class SPIBusManagerImpl implements SPIBusManager {
    private final Map<String, SpiBus> buses = new HashMap<>();

    public SPIBusManagerImpl(SPIBusesConfig cfg) {
        if (cfg.defaultBus != null) {
            processBus("<default>", cfg.defaultBus);
        }

        for (Map.Entry<String, SPIBusConfig> item : cfg.namedBuses.entrySet()) {
            SPIBusConfig config = item.getValue();
            processBus(item.getKey(), config);
        }
    }

    private void processBus(String name, SPIBusConfig config) {
        if (config.enabled && config.path.isPresent()) {
            try {
                SpiBus bus = SpiBus.create(config.path.get());
                bus.setClockFrequency(config.clockFrequency);
                bus.setClockMode(config.spiMode);
                bus.setBitOrdering(config.bitOrdering.getValue());
                bus.setWordLength(config.wordLength);
                buses.put(name, bus);
            } catch (NativeIOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Map<String, SpiBus> getAll() {
        return buses;
    }

    @Override
    public SpiBus getBus(String name) {
        return buses.get(name);
    }
}
