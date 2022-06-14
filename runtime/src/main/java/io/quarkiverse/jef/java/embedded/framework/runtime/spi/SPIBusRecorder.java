package io.quarkiverse.jef.java.embedded.framework.runtime.spi;

import java.util.function.Supplier;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.SPIBusesConfig;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class SPIBusRecorder {
    public RuntimeValue<SPIBusManager> create(SPIBusesConfig cfg) {
        return new RuntimeValue<>(new SPIBusManagerImpl(cfg));
    }

    public Supplier<?> getSpiBusManagerSupplier(SPIBusesConfig config) {
        return () -> new SPIBusManagerImpl(config);
    }
}
