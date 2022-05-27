package io.quarkiverse.jef.java.embedded.framework.runtime.i2c;

import io.quarkiverse.jef.java.embedded.framework.linux.i2c.I2CBus;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.lang.annotation.Annotation;

@ApplicationScoped
public class I2CBusProducer {
    @Inject
    I2CBusManager manager;

    @Produces
    //@I2C("") // The `value` attribute is @Nonbinding.
    I2CBus produce(InjectionPoint injectionPoint) {
        for (Annotation qualifier : injectionPoint.getQualifiers()) {
            if (qualifier instanceof I2C) {
                return manager.getBus(((I2C) qualifier).name());
            }
        }
        // This will never be returned.
        return null;
    }
}
