package io.quarkiverse.jef.java.embedded.framework.runtime.dev.tracing;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * spi:spi_transfer_stop
 * spi:spi_transfer_start
 * spi:spi_message_done
 * spi:spi_message_start
 * spi:spi_message_submit
 * spi:spi_set_cs
 * spi:spi_setup
 * spi:spi_controller_busy
 * spi:spi_controller_idle
 */
@RegisterForReflection
public class SpiTracingAgent extends AbstractTracingAgent {
    private final static List<String> categories = List.of("spi");
    private final static List<String> events = List.of(
            "spi_transfer_stop", "spi_transfer_start",
            "spi_message_done", "spi_message_start", "spi_message_submit",
            "spi_set_cs", "spi_setup", "spi_controller_busy", "spi_controller_idle");

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
        return null;
    }
}
