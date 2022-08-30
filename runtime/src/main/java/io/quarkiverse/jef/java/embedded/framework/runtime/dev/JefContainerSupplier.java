package io.quarkiverse.jef.java.embedded.framework.runtime.dev;

import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JefContainerSupplier implements Supplier<JefDevContainer> {
    private final static Logger logger = LogManager.getLogger("JEF-Dev-Tools");

    @Override
    public JefDevContainer get() {
        logger.debug("Create Jef Dev Container");
        return new JefDevContainer();
    }
}
