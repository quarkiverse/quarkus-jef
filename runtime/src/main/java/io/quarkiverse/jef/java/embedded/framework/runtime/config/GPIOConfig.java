package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;

@ConfigGroup
public interface GPIOConfig {
    /**
     * Enable bus
     */
    @WithDefault("false")
    boolean enabled();

    /**
     * Path to /dev/gpio*
     */
    Optional<String> path();

}
