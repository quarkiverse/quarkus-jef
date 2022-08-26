package io.quarkiverse.jef.java.embedded.framework.runtime.dev.tracing;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * i2c:i2c_result
 * i2c:i2c_reply
 * i2c:i2c_read
 * i2c:i2c_write
 * smbus:smbus_result
 * smbus:smbus_reply
 * smbus:smbus_read
 * smbus:smbus_write
 */
@RegisterForReflection
public class I2CTracingAgent extends AbstractTracingAgent {
    private final static List<String> categories = List.of("i2c", "smbus");
    private final static List<String> events = List.of(
            "i2c_result", "i2c_reply", "i2c_read", "i2c_write",
            "smbus_result", "smbus_reply", "smbus_read", "smbus_write");

    @Override
    public List<String> categories() {
        return categories;
    }

    @Override
    public List<String> events() {
        return events;
    }

    @Override
    public String key() {
        //TODO: review key
        return null;
    }
}
