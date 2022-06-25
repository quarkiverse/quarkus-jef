package io.quarkiverse.jef.java.embedded.framework.deployment;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.OneWiresConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.SerialBusesConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.onewire.OneWire;
import io.quarkiverse.jef.java.embedded.framework.runtime.onewire.OneWireManager;
import io.quarkiverse.jef.java.embedded.framework.runtime.onewire.OneWireProducer;
import io.quarkiverse.jef.java.embedded.framework.runtime.onewire.OneWireRecorder;
import io.quarkiverse.jef.java.embedded.framework.runtime.serial.SerialBusManager;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.AutoInjectAnnotationBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.arc.deployment.ValidationPhaseBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;

import javax.enterprise.context.ApplicationScoped;

import static io.quarkiverse.jef.java.embedded.framework.deployment.JefDeploymentConstants.ONE_WIRE_NAME;
import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

@SuppressWarnings("unused")
public class OneWireProcessor extends AbstractProcessor {
    @BuildStep
    AutoInjectAnnotationBuildItem autoInjectSerialName() {
        return new AutoInjectAnnotationBuildItem(ONE_WIRE_NAME);
    }

    @BuildStep
    @Record(STATIC_INIT)
    SyntheticBeanBuildItem configureSerialBusManager(
            CombinedIndexBuildItem combinedIndex,
            BuildProducer<ValidationPhaseBuildItem.ValidationErrorBuildItem> validationErrors,
            OneWiresConfig config,
            OneWireRecorder recorder) {
        analyseInjections(combinedIndex, validationErrors, config);
        return SyntheticBeanBuildItem.configure(OneWireManager.class)
                .scope(ApplicationScoped.class)
                .unremovable()
                .supplier(recorder.getOneWireManagerSupplier(config))
                .done();
    }

    @BuildStep
    AdditionalBeanBuildItem additionalBeans() {
        return AdditionalBeanBuildItem.builder().addBeanClasses(OneWire.class, OneWireProducer.class).build();
    }

    private void analyseInjections(
            CombinedIndexBuildItem combinedIndex,
            BuildProducer<ValidationPhaseBuildItem.ValidationErrorBuildItem> validationErrors,
            OneWiresConfig config) {
        analyseInjections(
                combinedIndex,
                validationErrors,
                ONE_WIRE_NAME,
                "OneWire",
                null,
                config.namedWires.keySet());
    }
}
