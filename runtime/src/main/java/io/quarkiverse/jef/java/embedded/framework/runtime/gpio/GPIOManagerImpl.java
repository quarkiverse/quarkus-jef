package io.quarkiverse.jef.java.embedded.framework.runtime.gpio;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioChipInfo;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioManager;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioPin;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.GPIOConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.GPIOsConfig;

public class GPIOManagerImpl implements GPIOManager {
    private final static Logger logger = LogManager.getLogger(GPIOManagerImpl.class);
    private final Map<String, String> buses = new HashMap<>();

    public GPIOManagerImpl(GPIOsConfig cfg) {
        logger.debug("Create GPIOManagerImpl");

        if (cfg.defaultBus != null) {
            logger.debug("Default bus found");
            processBus("<default>", cfg.defaultBus);
        }

        for (Map.Entry<String, GPIOConfig> entry : cfg.namedBuses.entrySet()) {
            GPIOConfig config = entry.getValue();
            processBus(entry.getKey(), config);
        }
    }

    private void processBus(String name, GPIOConfig bus) {
        if (bus.enabled && bus.path.isPresent()) {
            logger.debug("add GPIO bus to list: {}", name);
            buses.put(name, bus.path.get());
        }
    }

    @Override
    public GpioChipInfo getChipInfo(String path) throws NativeIOException {
        return GpioManager.getChipInfo(path);
    }

    @Override
    public GpioPin getPin(String name, int number) {
        logger.debug("Get pin({}) from bus {}", number, name);
        String path = buses.get(name);
        if (path != null) {
            try {
                return GpioManager.getPin(path, number);
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    @Override
    public Map<String, String> getBuses() {
        return buses;
    }
}
