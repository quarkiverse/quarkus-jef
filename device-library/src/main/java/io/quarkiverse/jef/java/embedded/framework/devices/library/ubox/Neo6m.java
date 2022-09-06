package io.quarkiverse.jef.java.embedded.framework.devices.library.ubox;

import java.io.IOException;

import io.quarkiverse.jef.java.embedded.framework.linux.serial.SerialBus;
import io.quarkiverse.jef.java.embedded.framework.tools.nmea0183.Nmea0183;
import io.quarkiverse.jef.java.embedded.framework.tools.nmea0183.Nmea0183Listener;

public class Neo6m {
    private final SerialBus bus;
    private final Nmea0183Listener listener;
    private final Nmea0183 nmea;

    public Neo6m(SerialBus bus, Nmea0183Listener listener) throws IOException {
        this.bus = bus;
        this.listener = listener;
        this.nmea = new Nmea0183(bus.getInputStream(), listener);
        nmea.start();
    }

    public SerialBus getBus() {
        return bus;
    }

}
