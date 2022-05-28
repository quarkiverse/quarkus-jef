package io.quarkiverse.jef.java.embedded.framework.deployment;

import java.util.ArrayList;
import java.util.List;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.BitOrderingConverter;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.SerialBaudRateConverter;
import io.quarkiverse.jef.java.embedded.framework.runtime.config.SpiModeConverter;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ServiceProviderBuildItem;

class JavaEmbeddedFrameworkProcessor {

    private static final String FEATURE = "java-embedded-framework";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
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
}
