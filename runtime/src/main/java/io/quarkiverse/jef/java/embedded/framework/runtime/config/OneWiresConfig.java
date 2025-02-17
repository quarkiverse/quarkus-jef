package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import java.util.Map;

import io.quarkus.runtime.annotations.*;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithParentName;

@ConfigRoot(phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
@ConfigMapping(prefix = "quarkus.jef.onewire")
public interface OneWiresConfig {
    /**
     * Additional named one wires.
     */
    @ConfigDocSection
    @ConfigDocMapKey("onewire-name")
    @WithParentName
    Map<String, OneWireConfig> namedWires();
}
