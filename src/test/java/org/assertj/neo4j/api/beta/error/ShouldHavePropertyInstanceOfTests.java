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
package org.assertj.neo4j.api.beta.error;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.neo4j.api.beta.testing.Randomize;
import org.assertj.neo4j.api.beta.testing.Samples;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 31/01/2021
 */
class ShouldHavePropertyInstanceOfTests {

    @Nested
    class CreateTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final Nodes.DbNode actual = Drivers.node().id(1).property("key", "value-1").build();

            // WHEN
            final ErrorMessageFactory error = ShouldHavePropertyInstanceOf.create(actual, "key", Long.class);

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\n"
                    + "Expecting NODE{id=1} to have property value \"key\" instance of:\n"
                    + "  <java.lang.Long>\n"
                    + "but the actual property value for this key is a :\n"
                    + "  <\"java.lang.String\">\n"
                    + "which is not an instance of the expected class.\n"
                    + "\n"
                    + "Actual value for this property is:\n"
                    + "  <\"value-1\">"
            );
        }
    }

    @Nested
    class ElementsTests {

        @Test
        void should_generate_an_aggregate_error_message() {
            // GIVEN
            final Nodes.DbNode node1 = Drivers.node().id(1).property("key", "value-1").build();
            final Nodes.DbNode node2 = Drivers.node().id(2).property("key", Samples.LOCAL_DATE_TIME).build();
            final Nodes.DbNode node3 = Drivers.node().id(3).property("key", 3.14).build();
            final Nodes.DbNode node4 = Drivers.node().id(4).property("key", 4).build();
            final Nodes.DbNode node5 = Drivers.node().id(5).property("key", Samples.ZONED_DATE_TIME).build();
            final Nodes.DbNode node6 = Drivers.node().id(6).property("key", true).build();

            final List<Nodes.DbNode> actual = Randomize.listOf(node1, node2, node3, node4, node5, node6);

            // WHEN
            final ErrorMessageFactory error = ShouldHavePropertyInstanceOf
                    .elements(actual, "key", Boolean.class)
                    .notSatisfies(Randomize.listOf(node1, node2, node3, node4, node5));

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\n"
                    + "Expecting nodes:\n"
                    + "  <[\"NODE{id=1}\",\n"
                    + "    \"NODE{id=2}\",\n"
                    + "    \"NODE{id=3}\",\n"
                    + "    \"NODE{id=4}\",\n"
                    + "    \"NODE{id=5}\",\n"
                    + "    \"NODE{id=6}\"]>\n"
                    + "to have property value \"key\" instance of:\n"
                    + "  <java.lang.Boolean>\n"
                    + "but some nodes have a property value which is not an instance of the expected class:\n"
                    + "\n"
                    + "  1) NODE{id=1}\n"
                    + "    - Actual value class: java.lang.String\n"
                    + "    - Actual value: value-1\n"
                    + "\n"
                    + "  2) NODE{id=2}\n"
                    + "    - Actual value class: java.time.LocalDateTime\n"
                    + "    - Actual value: 2020-02-03T04:05:06.000000007\n"
                    + "\n"
                    + "  3) NODE{id=3}\n"
                    + "    - Actual value class: java.lang.Double\n"
                    + "    - Actual value: 3.14\n"
                    + "\n"
                    + "  4) NODE{id=4}\n"
                    + "    - Actual value class: java.lang.Long\n"
                    + "    - Actual value: 4\n"
                    + "\n"
                    + "  5) NODE{id=5}\n"
                    + "    - Actual value class: java.time.ZonedDateTime\n"
                    + "    - Actual value: 2020-02-03T04:05:06.000000007+11:00[Australia/Sydney]"
            );
        }
    }

}
