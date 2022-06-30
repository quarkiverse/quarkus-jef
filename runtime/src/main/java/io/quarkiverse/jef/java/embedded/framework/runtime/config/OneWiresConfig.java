package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import java.util.Map;

import io.quarkus.runtime.annotations.*;

@ConfigRoot(name = "jef.onewire", phase = ConfigPhase.BUILD_TIME)
public class OneWiresConfig {
    /**
     * Additional named one wires.
     */
    @ConfigDocSection
    @ConfigDocMapKey("onewire-name")
    @ConfigItem(name = ConfigItem.PARENT)
    public Map<String, OneWireConfig> namedWires;
}
