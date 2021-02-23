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
import org.assertj.neo4j.api.beta.testing.TestTags;
import org.assertj.neo4j.api.beta.testing.Version;
import org.assertj.neo4j.api.beta.type.Entities;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.assertj.neo4j.api.beta.type.loader.Nodes;
import org.assertj.neo4j.api.beta.util.DbRepresentation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Patrick Allain - 5/26/20.
 */
@Tag(TestTags.INTEGRATION)
class SampleNodesIntegrationTests {

    @Nested
    @DisplayName("WithId")
    class WithIdTests extends IntegrationTests.DirtyDatasetTests {

        public WithIdTests() {
            super(Dataset.GITHUB_LANGUAGE, Version.NEO4J_4_11_1);
        }

        @Test
        public void contains() {
            final Nodes nodes = Nodes.of(driver, "Language");
            DriverAssertions.assertThat(nodes)
                    .contains(Entities.node().id(6).labels("Language").property("name", "Scala").build());
        }

    }

    @Nested
    @IntegrationTests.FailingTests
    @DisplayName("Should failed")
    class ShouldFailedTests extends IntegrationTests.DatasetTests {

        public ShouldFailedTests() {
            super(Dataset.GITHUB_LANGUAGE);
        }

        @Test
        void haveLabels() {
            final Nodes nodes = Nodes.of(driver, "Language");
            DriverAssertions.assertThat(nodes).haveLabels("OtherLabel", "AnotherLabel");
        }

        @Test
        void haveSize() {
            final Nodes nodes = Nodes.of(driver, "Language");
            DriverAssertions.assertThat(nodes).hasSize(42);
        }

        @Test
        void havePropertyKeys() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes).havePropertyKeys("prop_1", "prop_2", "prop_3", "prop_4");
        }

        @Test
        void havePropertyOfType() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .havePropertyOfType("name", ValueType.DATE_TIME)
                    .havePropertyOfType("creation_date", ValueType.DURATION)
                    .havePropertyOfType("active_branches", ValueType.DATE_TIME)
                    .havePropertyOfType("non_existing_property", ValueType.DATE_TIME);
        }

        @Test
        void haveProperty() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes).haveProperty("name", "value");
        }

        @Test
        void haveListPropertyOfType() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes).haveListPropertyOfType("active_branches", ValueType.INTEGER);
        }

        @Test
        void filteredOn() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .filteredOn(n -> Objects.equals(n.getPropertyValue("owner"), "pallets"))
                    .hasSize(3);
        }

        @Test
        void filteredOnPropertyExists() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .hasSize(12)
                    .filteredOnPropertyExists("onboarding_duration")
                    .havePropertyOfType("onboarding_duration", ValueType.STRING);
        }

        @Test
        void filteredOnPropertyValue() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .filteredOnPropertyValue("owner", "pallets")
                    .hasSize(3);
        }

        @Test
        void haveNoIncomingRelationships() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .filteredOnPropertyValue("name", "neo4j")
                    .haveNoIncomingRelationships()
                    .hasSize(2);
        }

        @Test
        void incomingRelationships() {
            final Nodes nodes = Nodes.of(driver, "Language");
            DriverAssertions.assertThat(nodes)
                    .filteredOnPropertyValue("name", "Java")
                    .incomingRelationships("KNOWS", "WRITTEN")
                    .hasSize(8)
                    .toParentAssert()
                    .incomingRelationships("WRITTEN")
                    .hasSize(4);
        }

        @Test
        void outgoingRelationships() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .filteredOnPropertyValue("name", "neo4j")
                    .outgoingRelationships("WRITTEN")
                    .hasSize(3);
        }

        @Test
        void withRepresentation() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .withRepresentation(DbRepresentation.full())
                    .hasSize(12)
                    .filteredOnPropertyExists("onboarding_duration")
                    .havePropertyOfType("onboarding_duration", ValueType.STRING);
        }
    }

    @Nested
    @DisplayName("Should succeed")
    class ShouldSucceedTests extends IntegrationTests.DatasetTests {

        public ShouldSucceedTests() {
            super(Dataset.GITHUB_LANGUAGE);
        }

        @Test
        void full() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            //@formatter:off
            DriverAssertions.assertThat(nodes)
                    .hasSize(12)
                    .withFullRepresentation()
                    .haveLabels("Repo")
                    .havePropertyKeys("name")
                    .filteredOnPropertyExists("onboarding_duration")
                        .havePropertySize(8)
                        .toParentAssert()
                    .havePropertyOfType("name", ValueType.STRING)
                    .usingNoEntityIdComparison()
                    .filteredOnPropertyExists("name")
                        .havePropertyValueMatching("creation_date", LocalDateTime.class::isInstance)
                        .filteredOnPropertyValue("owner", "pallets")
                            .havePropertyValueMatching("url", String.class, (s) -> s.startsWith("https://github.com/pallets/"))
                            .toRootAssert()
                    .filteredOnPropertyValue("name", "junit5")
                        .hasSize(1)
                        .outgoingRelationships()
                            .usingNoEntityIdComparison()
                            .contains(Entities.relationship("WRITTEN").property("percent", 97.5).build())
                            .extractingProperty("percent", Double.class)
                                .allMatch(i -> i >= 0.0)
                                .allMatch(i -> i <= 100.0)
                                .contains(2.1, 97.5)
                                .toParentAssert()
                            .filteredOnPropertyValue("percent", 97.5)
                                .hasSize(1)
                                .endingNodes("Language")
                                    .haveProperty("name", "Java")
                                    .toParentAssert()
                                .toParentAssert()
                            .toParentAssert()
                        .toParentAssert()
                    .hasSize(12)
                    .haveLabels("Repo")
//            .havePropertyMatching()
            ;

            //@formatter:on
        }

        @Test
        void ignoringIds() {
            final Nodes nodes = Nodes.of(driver, "Language");
            DriverAssertions.assertThat(nodes)
                    .usingNoEntityIdComparison()
                    .contains(Entities.node().labels("Language").property("name", "Scala").build());
        }

        @Test
        void haveLabels() {
            final Nodes nodes = Nodes.of(driver, "Language");
            DriverAssertions.assertThat(nodes).haveLabels("Language");
        }

        @Test
        void havePropertyKeys() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .havePropertyKeys("name", "owner", "url", "creation_date");
        }

        @Test
        void havePropertyOfType() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .havePropertyOfType("name", ValueType.STRING)
                    .havePropertyOfType("creation_date", ValueType.LOCAL_DATE_TIME)
                    .havePropertyOfType("active_branches", ValueType.LIST);
        }

        @Test
        void haveProperty() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .filteredOnPropertyValue("name", "ktor")
                    .haveProperty("owner", "ktorio");
        }

        @Test
        void haveListPropertyOfType() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .haveListPropertyOfType("active_branches", ValueType.STRING);
        }

        @Test
        void filteredOn() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .filteredOn(n -> Objects.equals(n.getPropertyValue("owner"), "pallets"))
                    .hasSize(2);
        }

        @Test
        void filteredOnPropertyExists() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .hasSize(12)
                    .filteredOnPropertyExists("onboarding_duration")
                    .havePropertyOfType("onboarding_duration", ValueType.DURATION)
                    .hasSize(10);
        }

        @Test
        void filteredOnPropertyValue() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .filteredOnPropertyValue("owner", "pallets")
                    .hasSize(2);
        }

        @Test
        void haveNoIncomingRelationships() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .filteredOnPropertyValue("name", "neo4j")
                    .haveNoIncomingRelationships()
                    .hasSize(1);
        }

        @Test
        void incomingRelationships() {
            final Nodes nodes = Nodes.of(driver, "Language");
            DriverAssertions.assertThat(nodes)
                    .filteredOnPropertyValue("name", "Java")
                    .incomingRelationships("KNOWS", "WRITTEN")
                    .hasSize(8)
                    .toParentAssert()
                    .incomingRelationships("WRITTEN")
                    .hasSize(3);
        }

        @Test
        void outgoingRelationships() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .filteredOnPropertyValue("name", "neo4j")
                    .outgoingRelationships("WRITTEN")
                    .hasSize(2);
        }
    }
}
