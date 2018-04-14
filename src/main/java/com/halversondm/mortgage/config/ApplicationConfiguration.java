package com.halversondm.mortgage.config;

import com.google.common.reflect.ClassPath;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableConfigurationProperties(KieServerProperties.class)
public class ApplicationConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfiguration.class);

    @Autowired
    KieServerProperties kieServerProperties;

    @Bean
    public KieServicesConfiguration kieServicesConfiguration() throws Exception {
        KieServicesConfiguration conf;
        LOGGER.debug("start init KIE connection");
        LOGGER.debug("KIE Configuration: {}", kieServerProperties);
        conf = KieServicesFactory.newRestConfiguration(kieServerProperties.getServerUrl(), kieServerProperties.getServerUsername(), kieServerProperties.getServerPassword());
        conf.setMarshallingFormat(MarshallingFormat.JAXB);
        ClassPath classpath = ClassPath.from(Thread.currentThread().getContextClassLoader());
        Set<Class<?>> allClasses = new HashSet<>();
        for (KieServerProperties.Containers containers : kieServerProperties.getContainers()) {
            for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClasses(containers.getPackageName())) {
                allClasses.add(classInfo.load());
            }
        }
        conf.addExtraClasses(allClasses);
        LOGGER.debug("end init KIE connection");
        return conf;
    }

    @Bean
    public KieServicesClient kieServicesClient(KieServicesConfiguration conf) {
        return KieServicesFactory.newKieServicesClient(conf);
    }
}
