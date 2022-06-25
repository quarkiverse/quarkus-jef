package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import io.quarkus.runtime.annotations.*;

import java.util.Map;
import java.util.Optional;

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
