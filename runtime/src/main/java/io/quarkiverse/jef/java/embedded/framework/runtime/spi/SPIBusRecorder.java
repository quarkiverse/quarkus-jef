package io.quarkiverse.jef.java.embedded.framework.runtime.spi;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.I2CBusesConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.SPIBusesConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.i2c.I2CBusManager;
import io.quarkiverse.jef.java.embedded.framework.runtime.i2c.I2CBusManagerImpl;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class SPIBusRecorder {
    public RuntimeValue<SPIBusManager> create(SPIBusesConfig cfg) {
        return new RuntimeValue<>(new SPIBusManagerImpl(cfg));
    }
}
