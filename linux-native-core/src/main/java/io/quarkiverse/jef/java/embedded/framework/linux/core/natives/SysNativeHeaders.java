package io.quarkiverse.jef.java.embedded.framework.linux.core.natives;

import java.util.Arrays;
import java.util.List;

import org.graalvm.nativeimage.c.CContext;

import io.quarkiverse.jef.java.embedded.framework.linux.core.LinuxUtils;

public class SysNativeHeaders implements CContext.Directives {
    public List<String> getHeaderFiles() {
        return Arrays.asList(
                "\"/usr/include/linux/fcntl.h\"",
                LinuxUtils.resolveHeader("sys.h"));
    }
}
