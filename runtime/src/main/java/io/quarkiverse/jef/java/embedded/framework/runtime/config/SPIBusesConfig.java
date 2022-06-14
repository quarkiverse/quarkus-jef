package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import java.util.Map;
import java.util.Objects;

import io.quarkus.runtime.annotations.*;

@ConfigRoot(name = "jef.spi", phase = ConfigPhase.BUILD_TIME)
public class SPIBusesConfig {
    /**
     * The default spi bus.
     */
    @ConfigItem(name = ConfigItem.PARENT)
    public SPIBusConfig defaultBus;

    /**
     * Additional named SPIs.
     */
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
