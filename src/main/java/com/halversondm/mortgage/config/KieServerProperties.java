package com.halversondm.mortgage.config;

import com.sun.istack.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "kie")
@Validated
public class KieServerProperties {

    @NotNull
    private String serverUrl;
    @NotNull
    private String serverUsername;
    @NotNull
    private String serverPassword;
    @NotEmpty
    private List<Containers> containers = new ArrayList<>();

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getServerUsername() {
        return serverUsername;
    }

    public void setServerUsername(String serverUsername) {
        this.serverUsername = serverUsername;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }

    public List<Containers> getContainers() {
        return containers;
    }

    public void setContainers(List<Containers> containers) {
        this.containers = containers;
    }

    @Override
    public String toString() {
        return "KieServerProperties{" +
                "serverUrl='" + serverUrl + '\'' +
                ", serverUsername='" + serverUsername + '\'' +
                ", serverPassword='" + serverPassword + '\'' +
                ", containers=" + containers +
                '}';
    }

    public static class Containers {

        private String packageName;
        private String containerName;

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getContainerName() {
            return containerName;
        }

        public void setContainerName(String containerName) {
            this.containerName = containerName;
        }
    }
}
