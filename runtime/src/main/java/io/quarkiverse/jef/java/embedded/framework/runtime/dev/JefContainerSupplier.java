package io.quarkiverse.jef.java.embedded.framework.runtime.dev;

import java.util.function.Supplier;

public class JefContainerSupplier implements Supplier<JefDevContainer> {
    @Override
    public JefDevContainer get() {
        return new JefDevContainer();
    }
}
