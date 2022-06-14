package io.quarkiverse.jef.java.embedded.framework.it;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.quarkiverse.jef.java.embedded.framework.linux.i2c.I2CBus;
import io.quarkiverse.jef.java.embedded.framework.runtime.i2c.I2C;
import io.quarkiverse.jef.java.embedded.framework.runtime.i2c.I2CBusManager;

@Path("/java-embedded-framework/i2c")
@ApplicationScoped
public class I2CBusResource {
    @Inject
    I2CBusManager manager;

    @I2C(name = "<default>")
    I2CBus defaultBus;

    @I2C(name = "i2c1")
    I2CBus i2c1;

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
        return defaultBus.getPath() + "/" +
                defaultBus.isTenBits() + "/" +
                defaultBus.getRetries() + "/" +
                defaultBus.getTimeout();
    }

    @GET
    @Path("i2c1")
    public String serial1() {
        if (i2c1 == null) {
            return null;
        }
        return i2c1.getPath() + "/" +
                i2c1.isTenBits() + "/" +
                i2c1.getRetries() + "/" +
                i2c1.getTimeout();
    }
}
