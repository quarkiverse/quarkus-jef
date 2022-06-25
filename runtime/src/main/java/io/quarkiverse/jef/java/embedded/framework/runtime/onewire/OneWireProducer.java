package io.quarkiverse.jef.java.embedded.framework.runtime.onewire;

import io.quarkiverse.jef.java.embedded.framework.linux.core.OneWireDevice;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.lang.annotation.Annotation;

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
