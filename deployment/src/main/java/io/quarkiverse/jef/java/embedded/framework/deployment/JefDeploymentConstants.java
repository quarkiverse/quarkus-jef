package io.quarkiverse.jef.java.embedded.framework.deployment;

import org.jboss.jandex.DotName;

import io.quarkiverse.jef.java.embedded.framework.runtime.gpio.GPIO;
import io.quarkiverse.jef.java.embedded.framework.runtime.i2c.I2C;
import io.quarkiverse.jef.java.embedded.framework.runtime.onewire.OneWire;
import io.quarkiverse.jef.java.embedded.framework.runtime.serial.Serial;
import io.quarkiverse.jef.java.embedded.framework.runtime.spi.SPI;

public class JefDeploymentConstants {
    public static final DotName SERIAL_BUS_NAME = dotName(Serial.class);
    public static final DotName SPI_BUS_NAME = dotName(SPI.class);

    public static final DotName I2C_BUS_NAME = dotName(I2C.class);
    public static final DotName GPIO_NAME = dotName(GPIO.class);
    public static final DotName ONE_WIRE_NAME = dotName(OneWire.class);

    private static DotName dotName(Class<?> annotationClass) {
        return DotName.createSimple(annotationClass.getName());
    }
}
