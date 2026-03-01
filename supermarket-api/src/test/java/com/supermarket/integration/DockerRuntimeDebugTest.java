package com.supermarket.integration;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import org.junit.jupiter.api.Test;

public class DockerRuntimeDebugTest {

    @Test
    void printDockerClientInfo() {
        DefaultDockerClientConfig config =
                DefaultDockerClientConfig.createDefaultConfigBuilder().build();

        System.out.println("==== DOCKER RUNTIME DEBUG ====");
        System.out.println("Docker host: " + config.getDockerHost());
        System.out.println("Configured API version: " + config.getApiVersion());
        System.out.println("Docker config path: " + config.getDockerConfig());

        DockerClient client = DockerClientBuilder.getInstance(config).build();
        System.out.println("Client implementation: " + client.getClass().getName());

        try {
            var version = client.versionCmd().exec();
            System.out.println("Server API version: " + version.getApiVersion());
            System.out.println("Server version: " + version.getVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("================================");
    }
}