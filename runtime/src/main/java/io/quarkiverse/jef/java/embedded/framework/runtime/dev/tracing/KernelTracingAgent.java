package io.quarkiverse.jef.java.embedded.framework.runtime.dev.tracing;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * All events
 * cat /sys/kernel/debug/tracing/available_events
 *
 * Debug:
 * echo 'spi:*' > /sys/kernel/debug/tracing/set_event
 *
 * Read:
 * less /sys/kernel/debug/tracing/trace
 */
@RegisterForReflection
public interface KernelTracingAgent {
    List<String> categories();

    List<String> events();

    String key();

    boolean enabled();

    boolean enable();
}
