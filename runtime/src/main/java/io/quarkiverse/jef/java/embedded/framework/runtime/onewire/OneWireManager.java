package io.quarkiverse.jef.java.embedded.framework.runtime.onewire;

import java.util.Map;

import io.quarkiverse.jef.java.embedded.framework.linux.core.OneWireDevice;

public interface OneWireManager {
    OneWireDevice getDevice(String name);

    Map<String, OneWireDevice> getAll();
}
