package io.quarkiverse.jef.java.embedded.framework.devices.library.core;

public interface DeviceFactory {
    String getVendor();

    String getVendorIcon();

    String getType();

    String getTypeIcon();

    String getDeviceName();

    String getDeviceIcon();

    String getDeviceDescription();

    String getDatasheet();

    Device createDevice();
}
