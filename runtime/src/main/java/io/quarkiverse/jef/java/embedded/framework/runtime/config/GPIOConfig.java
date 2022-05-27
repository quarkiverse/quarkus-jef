package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

import java.util.Optional;

@ConfigGroup
public class GPIOConfig {
    /**
     * Enable bus
     */
    @ConfigItem(defaultValue = "false")
    public boolean enabled;

    @ConfigItem
    public Optional<String> path;

}
