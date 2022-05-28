package io.quarkiverse.jef.java.embedded.framework.deployment;

import javax.inject.Singleton;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.SPIBusesConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.spi.SPIBusManager;
import io.quarkiverse.jef.java.embedded.framework.runtime.spi.SPIBusRecorder;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;

@SuppressWarnings("unused")
public class SPIBusProcessor {
    @BuildStep
    @Record(ExecutionTime.STATIC_INIT)
    public void register(
            BuildProducer<SyntheticBeanBuildItem> syntheticBuildProducer,
            SPIBusesConfig cfg,
            SPIBusRecorder recorder) {
        SyntheticBeanBuildItem bean = SyntheticBeanBuildItem
                .configure(SPIBusManager.class)
                .scope(Singleton.class)
                .runtimeValue(recorder.create(cfg))
                .unremovable()
                .done();
        syntheticBuildProducer.produce(bean);
    }
}
