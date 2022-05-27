package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

import java.util.Optional;
import java.util.OptionalInt;

@ConfigGroup
public class I2CBusConfig {
    /**
     * Enable bus
     */
    @ConfigItem(defaultValue = "false")
    public boolean enabled;

    /**
     * Path to I2C bus i.e /dev/i2c-1
     */
    @ConfigItem
    public Optional<String> path;

    /**
     * Is 8 or 10 bits bus. Default: 8 bits
     */
    @ConfigItem(name = "ten-bits", defaultValue = "false")
    public boolean isTenBits;

    /**
     * Amount of bus reties
     */
    @ConfigItem
    public OptionalInt retries;

    /**
     * Timeout ms
     */
    @ConfigItem
    public OptionalInt timeout;

}
