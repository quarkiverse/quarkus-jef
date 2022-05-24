package io.quarkiverse.jef.java.embedded.framework.linux.core.natives.features;

import java.io.File;

import org.graalvm.nativeimage.hosted.Feature;

import com.oracle.svm.core.annotate.AutomaticFeature;

@AutomaticFeature
public class LinuxCoreRegistrationFeature implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        //System.out.println("LinuxCore: beforeAnalysis");
    }

    @Override
    public void duringAnalysis(DuringAnalysisAccess access) {
        //System.out.println("LinuxCore: duringAnalysis");
    }

    @Override
    public void afterAnalysis(AfterAnalysisAccess access) {
        //System.out.println("LinuxCore: afterAnalysis");
    }

    @Override
    public void onAnalysisExit(OnAnalysisExitAccess access) {
        //System.out.println("LinuxCore: onAnalysisExit");
    }

    @Override
    public void afterRegistration(AfterRegistrationAccess access) {
        //System.out.println("LinuxCore: afterRegistration");
    }

    @Override
    public void duringSetup(DuringSetupAccess access) {
        //System.out.println("LinuxCore: duringSetup");
    }

    @Override
    public void beforeCompilation(BeforeCompilationAccess access) {
        //System.out.println("LinuxCore: beforeCompilation");
    }

    @Override
    public void afterCompilation(AfterCompilationAccess access) {
        //System.out.println("LinuxCore: afterCompilation");
    }

    @Override
    public void afterHeapLayout(AfterHeapLayoutAccess access) {
        //System.out.println("LinuxCore: afterHeapLayout");
    }

    @Override
    public void beforeImageWrite(BeforeImageWriteAccess access) {
        //System.out.println("LinuxCore: beforeImageWrite");
    }

    @Override
    public void afterImageWrite(AfterImageWriteAccess access) {
        //System.out.println("LinuxCore: afterImageWrite");
    }

    @Override
    public void cleanup() {
        System.out.println("LinuxCore: CLEANUP AFTER BUILD IMAGE");
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        File jefDir = new File(tmpDir, "jef");
        if (jefDir.exists()) {
            File[] files = jefDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    f.delete();
                }
            }
        }
        if (!jefDir.delete()) {
            System.out.println("WARNING: JEF tmp dir not deleted");
        }
    }
}
