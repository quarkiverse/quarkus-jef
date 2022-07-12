package io.quarkiverse.jef.java.embedded.framework.deployment.devconsole;

import io.quarkiverse.jef.java.embedded.framework.runtime.JefContainerSupplier;
import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.pkg.builditem.CurateOutcomeBuildItem;
import io.quarkus.devconsole.spi.DevConsoleRuntimeTemplateInfoBuildItem;

public class DevConsoleProcessor {
    @BuildStep(onlyIf = IsDevelopment.class)
    //@Record(ExecutionTime.STATIC_INIT)
    public DevConsoleRuntimeTemplateInfoBuildItem exposeJefContainer(
            CurateOutcomeBuildItem curateOutcomeBuildItem) {
        System.out.println("CALLLLLLEEEEED!!!!");
        return new DevConsoleRuntimeTemplateInfoBuildItem("jefContainer",
                new JefContainerSupplier(), this.getClass(), curateOutcomeBuildItem);
    }
}
