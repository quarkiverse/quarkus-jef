
package io.quarkiverse.jef.java.embedded.framework.linux.core.natives;

import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;

public class CUtil {
    static CCharPointer toCString(String s) {
        try (CTypeConversion.CCharPointerHolder c = CTypeConversion.toCString(s)) {
            return c.get();
        }
    }

    static String fromCString(CCharPointer ptr) {
        return CTypeConversion.toJavaString(ptr);
    }
}
