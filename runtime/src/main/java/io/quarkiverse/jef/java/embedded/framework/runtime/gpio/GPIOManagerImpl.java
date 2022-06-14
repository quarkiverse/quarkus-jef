package io.quarkiverse.jef.java.embedded.framework.runtime.gpio;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioManager;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioPin;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.GPIOConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.GPIOsConfig;

public class GPIOManagerImpl implements GPIOManager {
    private final Map<String, String> buses = new HashMap<>();

    public GPIOManagerImpl(GPIOsConfig cfg) {
        if (cfg.defaultBus != null) {
            processBus("<default>", cfg.defaultBus);
        }

        for (Map.Entry<String, GPIOConfig> entry : cfg.namedBuses.entrySet()) {
            GPIOConfig config = entry.getValue();
            processBus(entry.getKey(), config);
        }
    }

    private void processBus(String name, GPIOConfig bus) {
        if (bus.enabled && bus.path.isPresent()) {
            buses.put(name, bus.path.get());
        }
    }

    @Override
    public GpioPin getPin(String name, int number) {
        String path = buses.get(name);
        if (path != null) {
            try {
                return GpioManager.getPin(path, number);
            } catch (IOException ignored) {
            }
        }
        return null;
    }
}
