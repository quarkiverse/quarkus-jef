
package io.quarkiverse.jef.java.embedded.framework.linux.core;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

class NativeBeanLoader<S> {
    private static final Logger log = Logger.getLogger(NativeBeanLoader.class.getName());

    static <S extends FeatureSupport> S createContent(Class<S> service, boolean checkPermissions) {
        if (checkPermissions) {
            RuntimePermissionsChecker.check();
        }

        log.info(() -> String.format(
                "Initialize %s linux access for %s type of application\n",
                service.getName(),
                LinuxUtils.isNative() ? "native" : "java"));

        ServiceLoader<S> loader = ServiceLoader.load(service, service.getClassLoader());
        Iterator<S> iterator = loader.iterator();
        while (iterator.hasNext()) {
            try {
                S obj = iterator.next();
                log.log(Level.FINEST,
                        () -> String.format(
                                "Checking %s bean %s",
                                service.getName(), obj.getClass().getName()));

                if (LinuxUtils.isMock()) {
                    if (!obj.isMock()) {
                        continue;
                    } else {
                        return obj;
                    }
                }

                if (LinuxUtils.isNative() == obj.isNativeSupported()) {
                    log.info(
                            () -> String.format(
                                    "Assign bean %s as %s provider",
                                    obj.getClass().getName(), service.getName()));
                    return obj;
                }
            } catch (ServiceConfigurationError error) {
                log.warning("loadder warn: " + error.getMessage());
                error.printStackTrace();
            }
        }

        log.log(Level.WARNING,
                () -> String.format(
                        "Provider bean for %s linux access not found. Exit from application",
                        service.getName()));
        throw new RuntimeException("unable to create SPI for " + service.getName());
    }

    static <S extends FeatureSupport> S createContent(Class<S> service) {
        return createContent(service, true);
    }
}
