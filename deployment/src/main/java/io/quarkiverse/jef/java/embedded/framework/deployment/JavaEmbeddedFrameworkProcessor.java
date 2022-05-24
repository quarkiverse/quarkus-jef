package io.quarkiverse.jef.java.embedded.framework.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class JavaEmbeddedFrameworkProcessor {

    private static final String FEATURE = "java-embedded-framework";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
