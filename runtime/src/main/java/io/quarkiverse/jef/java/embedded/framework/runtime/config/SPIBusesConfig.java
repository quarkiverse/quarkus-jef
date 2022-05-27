package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import io.quarkus.runtime.annotations.*;

import java.util.Map;
import java.util.Objects;

@ConfigRoot(name = "spi", phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
public class SPIBusesConfig {
    /**
     * The default spi bus.
     */
    @ConfigItem(name = ConfigItem.PARENT)
    public SPIBusConfig defaultBus;


    @ConfigDocSection
    @ConfigDocMapKey("i2c-name")
    @ConfigItem(name = ConfigItem.PARENT)
    public Map<String, SPIBusConfig> namedBuses;

    public SPIBusConfig getRuntimeConfig(String name) {
        if ("<default>".equals(name)) {
            return defaultBus;
        }
        SPIBusConfig cfg = namedBuses.get(name);
        return Objects.requireNonNullElseGet(cfg, SPIBusConfig::new);
    }
}
