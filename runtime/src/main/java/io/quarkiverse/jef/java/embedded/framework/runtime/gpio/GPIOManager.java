package io.quarkiverse.jef.java.embedded.framework.runtime.gpio;

import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioPin;

public interface GPIOManager {
    GpioPin getPin(String name, int number);
}
