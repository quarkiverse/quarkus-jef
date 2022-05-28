package io.quarkiverse.jef.java.embedded.framework.deployment;

import javax.inject.Singleton;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.SerialBusesConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.serial.SerialBusManager;
import io.quarkiverse.jef.java.embedded.framework.runtime.serial.SerialBusRecorder;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;

public class SerialBusProcessor {
    @BuildStep
    @Record(ExecutionTime.STATIC_INIT)
    public void register(
            BuildProducer<SyntheticBeanBuildItem> syntheticBuildProducer,
            SerialBusesConfig cfg,
            SerialBusRecorder recorder) {
        SyntheticBeanBuildItem bean = SyntheticBeanBuildItem
                .configure(SerialBusManager.class)
                .scope(Singleton.class)
                .runtimeValue(recorder.create(cfg))
                .unremovable()
                .done();
        syntheticBuildProducer.produce(bean);
    }
}
