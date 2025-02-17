package io.quarkiverse.jef.java.embedded.framework.runtime.spi;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiBus;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.SPIBusConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.SPIBusesConfig;

public class SPIBusManagerImpl implements SPIBusManager {
    private final static Logger logger = LogManager.getLogger("JEF-Dev-Tools");
    private final Map<String, SpiBus> buses = new HashMap<>();

    public SPIBusManagerImpl(SPIBusesConfig cfg) {
        for (Map.Entry<String, SPIBusConfig> item : cfg.buses().entrySet()) {
            SPIBusConfig config = item.getValue();
            processBus(item.getKey(), config);
        }
    }

    private void processBus(String name, SPIBusConfig config) {
        if (config.enabled() && config.path().isPresent()) {
            try {
                String path = config.path().get();
                SpiBus bus = config.workMode() == SPIBusConfig.SpiWorkMode.FULL_DUPLEX ? SpiBus.createFullDuplex(path)
                        : SpiBus.createHalfDuplex(path);
                bus.setClockFrequency(config.clockFrequency());
                bus.setClockMode(config.spiMode());
                bus.setBitOrdering(config.bitOrdering().getValue());
                bus.setWordLength(config.wordLength());
                buses.put(name, bus);
            } catch (NativeIOException e) {
                logger.error(e);
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
