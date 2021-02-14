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
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.type.ValueType;
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
                    .contains(Drivers.node().id(6).label("Language").property("name", "Scala").build());
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
            DriverAssertions.assertThat(nodes)
                    .havePropertyKeys("prop_1", "prop_2", "prop_3", "prop_4");
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
            DriverAssertions.assertThat(nodes)
                    .haveProperty("name", "value");
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
                    .haveLabels("Repo")
                    .havePropertyKeys("name")
                    .filteredOnPropertyExists("onboarding_duration")
                        .havePropertySize(8)
                        .toParentAssert()
                    .havePropertyOfType("name", ValueType.STRING)
                    .ignoringIds()
                        .filteredOnPropertyValue("name", "junit5")
                            .havePropertyValueMatching("creation_date", LocalDateTime.class::isInstance)
                            .toParentAssert()
                        .toParentAssert()
                    .incomingRelationships("TYPE")
                        .ignoringIds()
                            .contains(Drivers.relation("TYPE").build())
                            .toParentAssert()
                        .toParentAssert()
                    .toRootAssert()
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
                    .ignoringIds()
                    .contains(Drivers.node().label("Language").property("name", "Scala").build());
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
        void haveListPropertyOfType() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .haveListPropertyOfType("active_branches", ValueType.STRING);
        }

        @Test
        void haveProperty() {
            final Nodes nodes = Nodes.of(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .filteredOnPropertyValue("name", "ktor")
                    .haveProperty("owner", "ktorio");
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
