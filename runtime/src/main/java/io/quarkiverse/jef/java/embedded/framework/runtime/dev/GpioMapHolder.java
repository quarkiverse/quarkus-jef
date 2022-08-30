package io.quarkiverse.jef.java.embedded.framework.runtime.dev;

import java.util.ArrayList;
import java.util.List;

import io.quarkiverse.jef.java.embedded.framework.linux.core.NativeIOException;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioChipInfo;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioLineInfo;
import io.quarkiverse.jef.java.embedded.framework.linux.gpio.GpioPin;
import io.quarkiverse.jef.java.embedded.framework.runtime.gpio.GPIOManager;

public class GpioMapHolder {
    private final String name;
    private final String path;

    private final GPIOManager gpioManager;
    private String systemName;
    private String systemLabel;
    private int lines;

    private List<GpioPinInfo> pins;

    public GpioMapHolder(String name, String path, GPIOManager gpioManager) throws NativeIOException {
        this.name = name;
        this.path = path;
        this.gpioManager = gpioManager;
        init();
    }

    private void init() throws NativeIOException {
        GpioChipInfo chipInfo = gpioManager.getChipInfo(path);
        this.systemName = chipInfo.getName();
        this.systemLabel = chipInfo.getLabel();
        this.lines = chipInfo.getLines();
        pins = new ArrayList<>();
        for (int i = 0; i < lines; i++) {
            GpioPin pin = gpioManager.getPin(name, i);
            pins.add(new GpioPinInfo(pin));
        }
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getSystemName() {
        return systemName;
    }

    public String getSystemLabel() {
        return systemLabel;
    }

    public int getLines() {
        return lines;
    }

    public List<GpioPinInfo> getPins() {
        return pins;
    }

    public static class GpioPinInfo {
        private final GpioPin delegate;

        public GpioPinInfo(GpioPin pin) {
            this.delegate = pin;
        }

        public int getPinNumber() {
            return delegate.getPinNumber();
        }

        public String getName() {
            return delegate.getName();
        }

        public String getConsumer() {
            return delegate.getConsumer();
        }

        public String getDirection() {
            return delegate.getDirection() == GpioPin.Direction.INPUT ? "Input" : "Output";
        }

        public String getFlags() {
            int flags = delegate.getFlags();
            StringBuilder buf = new StringBuilder();

            if ((flags & GpioLineInfo.Flags.GPIOLINE_FLAG_KERNEL.getValue()) > 0) {
                buf.append("Kernel ");
            }

            if ((flags & GpioLineInfo.Flags.GPIOLINE_FLAG_IS_OUT.getValue()) > 0) {
                buf.append("Out ");
            }

            if ((flags & GpioLineInfo.Flags.GPIOLINE_FLAG_ACTIVE_LOW.getValue()) > 0) {
                buf.append("Active Low ");
            }

            if ((flags & GpioLineInfo.Flags.GPIOLINE_FLAG_OPEN_DRAIN.getValue()) > 0) {
                buf.append("Open Drain ");
            }

            if ((flags & GpioLineInfo.Flags.GPIOLINE_FLAG_OPEN_SOURCE.getValue()) > 0) {
                buf.append("Open Source ");
            }

            if ((flags & GpioLineInfo.Flags.GPIOLINE_FLAG_BIAS_PULL_UP.getValue()) > 0) {
                buf.append("Pull Up ");
            }

            if ((flags & GpioLineInfo.Flags.GPIOLINE_FLAG_BIAS_PULL_DOWN.getValue()) > 0) {
                buf.append("Pull Down ");
            }

            if ((flags & GpioLineInfo.Flags.GPIOLINE_FLAG_BIAS_DISABLE.getValue()) > 0) {
                buf.append("Disable ");
            }

            return buf.toString();
        }
    }
}
