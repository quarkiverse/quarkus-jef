package io.quarkiverse.jef.java.embedded.framework.deployment;

import static io.quarkiverse.jef.java.embedded.framework.deployment.JefDeploymentConstants.I2C_BUS_NAME;
import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.I2CBusesConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.i2c.I2C;
import io.quarkiverse.jef.java.embedded.framework.runtime.i2c.I2CBusManager;
import io.quarkiverse.jef.java.embedded.framework.runtime.i2c.I2CBusProducer;
import io.quarkiverse.jef.java.embedded.framework.runtime.i2c.I2CBusRecorder;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.AutoInjectAnnotationBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.arc.deployment.ValidationPhaseBuildItem.ValidationErrorBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;

@SuppressWarnings("unused")
public class I2CBusProcessor extends AbstractProcessor {
    @BuildStep
    AutoInjectAnnotationBuildItem autoInjectSerialName() {
        return new AutoInjectAnnotationBuildItem(I2C_BUS_NAME);
    }

    @BuildStep
    @Record(STATIC_INIT)
    SyntheticBeanBuildItem configureSerialBusManager(
            CombinedIndexBuildItem combinedIndex,
            BuildProducer<ValidationErrorBuildItem> validationErrors,
            I2CBusesConfig config,
            I2CBusRecorder recorder) {
        analyseInjections(combinedIndex, validationErrors, config);
        return SyntheticBeanBuildItem.configure(I2CBusManager.class)
                .scope(ApplicationScoped.class)
                .unremovable()
                .supplier(recorder.getI2CBusManagerSupplier(config))
                .done();
    }

    @BuildStep
    AdditionalBeanBuildItem additionalBeans() {
        return AdditionalBeanBuildItem.builder().addBeanClasses(I2C.class, I2CBusProducer.class).build();
    }

    private void analyseInjections(
            CombinedIndexBuildItem combinedIndex,
            BuildProducer<ValidationErrorBuildItem> validationErrors,
            I2CBusesConfig config) {
        analyseInjections(
                combinedIndex,
                validationErrors,
                I2C_BUS_NAME,
                "I2C",
                config.defaultBus,
                config.namedBuses.keySet());
    }
}
