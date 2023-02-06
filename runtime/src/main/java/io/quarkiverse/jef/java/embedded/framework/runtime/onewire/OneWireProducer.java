package io.quarkiverse.jef.java.embedded.framework.runtime.onewire;

import java.lang.annotation.Annotation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;

import io.quarkiverse.jef.java.embedded.framework.linux.core.OneWireDevice;

@SuppressWarnings("unused")
@ApplicationScoped
public class OneWireProducer {
    @Inject
    OneWireManager manager;

    @Produces
    @OneWire(name = "")
    OneWireDevice produce(InjectionPoint injectionPoint) {
        for (Annotation qualifier : injectionPoint.getQualifiers()) {
            if (qualifier instanceof OneWire) {
                OneWire wire = (OneWire) qualifier;
                return manager.getDevice(wire.name());
            }
        }
        // This will never be returned.
        return null;
    }
}
