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
package org.assertj.neo4j.api.beta.integrations;

import org.assertj.neo4j.api.beta.DriverAssertions;
import org.assertj.neo4j.api.beta.testing.Dataset;
import org.assertj.neo4j.api.beta.testing.IntegrationTests;
import org.assertj.neo4j.api.beta.testing.Neo4JDataSet;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Driver;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * https://insights.stackoverflow.com/survey/2020#correlated-technologies
 *
 * @author patouche - 5/26/20.
 */
@Testcontainers
@IntegrationTests.ToBeImplemented
public class ParisIntegrationTests extends IntegrationTests.DatasetTests {


    protected ParisIntegrationTests() {
        super(Dataset.PARIS_SUBWAY);
    }

    @Test
    public void allStationOfLines() {
        // GIVEN & WHEN

        // WHEN
        final String s = "MATCH (n:Station)<--(s:Stop) WHERE '14' IN n.lines RETURN n, s";

        // THEN

    }

}
