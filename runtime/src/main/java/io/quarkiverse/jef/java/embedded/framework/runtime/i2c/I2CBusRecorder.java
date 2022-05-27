package io.quarkiverse.jef.java.embedded.framework.runtime.i2c;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.I2CBusesConfig;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class I2CBusRecorder {
    public RuntimeValue<I2CBusManager> create(I2CBusesConfig cfg) {
        return new RuntimeValue<>(new I2CBusManagerImpl(cfg));
    }
}
