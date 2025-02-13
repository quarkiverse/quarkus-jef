package io.quarkiverse.jef.java.embedded.framework.runtime.config;

import java.util.Map;

import io.quarkus.runtime.annotations.ConfigDocMapKey;
import io.quarkus.runtime.annotations.ConfigDocSection;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefaults;
import io.smallrye.config.WithParentName;
import io.smallrye.config.WithUnnamedKey;

@ConfigRoot(phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
@ConfigMapping(prefix = "quarkus.jef.gpio")
public interface GPIOsConfig {

    /**
     * GPIO buses.
     */
    @ConfigDocSection
    @ConfigDocMapKey("i2c-name")
    @WithParentName
    @WithDefaults
    @WithUnnamedKey(ConfigConstants.DEFAULT_NAME)
    Map<String, GPIOConfig> buses();
}
