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
