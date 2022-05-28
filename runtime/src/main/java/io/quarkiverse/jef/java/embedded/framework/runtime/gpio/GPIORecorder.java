package io.quarkiverse.jef.java.embedded.framework.runtime.gpio;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.GPIOsConfig;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class GPIORecorder {
    public RuntimeValue<GPIOManager> create(GPIOsConfig cfg) {
        return new RuntimeValue<>(new GPIOManagerImpl(cfg));
    }
}
