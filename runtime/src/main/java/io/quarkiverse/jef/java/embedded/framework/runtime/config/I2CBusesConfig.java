package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import java.util.Map;
import java.util.Objects;

import io.quarkus.runtime.annotations.*;

@ConfigRoot(name = "jef.i2c", phase = ConfigPhase.BUILD_TIME)
public class I2CBusesConfig {
    /**
     * The default i2c bus.
     */
    @ConfigItem(name = ConfigItem.PARENT)
    public I2CBusConfig defaultBus;

    /**
     * Additional named I2Cs.
     */
    @ConfigDocSection
    @ConfigDocMapKey("i2c-name")
    @ConfigItem(name = ConfigItem.PARENT)
    public Map<String, I2CBusConfig> namedBuses;

    public I2CBusConfig getRuntimeConfig(String name) {
        if ("<default>".equals(name)) {
            return defaultBus;
        }
        I2CBusConfig cfg = namedBuses.get(name);
        return Objects.requireNonNullElseGet(cfg, I2CBusConfig::new);
    }

}
