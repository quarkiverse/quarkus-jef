package io.quarkiverse.jef.java.embedded.framework.it;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioPin;
import io.quarkiverse.jef.java.embedded.framework.runtime.gpio.GPIO;
import io.quarkiverse.jef.java.embedded.framework.runtime.gpio.GPIOManager;

@Path("/java-embedded-framework/gpio")
@ApplicationScoped
public class GPIOResource {
    @Inject
    GPIOManager manager;

    @GPIO(name = "<default>", number = 1)
    GpioPin defaultPin;

    @GPIO(name = "gpio1", number = 1)
    GpioPin gpioPin1;

    @GET
    @Path("manager")
    public String manager() {
        return "" + (manager != null);
    }

    @GET
    @Path("default")
    public String defaultBus() {
        if (defaultPin == null) {
            return null;
        }
        return defaultPin.getName() + "/" +
                defaultPin.getConsumer() + "/" +
                defaultPin.getDirection() + "/" +
                defaultPin.getFlags();
    }

    @GET
    @Path("pin1")
    public String serial1() {
        if (gpioPin1 == null) {
            return null;
        }
        return gpioPin1.getName() + "/" +
                gpioPin1.getConsumer() + "/" +
                gpioPin1.getDirection() + "/" +
                gpioPin1.getFlags();
    }
}
