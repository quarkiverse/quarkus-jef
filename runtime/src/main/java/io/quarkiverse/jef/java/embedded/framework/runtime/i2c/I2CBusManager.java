package io.quarkiverse.jef.java.embedded.framework.runtime.i2c;

import java.util.Map;

import io.quarkiverse.jef.java.embedded.framework.linux.i2c.I2CBus;

//@ApplicationScoped
public interface I2CBusManager {
    I2CBus getBus(String name);

    Map<String, I2CBus> getAll();
}
