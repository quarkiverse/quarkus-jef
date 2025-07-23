package io.quarkiverse.jef.java.embedded.framework.deployment;

import static org.jboss.jandex.AnnotationTarget.Kind.METHOD;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.DotName;

import io.quarkiverse.jef.java.embedded.framework.runtime.config.ConfigConstants;
import io.quarkus.arc.deployment.ValidationPhaseBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;

public abstract class AbstractProcessor {
    private final static Logger logger = LogManager.getLogger(SerialBusProcessor.class);

    protected void analyseInjections(
            CombinedIndexBuildItem combinedIndex,
            BuildProducer<ValidationPhaseBuildItem.ValidationErrorBuildItem> validationErrors,
            DotName annotation,
            String topic,
            Set<String> names) {
        Set<String> errors = new HashSet<>();
        Set<String> buses = new HashSet<>(names);
        // the default bus is part of the map and we want to ignore it
        buses.remove(ConfigConstants.DEFAULT_NAME);
        Set<String> unused = new TreeSet<>(buses);

        for (AnnotationInstance binding : combinedIndex.getIndex().getAnnotations(annotation)) {
            String name = binding.value("name").asString();
            if (binding.target().kind() == METHOD
                    && "".equals(name)) {
                continue;
            }
            if (buses.contains(name)) {
                unused.remove(name);
            } else {
                errors.add(name);
            }
        }

        if (!unused.isEmpty()) {
            logger.warn("{} buses {} declared in config but never used in code", topic, String.join(",", unused));
        }

        for (String s : errors) {
            validationErrors.produce(new ValidationPhaseBuildItem.ValidationErrorBuildItem(
                    new IllegalArgumentException(topic + " bus " + s + " used in code but not declared in configuration")));
        }
    }
}
