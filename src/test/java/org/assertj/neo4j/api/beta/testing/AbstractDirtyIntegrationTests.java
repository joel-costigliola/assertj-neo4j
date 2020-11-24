package org.assertj.neo4j.api.beta.testing;

import org.junit.jupiter.api.BeforeEach;
import org.neo4j.driver.Driver;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;

/**
 * A new instance of the database will be load before each tests.
 *
 * @author pallain - 24/11/2020
 */
public abstract class AbstractDirtyIntegrationTests {

    private final Dataset dataset;

    protected Driver driver;

    @Container
    protected Neo4jContainer neo4jContainer;

    public AbstractDirtyIntegrationTests(Dataset dataset, Version version) {
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
