package io.quarkiverse.jef.java.embedded.framework.runtime.serial;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.SerialBusesConfig;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class SerialBusRecorder {
    public RuntimeValue<SerialBusManager> create(SerialBusesConfig cfg) {
        return new RuntimeValue<>(new SerialBusManagerImpl(cfg));
    }
}
