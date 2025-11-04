package andreydem0505.remoteconfig.services;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class MongoTestBase {

    static final MongoDBContainer mongoDBContainer;

    static {
        mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:8.0"))
                .withExposedPorts(27017);
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.data.mongodb.database", () -> "RemoteConfig-Test");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("MongoDB container started at: " + mongoDBContainer.getReplicaSetUrl());
        System.out.println("MongoDB container is running: " + mongoDBContainer.isRunning());
    }

    @AfterAll
    static void afterAll() {
        if (mongoDBContainer.isRunning()) {
            System.out.println("MongoDB container will be stopped");
        }
    }
}
