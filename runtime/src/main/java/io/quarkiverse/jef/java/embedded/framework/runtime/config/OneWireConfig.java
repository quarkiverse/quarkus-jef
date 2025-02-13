package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;

@ConfigGroup
public interface OneWireConfig {
    /**
     * Enable one wire
     */
    @WithDefault("false")
    boolean enabled();

    /**
     * Path to one wire device like /sys/bus/w1/devices/28-01203882217a/w1_slave
     */
    Optional<String> path();
}
