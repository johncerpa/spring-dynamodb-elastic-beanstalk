package com.johngroup.aws;

import org.junit.ClassRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = SpringbootDynamodbExampleApplication.class, webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    private static final int DYNAMO_PORT = 8000;

    @ClassRule
    public static GenericContainer dynamoDb = new GenericContainer("amazon/dynamodb-local:latest"   )
            .withExposedPorts(DYNAMO_PORT);

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            String endpoint = String.format(
                    "aws.dynamodb.service_endpoint=http://%s:%s",
                    dynamoDb.getContainerIpAddress(),
                    dynamoDb.getMappedPort(DYNAMO_PORT)
            );

            TestPropertyValues.of(endpoint).applyTo(configurableApplicationContext);
        }
    }



}
