package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;

@ConfigGroup
public interface I2CBusConfig {
    /**
     * Enable bus
     */
    @WithDefault("false")
    boolean enabled();

    /**
     * Path to I2C bus i.e /dev/i2c-1
     */
    Optional<String> path();

    /**
     * Is 8 or 10 bits bus. Default: 8 bits
     */
    @WithDefault("false")
    boolean tenBits();

    /**
     * Amount of bus reties
     */
    @WithDefault("-1")
    int retries();

    /**
     * Timeout ms
     */
    @WithDefault("-1")
    int timeout();

}
