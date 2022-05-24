package io.quarkiverse.jef.java.embedded.framework.linux.core.natives;

import java.util.Arrays;
import java.util.List;

import org.graalvm.nativeimage.c.CContext;

public class IoctlNativeHeaders implements CContext.Directives {
    public List<String> getHeaderFiles() {
        return Arrays.asList(
                "\"/usr/include/linux/ioctl.h\"",
                "\"/usr/include/linux/spi/spidev.h\""/*
                                                      * ,
                                                      * "\"/usr/include/linux/gpio.h\""
                                                      */
        );
    }
}
