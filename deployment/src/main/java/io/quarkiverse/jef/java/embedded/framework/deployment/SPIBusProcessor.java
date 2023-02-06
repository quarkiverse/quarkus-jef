package io.quarkiverse.jef.java.embedded.framework.deployment;

import static io.quarkiverse.jef.java.embedded.framework.deployment.JefDeploymentConstants.SPI_BUS_NAME;
import static io.quarkus.arc.deployment.ValidationPhaseBuildItem.ValidationErrorBuildItem;
import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.SPIBusesConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.spi.SPI;
import io.quarkiverse.jef.java.embedded.framework.runtime.spi.SPIBusManager;
import io.quarkiverse.jef.java.embedded.framework.runtime.spi.SPIBusProducer;
import io.quarkiverse.jef.java.embedded.framework.runtime.spi.SPIBusRecorder;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.AutoInjectAnnotationBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.arc.deployment.ValidationPhaseBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;

@SuppressWarnings("unused")
public class SPIBusProcessor extends AbstractProcessor {
    @BuildStep
    AutoInjectAnnotationBuildItem autoInjectSerialName() {
        return new AutoInjectAnnotationBuildItem(SPI_BUS_NAME);
    }

    @BuildStep
    @Record(STATIC_INIT)
    SyntheticBeanBuildItem configureSerialBusManager(
            CombinedIndexBuildItem combinedIndex,
            BuildProducer<ValidationErrorBuildItem> validationErrors,
            SPIBusesConfig config,
            SPIBusRecorder recorder) {
        analyseInjections(combinedIndex, validationErrors, config);
        return SyntheticBeanBuildItem.configure(SPIBusManager.class)
                .scope(ApplicationScoped.class)
                .unremovable()
                .supplier(recorder.getSpiBusManagerSupplier(config))
                .done();
    }

    @BuildStep
    AdditionalBeanBuildItem additionalBeans() {
        return AdditionalBeanBuildItem.builder().addBeanClasses(SPI.class, SPIBusProducer.class).build();
    }

    private void analyseInjections(
            CombinedIndexBuildItem combinedIndex,
            BuildProducer<ValidationPhaseBuildItem.ValidationErrorBuildItem> validationErrors,
            SPIBusesConfig config) {
        analyseInjections(
                combinedIndex,
                validationErrors,
                SPI_BUS_NAME,
                "SPI",
                config.defaultBus,
                config.namedBuses.keySet());
    }
}
