package io.quarkiverse.jef.java.embedded.framework.runtime.gpio;

import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioPin;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.lang.annotation.Annotation;

@ApplicationScoped
public class GPIOProducer {
    @Inject
    GPIOManager manager;

    @Produces
        //@SPI("") // The `value` attribute is @Nonbinding.
    GpioPin produce(InjectionPoint injectionPoint) {
        for (Annotation qualifier : injectionPoint.getQualifiers()) {
            if (qualifier instanceof GPIO) {
                GPIO gpio = (GPIO) qualifier;
                return manager.getPin(gpio.name(), gpio.number());
            }
        }
        // This will never be returned.
        return null;
    }
}
