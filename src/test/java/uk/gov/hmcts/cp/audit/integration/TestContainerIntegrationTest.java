package uk.gov.hmcts.cp.audit.integration;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@Slf4j
class TestContainerIntegrationTest {

    private static final int AMQ_PORT = 61616;


    @Container
    static final GenericContainer<?> artemis =
            new GenericContainer<>("apache/activemq-artemis")
                    .withEnv("ANONYMOUS_LOGIN", "true")
                    .withExposedPorts(AMQ_PORT, AMQ_PORT)
                    .waitingFor(Wait.forLogMessage(".*Artemis Console available.*\\n", 1));

    @DynamicPropertySource
    static void registerArtemisProps(DynamicPropertyRegistry registry) {
        String brokerUrl = "tcp://localhost:" + artemis.getMappedPort(AMQ_PORT);
        //registry.add("spring.artemis.broker-url", () -> brokerUrl);
    }

    @SneakyThrows
    @Test
    void artemis_test_container_should_be_accessible() {
        log.info("STARTED");
        log.info("Artemis host {}", artemis.getHost());
        log.info("Artemis exposed port:{}", artemis.getMappedPort(AMQ_PORT));
        log.info("docker ps shows that the docker artemis container is running ... and the log contains Artemis Console available\n");
        log.info("Check the port is accessible with \"nc -zv localhost {}\"", artemis.getMappedPort(AMQ_PORT));
        Thread.sleep(600000);
        log.info("The artemis container will shut down once this test finishes");
        log.info("FINISHED");
    }
}