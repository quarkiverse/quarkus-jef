package io.quarkiverse.jef.java.embedded.framework.runtime.dev.tracing;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OneWireAgent extends GPIOTracingAgent {
    @Override
    public String key() {
        return "w1_";
    }
}
