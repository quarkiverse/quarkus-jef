package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import java.util.Map;
import java.util.Objects;

import io.quarkus.runtime.annotations.*;

@ConfigRoot(name = "jef.gpio", phase = ConfigPhase.BUILD_TIME)
public class GPIOsConfig {
    /**
     * The default gpio bus.
     */
    @ConfigItem(name = ConfigItem.PARENT)
    public GPIOConfig defaultBus;

    /**
     * Additional named GPIOs.
     */
    @ConfigDocSection
    @ConfigDocMapKey("i2c-name")
    @ConfigItem(name = ConfigItem.PARENT)
    public Map<String, GPIOConfig> namedBuses;

    public GPIOConfig getRuntimeConfig(String name) {
        if ("<default>".equals(name)) {
            return defaultBus;
        }
        GPIOConfig cfg = namedBuses.get(name);
        return Objects.requireNonNullElseGet(cfg, GPIOConfig::new);
    }
}
