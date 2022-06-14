package io.quarkiverse.jef.java.embedded.framework.runtime.gpio;

import java.util.function.Supplier;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.GPIOsConfig;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class GPIORecorder {
    public Supplier<GPIOManager> getGPIOManagerSupplier(GPIOsConfig config) {
        return () -> new GPIOManagerImpl(config);
    }
}
