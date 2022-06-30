package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class OneWireConfig {
    /**
     * Enable one wire
     */
    @ConfigItem(defaultValue = "false")
    public boolean enabled;

    /**
     * Path to one wire device like /sys/bus/w1/devices/28-01203882217a/w1_slave
     */
    @ConfigItem
    public Optional<String> path;
}
