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
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.configuration.ProfileManager;

@SuppressWarnings("unused")
public class JefDevConsoleProcessor {
    private final static Logger logger = LogManager.getLogger("JEF-Dev-Tools");

    @BuildStep(onlyIf = IsDevelopment.class)
    @Record(ExecutionTime.RUNTIME_INIT)
    public void exposeJefContainer(
            BuildProducer<DevConsoleRuntimeTemplateInfoBuildItem> producer,
            CurateOutcomeBuildItem curateOutcomeBuildItem,
            JefDevConsoleRecorder recorder) {
        //Engine engine = createEngine();
        //Router
        logger.debug("Apply Jef Dev Console Recorder to Quarkus build system");
        producer.produce(new DevConsoleRuntimeTemplateInfoBuildItem(
                "io.quarkiverse.jef",
                "quarkus-java-embedded-framework",
                "jefDevContainer",
                recorder.getSupplier()));
    }

/*    @BuildStep(onlyIf = IsDevelopment.class)
    void enableService(CurateOutcomeBuildItem item,
            BuildProducer<DevConsoleRuntimeTemplateInfoBuildItem> producer) {
        logger.debug("Checking dev services for JEF");
        if (LaunchMode.current() != LaunchMode.DEVELOPMENT) {
            logger.debug("Enable dev services for JEF");
            ProfileManager.setLaunchMode(LaunchMode.DEVELOPMENT);
        } else {
            logger.debug("Dev services for JEF already enabled");
        }
    }*/

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
