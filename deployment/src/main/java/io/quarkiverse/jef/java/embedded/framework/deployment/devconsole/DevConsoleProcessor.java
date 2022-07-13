package io.quarkiverse.jef.java.embedded.framework.deployment.devconsole;

import io.quarkiverse.jef.java.embedded.framework.runtime.dev.DevConsoleRecorder;
import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
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
}
