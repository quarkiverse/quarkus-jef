package io.quarkiverse.jef.java.embedded.framework.deployment;

import io.quarkiverse.jef.java.embedded.framework.mcu.core.boards.BoardLoader;
import io.quarkiverse.jef.java.embedded.framework.mcu.core.boards.opi.OrangePiBoardsLoader;
import io.quarkiverse.jef.java.embedded.framework.mcu.core.boards.rpi.RpiBoardsLoader;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.nativeimage.ServiceProviderBuildItem;

@SuppressWarnings("unused")
class BoardServicesProcessor {
    @BuildStep
    void registerNativeServices(BuildProducer<ServiceProviderBuildItem> producer) {
        producer.produce(new ServiceProviderBuildItem(
                BoardLoader.class.getName(), RpiBoardsLoader.class.getName()));
        producer.produce(new ServiceProviderBuildItem(BoardLoader.class.getName(), OrangePiBoardsLoader.class.getName()));
    }
}
