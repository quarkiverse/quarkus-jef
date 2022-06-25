package io.quarkiverse.jef.java.embedded.framework.runtime.onewire;

import io.quarkiverse.jef.java.embedded.framework.linux.core.OneWireDevice;

public interface OneWireManager {
    OneWireDevice getDevice(String name);
}
