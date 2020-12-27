package org.assertj.neo4j.api.beta.testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.neo4j.driver.Driver;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Integration tests support.
 *
 * @author patouche - 26/12/2020
 */
public interface IntegrationTests {

    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @EnabledIfEnvironmentVariable(
            named = "ENABLE_ASSERTJ_TEST_FAILURES",
            matches = "true",
            disabledReason = "INTEGRATION TEST: FOR TESTING FAILURE (enabled when environment variable is \"true\")"
    )
    @interface FailingTests {

    }


    /** A new instance of the database will be load before each tests. */
    @Testcontainers
     abstract  class DirtyDatasetTests {

        private final Dataset dataset;

        protected Driver driver;

        @Container
        protected Neo4jContainer neo4jContainer;

        public DirtyDatasetTests(Dataset dataset, Version version) {
            this.dataset = dataset;
            this.neo4jContainer = new Neo4jContainer<>(version.dockerImage()).withoutAuthentication();
        }

        @BeforeEach
        public void setUp() {
            this.driver = Neo4JDataSet.fromUrl(neo4jContainer.getBoltUrl())
                    .execScript(dataset.resource())
                    .getDriver();
        }

    }

    /**
     * The database will be loaded once.
     * <p/>
     * Before each test, the database initialized with the provided dataset and after the test, the database will be cleanup.
     * <p/>
     * <b>Warning :</b> this may change Node and Relationship {@code id}.
     */
    @Testcontainers
     abstract  class DatasetTests {

        private final Dataset dataset;

        protected Driver driver;

        @Container
        protected static Neo4jContainer NEO4J_CONTAINER = new Neo4jContainer<>("neo4j:4.1.1")
                .withoutAuthentication();

        protected DatasetTests(final Dataset dataset) {
            this.dataset = dataset;
        }

        @BeforeEach
        public void setUp() {
            this.driver = Neo4JDataSet.fromUrl(NEO4J_CONTAINER.getBoltUrl())
                    .execScript(dataset.resource())
                    .getDriver();
        }

        @AfterEach
        public void tearDown() {
            Neo4JDataSet.fromUrl(NEO4J_CONTAINER.getBoltUrl()).clean();
        }
    }
}
