package io.quarkiverse.jef.java.embedded.framework.deployment;

import static io.quarkiverse.jef.java.embedded.framework.deployment.JefDeploymentConstants.GPIO_NAME;
import static io.quarkus.arc.deployment.ValidationPhaseBuildItem.ValidationErrorBuildItem;
import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

import javax.enterprise.context.ApplicationScoped;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.GPIOsConfig;
import io.quarkiverse.jef.java.embedded.framework.runtime.gpio.GPIOManager;
import io.quarkiverse.jef.java.embedded.framework.runtime.gpio.GPIORecorder;
import io.quarkus.arc.deployment.AutoInjectAnnotationBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;

@SuppressWarnings("unused")
public class GPIOProcessor extends AbstractProcessor {
    @BuildStep
    AutoInjectAnnotationBuildItem autoInjectSerialName() {
        return new AutoInjectAnnotationBuildItem(GPIO_NAME);
    }

    @BuildStep
    @Record(STATIC_INIT)
    SyntheticBeanBuildItem configureSerialBusManager(
            CombinedIndexBuildItem combinedIndex,
            BuildProducer<ValidationErrorBuildItem> validationErrors,
            GPIOsConfig config,
            GPIORecorder recorder) {
        analyseInjections(combinedIndex, validationErrors, config);
        return SyntheticBeanBuildItem.configure(GPIOManager.class)
                .scope(ApplicationScoped.class)
                .unremovable()
                .supplier(recorder.getGPIOManagerSupplier(config))
                .done();
    }

    private void analyseInjections(
            CombinedIndexBuildItem combinedIndex,
            BuildProducer<ValidationErrorBuildItem> validationErrors,
            GPIOsConfig config) {
        analyseInjections(
                combinedIndex,
                validationErrors,
                GPIO_NAME,
                "GPIO",
                config.defaultBus,
                config.namedBuses.keySet());
    }
}
