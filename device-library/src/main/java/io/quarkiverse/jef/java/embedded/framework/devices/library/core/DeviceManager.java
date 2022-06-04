package io.quarkiverse.jef.java.embedded.framework.devices.library.core;

import java.util.Iterator;
import java.util.ServiceLoader;

public class DeviceManager {

    public static Iterator<DeviceFactory> getDevices() {
        ServiceLoader<DeviceFactory> serviceLoader = ServiceLoader.load(DeviceFactory.class);
        return serviceLoader.iterator();
    }
}
