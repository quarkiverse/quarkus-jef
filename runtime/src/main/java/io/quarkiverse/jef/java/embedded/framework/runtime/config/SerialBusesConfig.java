package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import java.util.Map;
import java.util.Objects;

import io.quarkus.runtime.annotations.*;

@ConfigRoot(name = "jef.serial", phase = ConfigPhase.BUILD_TIME)
public class SerialBusesConfig {
    /**
     * The default serial bus.
     */
    @ConfigItem(name = ConfigItem.PARENT)
    public SerialBusConfig defaultBus;

    /**
     * Additional named Serials.
     */
    @ConfigDocSection
    @ConfigDocMapKey("i2c-name")
    @ConfigItem(name = ConfigItem.PARENT)
    public Map<String, SerialBusConfig> namedBuses;

    public SerialBusConfig getRuntimeConfig(String name) {
        if ("<default>".equals(name)) {
            return defaultBus;
        }
        SerialBusConfig cfg = namedBuses.get(name);
        return Objects.requireNonNullElseGet(cfg, SerialBusConfig::new);
    }

    @Override
    public String toString() {
        return "SerialBusesConfig{" +
                "defaultBus=" + defaultBus +
                ", namedBuses=" + namedBuses +
                '}';
    }
}
