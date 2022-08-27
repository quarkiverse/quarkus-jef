package io.quarkiverse.jef.java.embedded.framework.deployment.devconsole;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.quarkiverse.jef.java.embedded.framework.runtime.dev.JefDevConsoleRecorder;
import io.quarkiverse.jef.java.embedded.framework.runtime.dev.tracing.*;
import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.nativeimage.ServiceProviderBuildItem;
import io.quarkus.deployment.pkg.builditem.CurateOutcomeBuildItem;
import io.quarkus.devconsole.spi.DevConsoleRuntimeTemplateInfoBuildItem;

@SuppressWarnings("unused")
public class JefDevConsoleProcessor {
    private final static Logger logger = LogManager.getLogger("JEF-Dev-Tools");

    @BuildStep(onlyIf = IsDevelopment.class)
    @Record(ExecutionTime.RUNTIME_INIT)
    public DevConsoleRuntimeTemplateInfoBuildItem exposeJefContainer(
            CurateOutcomeBuildItem curateOutcomeBuildItem,
            JefDevConsoleRecorder recorder) {
        logger.debug("Apply Jef Dev Console Recorder to Quarkus build system");
        return new DevConsoleRuntimeTemplateInfoBuildItem("jefDevContainer",
                recorder.getSupplier(), this.getClass(), curateOutcomeBuildItem);
    }

    @BuildStep(onlyIf = IsDevelopment.class)
    public void registerSPI(BuildProducer<ServiceProviderBuildItem> spis) {
        logger.debug("Register SPI for loggers");
        spis.produce(new ServiceProviderBuildItem(
                KernelTracingAgent.class.getName(),
                List.of(
                        GPIOTracingAgent.class.getName(),
                        I2CTracingAgent.class.getName(),
                        OneWireAgent.class.getName(),
                        SerialTracer.class.getName(),
                        SpiTracingAgent.class.getName())));
    }
}
