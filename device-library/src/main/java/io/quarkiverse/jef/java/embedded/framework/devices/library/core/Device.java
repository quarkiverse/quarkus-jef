package io.quarkiverse.jef.java.embedded.framework.devices.library.core;

import java.util.Map;

public interface Device {
    Map<String, Property> getUserProperties();

    Map<String, Property> getSystemProperties();
}
