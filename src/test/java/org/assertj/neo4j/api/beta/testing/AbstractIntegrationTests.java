/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2013-2020 the original author or authors.
 */
package org.assertj.neo4j.api.beta.testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.neo4j.driver.Driver;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * The database will be clean after each tests. Node and Relationship id may change.
 *
 * @author patouche - 11/11/2020
 */
@Testcontainers
public abstract class AbstractIntegrationTests {

    private final Dataset dataset;

    protected Driver driver;

    @Container
    protected static Neo4jContainer NEO4J_CONTAINER = new Neo4jContainer<>("neo4j:4.1.1")
            .withoutAuthentication();

    public AbstractIntegrationTests(Dataset dataset) {
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
