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
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author Patrick Allain - 28/12/2020
 */
@DisplayName("Neo4j Property Types")
@Tag(TestTags.INTEGRATION)
class TypesTests {

    @Nested
    @DisplayName("Nodes")
    class PropertyTypeTests extends IntegrationTests.DatasetTests {

        public PropertyTypeTests() {
            super(Dataset.TYPES);
        }

        @Test
        void should_property_have_the_right_type() {
            Nodes nodes = Nodes.of(driver, "Types");
            DriverAssertions.assertThat(nodes)
                    // Simple property types
                    .havePropertyOfType("type_boolean", ValueType.BOOLEAN)
                    .havePropertyOfType("type_string", ValueType.STRING)
                    .havePropertyOfType("type_long", ValueType.INTEGER)
                    .havePropertyOfType("type_double", ValueType.FLOAT)
                    .havePropertyOfType("type_date", ValueType.DATE)
                    .havePropertyOfType("type_datetime", ValueType.DATE_TIME)
                    .havePropertyOfType("type_localdatetime", ValueType.LOCAL_DATE_TIME)
                    .havePropertyOfType("type_time", ValueType.TIME)
                    .havePropertyOfType("type_localtime", ValueType.LOCAL_TIME)
                    .havePropertyOfType("type_duration", ValueType.DURATION)
                    .havePropertyOfType("type_point_2d", ValueType.POINT)
                    .havePropertyOfType("type_point_3d", ValueType.POINT)
                    // Composite property types
                    .havePropertyOfType("type_list_boolean", ValueType.LIST)
                    .havePropertyOfType("type_list_string", ValueType.LIST)
                    .havePropertyOfType("type_list_long", ValueType.LIST)
                    .havePropertyOfType("type_list_double", ValueType.LIST)
                    .havePropertyOfType("type_list_date", ValueType.LIST)
                    .havePropertyOfType("type_list_datetime", ValueType.LIST)
                    .havePropertyOfType("type_list_localdatetime", ValueType.LIST)
                    .havePropertyOfType("type_list_time", ValueType.LIST)
                    .havePropertyOfType("type_list_localtime", ValueType.LIST)
                    .havePropertyOfType("type_list_duration", ValueType.LIST)
                    .havePropertyOfType("type_list_point_2d", ValueType.LIST)
                    .havePropertyOfType("type_list_point_3d", ValueType.LIST)
            ;
        }
    }
}
