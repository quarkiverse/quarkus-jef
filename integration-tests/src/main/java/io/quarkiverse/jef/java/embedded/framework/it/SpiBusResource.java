package io.quarkiverse.jef.java.embedded.framework.it;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiBus;
import io.quarkiverse.jef.java.embedded.framework.runtime.serial.SerialBusManager;
import io.quarkiverse.jef.java.embedded.framework.runtime.spi.SPI;

@Path("/java-embedded-framework/spi")
@ApplicationScoped
public class SpiBusResource {
    @Inject
    SerialBusManager manager;

    @SPI(name = "<default>")
    SpiBus defaultBus;

    @SPI(name = "spi1")
    SpiBus spi1;

    @GET
    @Path("manager")
    public String manager() {
        return "" + (manager != null);
    }

    @GET
    @Path("default")
    public String defaultBus() throws Exception {
        if (defaultBus == null) {
            return null;
        }
        return defaultBus.getBus() + "/" +
                defaultBus.getClockFrequency() + "/" +
                defaultBus.getClockMode() + "/" +
                defaultBus.getWordLength() + "/" +
                defaultBus.getBitOrdering();
    }

    @GET
    @Path("spi1")
    public String spi1() throws Exception {
        if (spi1 == null) {
            return null;
        }
        return spi1.getBus() + "/" +
                spi1.getClockFrequency() + "/" +
                spi1.getClockMode() + "/" +
                spi1.getWordLength() + "/" +
                spi1.getBitOrdering();
    }
}
