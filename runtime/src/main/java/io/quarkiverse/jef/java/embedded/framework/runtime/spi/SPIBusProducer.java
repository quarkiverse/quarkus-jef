package io.quarkiverse.jef.java.embedded.framework.runtime.spi;

import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiBus;
import io.quarkiverse.jef.java.embedded.framework.linux.spi.SpiBusImpl;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.lang.annotation.Annotation;

@ApplicationScoped
public class SPIBusProducer {
    @Inject
    SPIBusManager manager;

    @Produces
    //@SPI("") // The `value` attribute is @Nonbinding.
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
