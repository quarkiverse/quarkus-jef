package io.quarkiverse.jef.java.embedded.framework.runtime.gpio;

import java.util.Map;

import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioPin;

public interface GPIOManager {
    GpioPin getPin(String name, int number);

    Map<String, String> getBuses();
}
