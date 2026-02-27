import org.testcontainers.DockerClientFactory;

public class DockerPingManualTest {
    public static void main(String[] args) {
        System.out.println("Default Docker host: " + DockerClientFactory.instance().dockerHost());
        System.out.println("API version: " + DockerClientFactory.instance().dockerClient().versionCmd().exec().getApiVersion());
    }
}