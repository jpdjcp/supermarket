package com.supermarket.integration;

import org.junit.jupiter.api.Test;
import org.testcontainers.DockerClientFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DockerPingTest {

    @Test
    void dockerShouldBeAccessible() {
        assertTrue(DockerClientFactory.instance().isDockerAvailable(),
                "Docker should be accessible from Testcontainers");
    }
}