package io.quarkiverse.jef.java.embedded.framework.runtime.onewire;

import java.util.function.Supplier;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.OneWiresConfig;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class OneWireRecorder {
    public Supplier<OneWireManager> getOneWireManagerSupplier(OneWiresConfig config) {
        return () -> new OneWireManagerImpl(config);
    }
}
