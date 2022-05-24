package io.quarkiverse.jef.java.embedded.framework.linux.core.natives;

import java.util.Collections;
import java.util.List;

import org.graalvm.nativeimage.c.CContext;

public class ErrnoNativeHeaders implements CContext.Directives {
    public List<String> getHeaderFiles() {
        return Collections.singletonList(
                "\"/usr/include/errno.h\""//,
        );
    }
}
