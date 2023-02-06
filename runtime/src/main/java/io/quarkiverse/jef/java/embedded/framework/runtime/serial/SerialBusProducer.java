package io.quarkiverse.jef.java.embedded.framework.runtime.serial;

import java.lang.annotation.Annotation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.quarkiverse.jef.java.embedded.framework.linux.serial.SerialBus;

@SuppressWarnings("unused")
@ApplicationScoped
public class SerialBusProducer {
    private final static Logger logger = LogManager.getLogger(SerialBusProducer.class);

    @Inject
    SerialBusManager manager;

    @Produces
    @Serial(name = "")
    public SerialBus produce(InjectionPoint injectionPoint) {
        for (Annotation qualifier : injectionPoint.getQualifiers()) {
            if (qualifier instanceof Serial) {
                String name = ((Serial) qualifier).name();
                return manager.getBus(name);
            }
        }
        // This will never be returned.
        return null;
    }
}
