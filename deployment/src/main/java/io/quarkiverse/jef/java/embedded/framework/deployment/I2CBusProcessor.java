package io.quarkiverse.jef.java.embedded.framework.deployment;

import javax.inject.Singleton;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.I2CBusesConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.i2c.I2CBusManager;
import io.quarkiverse.jef.java.embedded.framework.runtime.i2c.I2CBusRecorder;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;

@SuppressWarnings("unused")
public class I2CBusProcessor {
    @BuildStep
    @Record(ExecutionTime.STATIC_INIT)
    public void register(
            BuildProducer<SyntheticBeanBuildItem> syntheticBuildProducer,
            I2CBusesConfig cfg,
            I2CBusRecorder recorder) {
        SyntheticBeanBuildItem bean = SyntheticBeanBuildItem
                .configure(I2CBusManager.class)
                .scope(Singleton.class)
                .runtimeValue(recorder.create(cfg))
                .unremovable()
                .done();
        syntheticBuildProducer.produce(bean);
    }
}
