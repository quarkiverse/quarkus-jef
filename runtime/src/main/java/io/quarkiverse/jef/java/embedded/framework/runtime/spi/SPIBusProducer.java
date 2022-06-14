package io.quarkiverse.jef.java.embedded.framework.runtime.spi;

import java.lang.annotation.Annotation;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiBus;

@SuppressWarnings("unused")
@ApplicationScoped
public class SPIBusProducer {
    @Inject
    SPIBusManager manager;

    @Produces
    @SPI(name = "") // The `value` attribute is @Nonbinding.
    SpiBus produce(InjectionPoint injectionPoint) {
        for (Annotation qualifier : injectionPoint.getQualifiers()) {
            if (qualifier instanceof SPI) {
                return manager.getBus(((SPI) qualifier).name());
            }
        }
        // This will never be returned.
        return null;
    }

}
