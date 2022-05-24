package io.quarkiverse.jef.java.embedded.framework.linux.core.natives;

import java.util.Collections;
import java.util.List;

import org.graalvm.nativeimage.c.CContext;

public class FcntlNativeHeaders implements CContext.Directives {
    public List<String> getHeaderFiles() {
        return Collections.singletonList(
                "\"/usr/include/linux/fcntl.h\"");
    }
}
