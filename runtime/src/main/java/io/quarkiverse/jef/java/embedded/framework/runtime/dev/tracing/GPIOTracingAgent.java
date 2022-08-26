package io.quarkiverse.jef.java.embedded.framework.runtime.dev.tracing;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * // cat /sys/kernel/debug/gpio ???
 * gpio:gpio_value
 * gpio:gpio_direction
 */
@RegisterForReflection
public class GPIOTracingAgent extends AbstractTracingAgent {
    private final static List<String> categories = List.of("categories");
    private final static List<String> events = List.of("gpio:gpio_value", "gpio:gpio_direction");

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
        return "gpio_value";
    }
}
