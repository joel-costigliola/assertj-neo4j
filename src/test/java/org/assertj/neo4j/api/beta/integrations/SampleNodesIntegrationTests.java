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
import org.assertj.neo4j.api.beta.testing.AbstractDirtyIntegrationTests;
import org.assertj.neo4j.api.beta.testing.AbstractIntegrationTests;
import org.assertj.neo4j.api.beta.testing.Dataset;
import org.assertj.neo4j.api.beta.testing.Version;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * https://insights.stackoverflow.com/survey/2020#correlated-technologies
 *
 * @author patouche - 5/26/20.
 */
@Testcontainers
class SampleNodesIntegrationTests extends AbstractIntegrationTests {

    public SampleNodesIntegrationTests() {
        super(Dataset.GITHUB_LANGUAGE);
    }

    @Nested
    class WithIdTests extends AbstractDirtyIntegrationTests {

        public WithIdTests() {
            super(Dataset.GITHUB_LANGUAGE, Version.NEO4J_4_11_1);
        }

        @Test
        public void contains() {
            final Nodes nodes = new Nodes(driver, "Language");
            DriverAssertions.assertThat(nodes)
                    .contains(Drivers.node().id(6).label("Language").property("name", "Scala").build());
        }

    }

    @Test
    public void full() {
        final Nodes nodes = new Nodes(driver, "Language");
        DriverAssertions.assertThat(nodes)
                .hasSize(7)
                .haveLabels("Language")
                .haveProperties("name")
                .ignoringIds()
                .contains(Drivers.node().label("Language").property("name", "Scala").build());
    }

    @Test
    public void ignoringIds() {
        final Nodes nodes = new Nodes(driver, "Language");
        DriverAssertions.assertThat(nodes)
                .ignoringIds()
                .contains(Drivers.node().label("Language").property("name", "Scala").build());
    }

    @Test
    public void haveLabels() {
        final Nodes nodes = new Nodes(driver, "Language");
        DriverAssertions.assertThat(nodes).haveLabels("Language");
    }

    @Test
    public void havePropertyKeys() {
        final Nodes nodes = new Nodes(driver, "Language");
        DriverAssertions.assertThat(nodes).haveProperties("name");
    }

}
