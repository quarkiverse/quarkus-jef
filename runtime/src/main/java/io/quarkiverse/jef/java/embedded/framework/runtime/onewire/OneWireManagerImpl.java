package io.quarkiverse.jef.java.embedded.framework.runtime.onewire;

import java.util.HashMap;
import java.util.Map;

import io.quarkiverse.jef.java.embedded.framework.linux.core.OneWireDevice;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.OneWireConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.OneWiresConfig;

public class OneWireManagerImpl implements OneWireManager {
    private final Map<String, OneWireDevice> devices = new HashMap<>();

    public OneWireManagerImpl(OneWiresConfig cfg) {
        for (Map.Entry<String, OneWireConfig> entry : cfg.namedWires().entrySet()) {
            OneWireConfig config = entry.getValue();
            processWire(entry.getKey(), config);
        }
    }

    private void processWire(String key, OneWireConfig config) {
        if (config.enabled() && config.path().isPresent()) {
            String path = config.path().get();
            devices.put(key, new OneWireDevice(path));
        }
    }

    @Override
    public Map<String, OneWireDevice> getAll() {
        return devices;
    }

    @Override
    public OneWireDevice getDevice(String name) {
        return devices.get(name);
    }
}
