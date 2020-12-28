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
import org.assertj.neo4j.api.beta.testing.Neo4JDataSet;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.junit.jupiter.api.Disabled;
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
@Disabled("TO BE WRITTEN WITH REAL TEST CASES")
public class ParisIntegrationTests {

    @Container
    public Neo4jContainer neo4jContainer = new Neo4jContainer<>("neo4j:4.1.1")
            .withoutAuthentication();

    @Test
    public void allStationOfLines() {
        // GIVEN & WHEN
        final Driver driver = Neo4JDataSet.fromUrl(neo4jContainer.getBoltUrl())
                .execScript("samples/language.cypher")
                .getDriver();

        // WHEN
        final String s = "MATCH (n:Station)<--(s:Stop) WHERE '14' IN n.lines RETURN n, s";

        // THEN
        final Nodes nodes = new Nodes(driver, "Language");
        DriverAssertions.assertThat(nodes)
                .hasSize(7)
                .haveLabels("Language")
                .haveProperties("name")
                .ignoringIds()
                .contains(Drivers.node().label("Language").property("name", "Scala").build());
    }

}
