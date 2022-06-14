package io.quarkiverse.jef.java.embedded.framework.it;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.quarkiverse.jef.java.embedded.framework.linux.serial.SerialBus;
import io.quarkiverse.jef.java.embedded.framework.runtime.serial.Serial;
import io.quarkiverse.jef.java.embedded.framework.runtime.serial.SerialBusManager;

@Path("/java-embedded-framework/serial")
@ApplicationScoped
public class SerialBusResource {
    @Inject
    SerialBusManager manager;

    @Serial(name = "<default>")
    SerialBus defaultBus;

    @Serial(name = "serial1")
    SerialBus serial1;

    @GET
    @Path("manager")
    public String manager() {
        return "" + (manager != null);
    }

    @GET
    @Path("default")
    public String defaultBus() {
        if (defaultBus == null) {
            return null;
        }
        return defaultBus.path() + "/" + defaultBus.serialBaudRate();
    }

    @GET
    @Path("serial1")
    public String serial1() {
        if (serial1 == null) {
            return null;
        }
        return serial1.path() + "/" + serial1.serialBaudRate();
    }
}
