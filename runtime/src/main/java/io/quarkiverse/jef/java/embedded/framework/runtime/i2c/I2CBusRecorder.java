package io.quarkiverse.jef.java.embedded.framework.runtime.i2c;

import java.util.function.Supplier;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.I2CBusesConfig;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class I2CBusRecorder {
    public Supplier<I2CBusManager> getI2CBusManagerSupplier(I2CBusesConfig config) {
        return () -> new I2CBusManagerImpl(config);
    }
}
