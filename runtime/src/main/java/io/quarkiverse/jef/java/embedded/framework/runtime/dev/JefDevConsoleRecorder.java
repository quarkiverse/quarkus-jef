package io.quarkiverse.jef.java.embedded.framework.runtime.dev;

import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class JefDevConsoleRecorder {
    private final static Logger logger = LogManager.getLogger("JEF-Dev-Tools");

    public Supplier<JefDevContainer> getSupplier() {
        logger.debug("Record Jef Dev Container");
        return new JefContainerSupplier();
    }
}
