package io.quarkiverse.jef.java.embedded.framework.runtime.dev;

import java.util.function.Supplier;

import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class DevConsoleRecorder {
    public Supplier<JefDevContainer> getSupplier() {
        return new JefContainerSupplier();
    }
}
