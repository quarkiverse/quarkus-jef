
package io.quarkiverse.jef.java.embedded.framework.linux.core;

public interface FeatureSupport {
    boolean isNativeSupported();

    default boolean isMock() {
        return false;
    }
}
