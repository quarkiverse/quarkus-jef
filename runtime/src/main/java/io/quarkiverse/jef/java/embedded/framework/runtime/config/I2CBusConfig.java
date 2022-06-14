package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

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
    @ConfigItem(defaultValue = "-1")
    public Integer retries;

    /**
     * Timeout ms
     */
    @ConfigItem(defaultValue = "-1")
    public Integer timeout;

}
