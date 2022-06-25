package io.quarkiverse.jef.java.embedded.framework.runtime.onewire;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.OneWiresConfig;
import io.quarkus.runtime.annotations.Recorder;

import java.util.function.Supplier;

@Recorder
public class OneWireRecorder {
    public Supplier<OneWireManager> getOneWireManagerSupplier(OneWiresConfig config) {
        return () -> new OneWireManagerImpl(config);
    }
}
