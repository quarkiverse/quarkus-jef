package io.quarkiverse.jef.java.embedded.framework.deployment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.BitOrderingConverter;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.SerialBaudRateConverter;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.SpiModeConverter;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.SystemPropertyBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageSystemPropertyBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ServiceProviderBuildItem;
import io.quarkus.runtime.LaunchMode;

@SuppressWarnings("unused")
class JavaEmbeddedFrameworkProcessor {

    private static final String FEATURE = "java-embedded-framework";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep(onlyIf = IsDevMode.class)
    SystemPropertyBuildItem isDevMode() {
        return new SystemPropertyBuildItem("java.embedded.framework.mode", "test");
    }

    @BuildStep(onlyIf = IsDevMode.class)
    NativeImageSystemPropertyBuildItem isNativeDevMode() {
        return new NativeImageSystemPropertyBuildItem("java.embedded.framework.mode", "test");
    }

    @BuildStep
    void registerConverters(BuildProducer<ServiceProviderBuildItem> producer) {
        List<String> providers = new ArrayList<>();
        providers.add(SerialBaudRateConverter.class.getName());
        providers.add(BitOrderingConverter.class.getName());
        providers.add(SpiModeConverter.class.getName());

        producer.produce(new ServiceProviderBuildItem(
                "org.eclipse.microprofile.config.spi.Converter",
                providers));
    }

    static class IsDevMode implements BooleanSupplier {
        LaunchMode launchMode;

        public boolean getAsBoolean() {
            return launchMode == LaunchMode.DEVELOPMENT || launchMode == LaunchMode.TEST;
        }
    }
}
