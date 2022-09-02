package io.quarkiverse.jef.java.embedded.framework.devices.library.ubox;

import io.quarkiverse.jef.java.embedded.framework.linux.serial.SerialBus;

public class Neo6m {
    private final SerialBus bus;

    public Neo6m(SerialBus bus) {
        this.bus = bus;
    }

    public SerialBus getBus() {
        return bus;
    }

}
