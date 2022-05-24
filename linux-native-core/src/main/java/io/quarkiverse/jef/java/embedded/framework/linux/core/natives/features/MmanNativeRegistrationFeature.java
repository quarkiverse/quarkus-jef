package io.quarkiverse.jef.java.embedded.framework.linux.core.natives.features;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

import com.oracle.svm.core.annotate.AutomaticFeature;

import io.quarkiverse.jef.java.embedded.framework.linux.core.natives.MmapNative;

@AutomaticFeature
public class MmanNativeRegistrationFeature implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        System.out.println("Register MmanNative.class");
        RuntimeReflection.register(MmapNative.class);
    }
}
