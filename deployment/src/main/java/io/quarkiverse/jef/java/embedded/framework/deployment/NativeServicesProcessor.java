package io.quarkiverse.jef.java.embedded.framework.deployment;

import io.quarkiverse.jef.java.embedded.framework.linux.core.*;
import io.quarkiverse.jef.java.embedded.framework.linux.core.jna.*;
import io.quarkiverse.jef.java.embedded.framework.linux.core.natives.*;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.nativeimage.ServiceProviderBuildItem;

@SuppressWarnings("unused")
class NativeServicesProcessor {
    @BuildStep
    void registerNativeServices(BuildProducer<ServiceProviderBuildItem> producer) {
        producer.produce(new ServiceProviderBuildItem(Cap.class.getName(), CapNative.class.getName()));
        producer.produce(new ServiceProviderBuildItem(Cap.class.getName(), CapJna.class.getName()));
        producer.produce(new ServiceProviderBuildItem(Errno.class.getName(), ErrnoNative.class.getName()));
        producer.produce(new ServiceProviderBuildItem(Errno.class.getName(), ErrnoJna.class.getName()));
        producer.produce(new ServiceProviderBuildItem(Fcntl.class.getName(), FcntlNative.class.getName()));
        producer.produce(new ServiceProviderBuildItem(Fcntl.class.getName(), FcntlJna.class.getName()));
        producer.produce(new ServiceProviderBuildItem(Ioctl.class.getName(), IoctlNative.class.getName()));
        producer.produce(new ServiceProviderBuildItem(Ioctl.class.getName(), IoctlJna.class.getName()));
        producer.produce(new ServiceProviderBuildItem(Mmap.class.getName(), MmapNative.class.getName()));
        producer.produce(new ServiceProviderBuildItem(Mmap.class.getName(), MmapJna.class.getName()));
        producer.produce(new ServiceProviderBuildItem(Sys.class.getName(), SysNative.class.getName()));
        producer.produce(new ServiceProviderBuildItem(Sys.class.getName(), SysJna.class.getName()));
        producer.produce(new ServiceProviderBuildItem(Termios.class.getName(), TermiosNative.class.getName()));
        producer.produce(new ServiceProviderBuildItem(Termios.class.getName(), TermiosJna.class.getName()));
    }
}
