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
import org.assertj.neo4j.api.beta.testing.Version;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * https://insights.stackoverflow.com/survey/2020#correlated-technologies
 *
 * @author patouche - 5/26/20.
 */
@Tag("INTEGRATION")
class SampleNodesIntegrationTests {

    @Nested
    @DisplayName("WithId")
    class WithIdTests extends IntegrationTests.DirtyDatasetTests {

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

    @Nested
    @DisplayName("Property type tests")
    class PropertyTypeTests extends IntegrationTests.DatasetTests {

        public PropertyTypeTests() {
            super(Dataset.TYPES);
        }

        @Test
        void should_property_have_the_right_type() {
            Nodes nodes = new Nodes(driver, "Types");
            DriverAssertions.assertThat(nodes)
                    // Simple property types
                    .havePropertyType("type_boolean", ValueType.BOOLEAN)
                    .havePropertyType("type_string", ValueType.STRING)
                    .havePropertyType("type_long", ValueType.INTEGER)
                    .havePropertyType("type_double", ValueType.FLOAT)
                    .havePropertyType("type_date", ValueType.DATE)
                    .havePropertyType("type_datetime", ValueType.DATE_TIME)
                    .havePropertyType("type_localdatetime", ValueType.LOCAL_DATE_TIME)
                    .havePropertyType("type_time", ValueType.TIME)
                    .havePropertyType("type_localtime", ValueType.LOCAL_TIME)
                    .havePropertyType("type_duration", ValueType.DURATION)
                    .havePropertyType("type_point_2d", ValueType.POINT)
                    .havePropertyType("type_point_3d", ValueType.POINT)
                    // Composite property types
                    .havePropertyType("type_list_boolean", ValueType.LIST)
                    .havePropertyType("type_list_string", ValueType.LIST)
                    .havePropertyType("type_list_long", ValueType.LIST)
                    .havePropertyType("type_list_double", ValueType.LIST)
                    .havePropertyType("type_list_date", ValueType.LIST)
                    .havePropertyType("type_list_datetime", ValueType.LIST)
                    .havePropertyType("type_list_localdatetime", ValueType.LIST)
                    .havePropertyType("type_list_time", ValueType.LIST)
                    .havePropertyType("type_list_localtime", ValueType.LIST)
                    .havePropertyType("type_list_duration", ValueType.LIST)
                    .havePropertyType("type_list_point_2d", ValueType.LIST)
                    .havePropertyType("type_list_point_3d", ValueType.LIST)
                    .havePropertyType("type_list_mixed", ValueType.LIST)
            ;
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
            final Nodes nodes = new Nodes(driver, "Language");
            DriverAssertions.assertThat(nodes).haveLabels("OtherLabel", "AnotherLabel");
        }

        @Test
        void havePropertyKeys() {
            final Nodes nodes = new Nodes(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .havePropertyKeys("prop_1", "prop_2", "prop_3", "prop_4");
        }

        @Test
        void havePropertyType() {
            final Nodes nodes = new Nodes(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .havePropertyType("name", ValueType.DATE_TIME)
                    .havePropertyType("creation_date", ValueType.DURATION)
                    .havePropertyType("active_branches", ValueType.DATE_TIME)
                    .havePropertyType("non_existing_property", ValueType.DATE_TIME);
        }

        @Test
        void haveProperty() {
            final Nodes nodes = new Nodes(driver, "Repo");
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
            final Nodes nodes = new Nodes(driver, "Language");
            DriverAssertions.assertThat(nodes)
                    .hasSize(7)
                    .haveLabels("Language")
                    .haveProperties("name")
                    .ignoringIds()
                    .contains(Drivers.node().label("Language").property("name", "Scala").build());
        }

        @Test
        void ignoringIds() {
            final Nodes nodes = new Nodes(driver, "Language");
            DriverAssertions.assertThat(nodes)
                    .ignoringIds()
                    .contains(Drivers.node().label("Language").property("name", "Scala").build());
        }

        @Test
        void haveLabels() {
            final Nodes nodes = new Nodes(driver, "Language");
            DriverAssertions.assertThat(nodes).haveLabels("Language");
        }

        @Test
        void havePropertyKeys() {
            final Nodes nodes = new Nodes(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .havePropertyKeys("name", "owner", "url", "creation_date");
        }

        @Test
        void havePropertyType() {
            final Nodes nodes = new Nodes(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .havePropertyType("name", ValueType.STRING)
                    .havePropertyType("creation_date", ValueType.LOCAL_DATE_TIME)
                    .havePropertyType("active_branches", ValueType.LIST);
        }

        @Test
        void  haveListPropertyContainingType() {
            final Nodes nodes = new Nodes(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .haveListPropertyContainingType("active_branches", ValueType.STRING);
        }

        @Test
        void haveProperty() {
            final Nodes nodes = new Nodes(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .haveProperty("name", "value");
        }

        @Test
        void filteredOn() {
            final Nodes nodes = new Nodes(driver, "Repo");
            DriverAssertions.assertThat(nodes)
                    .filteredOn(n -> Objects.equals(n.getPropertyValue("owner"), "pallets"))
                    .hasSize(2);
        }
    }
}
