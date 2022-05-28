package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import java.util.Optional;

import io.quarkiverse.jef.java.embedded.framework.linux.serial.SerialBaudRate;
import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConvertWith;

@ConfigGroup
public class SerialBusConfig {
    /**
     * Enable bus
     */
    @ConfigItem(defaultValue = "false")
    public boolean enabled;

    /**
     * Path to I2C bus i.e /dev/something
     */
    @ConfigItem
    public Optional<String> path;

    /**
     * Port speed
     */
    @ConfigItem(name = "baud-rate", defaultValue = "B9600")
    @ConvertWith(SerialBaudRateConverter.class)
    public SerialBaudRate baudRate;

}
