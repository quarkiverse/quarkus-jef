package io.quarkiverse.jef.java.embedded.framework.deployment.devconsole;

import java.util.List;

import io.quarkiverse.jef.java.embedded.framework.runtime.dev.DevConsoleRecorder;
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
public class DevConsoleProcessor {
    @BuildStep(onlyIf = IsDevelopment.class)
    @Record(ExecutionTime.RUNTIME_INIT)
    public DevConsoleRuntimeTemplateInfoBuildItem exposeJefContainer(
            CurateOutcomeBuildItem curateOutcomeBuildItem,
            DevConsoleRecorder recorder) {
        return new DevConsoleRuntimeTemplateInfoBuildItem("jefDevContainer",
                recorder.getSupplier(), this.getClass(), curateOutcomeBuildItem);
    }

    @BuildStep(onlyIf = IsDevelopment.class)
    public void registerSPI(BuildProducer<ServiceProviderBuildItem> spis) {
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
