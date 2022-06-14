package io.quarkiverse.jef.java.embedded.framework.runtime.serial;

import java.util.function.Supplier;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.SerialBusesConfig;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class SerialBusRecorder {

    public Supplier<SerialBusManager> getSerialBusManagerSupplier(SerialBusesConfig config) {
        return () -> new SerialBusManagerImpl(config);
    }

}
