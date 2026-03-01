package com.supermarket.integration;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;

public class DockerDebugTest {

    public static void main(String[] args) {
        DefaultDockerClientConfig config =
                DefaultDockerClientConfig.createDefaultConfigBuilder().build();

        System.out.println("Docker host: " + config.getDockerHost());
        System.out.println("Configured API version: " + config.getApiVersion());

        DockerClient client = DockerClientBuilder.getInstance(config).build();
        System.out.println("Client class: " + client.getClass());
    }
}