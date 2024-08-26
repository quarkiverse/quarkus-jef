package io.quarkiverse.jef.java.embedded.framework.linux.core.natives.features;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

//import com.oracle.svm.core.annotate.AutomaticFeature;
import io.quarkiverse.jef.java.embedded.framework.linux.core.natives.CapNative;

//@AutomaticFeature
public class CapNativeRegistrationFeature implements Feature {
    @Override
    public void beforeAnalysis(Feature.BeforeAnalysisAccess access) {
        System.out.println("Register CapNative.class");
        System.out.println(
                "Note: Please make sure what libcap installed in your Linux platform or run 'sudo apt-get install -y libcap-dev'");
        RuntimeReflection.register(CapNative.class);
    }
}
