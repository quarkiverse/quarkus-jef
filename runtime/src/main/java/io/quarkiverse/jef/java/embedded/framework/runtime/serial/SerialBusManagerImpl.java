package io.quarkiverse.jef.java.embedded.framework.runtime.serial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.serial.SerialBus;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.SerialBusConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.SerialBusesConfig;

public class SerialBusManagerImpl implements SerialBusManager {
    private final static Logger logger = LogManager.getLogger("JEF-Dev-Tools");
    private final Map<String, SerialBus> buses = new HashMap<>();

    public SerialBusManagerImpl(SerialBusesConfig cfg) {
        if (cfg.defaultBus != null) {
            processBus("<default>", cfg.defaultBus);
        }

        for (Map.Entry<String, SerialBusConfig> entry : cfg.namedBuses.entrySet()) {
            SerialBusConfig config = entry.getValue();
            processBus(entry.getKey(), config);
        }
    }

    @Override
    public List<SerialBus> getAll() {
        return new ArrayList<>(buses.values());
    }

    private void processBus(String name, SerialBusConfig config) {
        if (config.enabled && config.path.isPresent()) {
            try {
                buses.put(name, SerialBus.create(config.path.get(), config.baudRate));
            } catch (NativeIOException e) {
                logger.error(e);
            }
        }
    }

    @Override
    public SerialBus getBus(String name) {
        return buses.get(name);
    }
}
