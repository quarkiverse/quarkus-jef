package io.quarkiverse.jef.java.embedded.framework.deployment;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.GPIOsConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.gpio.GPIOManager;
import io.quarkiverse.jef.java.embedded.framework.runtime.gpio.GPIORecorder;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;

import javax.inject.Singleton;

public class GPIOProcessor {
    @BuildStep
    @Record(ExecutionTime.STATIC_INIT)
    public void register(
            BuildProducer<SyntheticBeanBuildItem> syntheticBuildProducer,
            GPIOsConfig cfg,
            GPIORecorder recorder
    ) {
        SyntheticBeanBuildItem bean = SyntheticBeanBuildItem
                .configure(GPIOManager.class)
                .scope(Singleton.class)
                .runtimeValue(recorder.create(cfg))
                .unremovable()
                .done();
        syntheticBuildProducer.produce(bean);
    }
}
