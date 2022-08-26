package io.quarkiverse.jef.java.embedded.framework.runtime.dev.tracing;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * xhci-hcd: ??? check hint
 */
@RegisterForReflection
public class SerialTracer extends AbstractTracingAgent {
    @Override
    public List<String> categories() {
        return null;
    }

    @Override
    public List<String> events() {
        return null;
    }

    @Override
    public String key() {
        return null;
    }
}
