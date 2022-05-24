package io.quarkiverse.jef.java.embedded.framework.linux.core.natives.features;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

import com.oracle.svm.core.annotate.AutomaticFeature;

import io.quarkiverse.jef.java.embedded.framework.linux.core.natives.TermiosNative;

@AutomaticFeature
public class TermiosNativeRegistrationFeature implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        System.out.println("Register TermiosNative.class");
        RuntimeReflection.register(TermiosNative.class);
    }
}
