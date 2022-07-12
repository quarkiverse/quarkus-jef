package io.quarkiverse.jef.java.embedded.framework.runtime;

import java.util.function.Supplier;

public class JefContainerSupplier implements Supplier<Object> {
    @Override
    public Object get() {
        return new Object();
    }
}
