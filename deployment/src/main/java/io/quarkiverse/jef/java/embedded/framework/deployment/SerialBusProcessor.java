package io.quarkiverse.jef.java.embedded.framework.deployment;

import static io.quarkiverse.jef.java.embedded.framework.deployment.JefDeploymentConstants.SERIAL_BUS_NAME;
import static io.quarkus.arc.deployment.ValidationPhaseBuildItem.ValidationErrorBuildItem;
import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

import javax.enterprise.context.ApplicationScoped;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.SerialBusesConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.serial.Serial;
import io.quarkiverse.jef.java.embedded.framework.runtime.serial.SerialBusManager;
import io.quarkiverse.jef.java.embedded.framework.runtime.serial.SerialBusProducer;
import io.quarkiverse.jef.java.embedded.framework.runtime.serial.SerialBusRecorder;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.AutoInjectAnnotationBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.arc.deployment.ValidationPhaseBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;

@SuppressWarnings("unused")
public class SerialBusProcessor extends AbstractProcessor {
    @BuildStep
    AutoInjectAnnotationBuildItem autoInjectSerialName() {
        return new AutoInjectAnnotationBuildItem(SERIAL_BUS_NAME);
    }

    @BuildStep
    @Record(STATIC_INIT)
    SyntheticBeanBuildItem configureSerialBusManager(
            CombinedIndexBuildItem combinedIndex,
            BuildProducer<ValidationPhaseBuildItem.ValidationErrorBuildItem> validationErrors,
            SerialBusesConfig config,
            SerialBusRecorder recorder) {
        analyseInjections(combinedIndex, validationErrors, config);
        return SyntheticBeanBuildItem.configure(SerialBusManager.class)
                .scope(ApplicationScoped.class)
                .unremovable()
                .supplier(recorder.getSerialBusManagerSupplier(config))
                .done();
    }

    @BuildStep
    AdditionalBeanBuildItem additionalBeans() {
        return AdditionalBeanBuildItem.builder().addBeanClasses(Serial.class, SerialBusProducer.class).build();
    }

    private void analyseInjections(
            CombinedIndexBuildItem combinedIndex,
            BuildProducer<ValidationErrorBuildItem> validationErrors,
            SerialBusesConfig config) {
        analyseInjections(
                combinedIndex,
                validationErrors,
                SERIAL_BUS_NAME,
                "Serial",
                config.defaultBus,
                config.namedBuses.keySet());
    }
}
