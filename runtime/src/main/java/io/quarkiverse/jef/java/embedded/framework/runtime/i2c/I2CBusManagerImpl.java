package io.quarkiverse.jef.java.embedded.framework.runtime.i2c;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.i2c.I2CBus;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.I2CBusConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.I2CBusesConfig;

import java.util.HashMap;
import java.util.Map;

//@ApplicationScoped
public class I2CBusManagerImpl implements I2CBusManager {
    private final Map<String, I2CBus> buses = new HashMap<>();
    public I2CBusManagerImpl(I2CBusesConfig cfg) {
        if(cfg.namedBuses.isEmpty()) {
            processBus("default", cfg.defaultBus);
        } else {
            for (Map.Entry<String, I2CBusConfig> entry : cfg.namedBuses.entrySet()) {
                I2CBusConfig config = entry.getValue();
                processBus(entry.getKey(), config);
            }
        }
    }

    private void processBus(String name, I2CBusConfig config) {
        if(config.enabled && config.path.isPresent()) {
            try {
                I2CBus bus = I2CBus.create(config.path.get());
                bus.setTenBits(config.isTenBits);

                if(config.retries.isPresent()) {
                    bus.setRetries(config.retries.getAsInt());
                }

                if(config.timeout.isPresent()) {
                    bus.setTimeout(config.timeout.getAsInt());
                }

                buses.put(name, bus);
            } catch (NativeIOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public I2CBus getBus(String value) {
        return buses.get(value);
    }
}
