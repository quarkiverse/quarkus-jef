package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import java.util.Optional;

import io.quarkiverse.jef.java.embedded.framework.linux.serial.SerialBaudRate;
import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithConverter;
import io.smallrye.config.WithDefault;

@ConfigGroup
public interface SerialBusConfig {
    /**
     * Enable bus
     */
    @WithDefault("false")
    public boolean enabled();

    /**
     * Path to I2C bus i.e /dev/something
     */
    public Optional<String> path();

    /**
     * Port speed
     */
    @WithDefault("B9600")
    @WithConverter(SerialBaudRateConverter.class)
    public SerialBaudRate baudRate();

}
