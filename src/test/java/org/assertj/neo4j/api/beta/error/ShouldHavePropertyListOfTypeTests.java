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
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes.DbNode;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 27/12/2020
 */
class ShouldHavePropertyListOfTypeTests {

    @Nested
    class CreateTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final DbNode node = Drivers.node().id(3).property("key", Arrays.asList(3.11, 3.12, 3.13)).build();

            // WHEN
            final ErrorMessageFactory error = ShouldHavePropertyListOfType.create(node, "key", ValueType.INTEGER);

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\n"
                    + "Expected NODE{id=3} to have a composite property list named \"key\" containing only type:\n"
                    + "  <INTEGER>\n"
                    + "but this composite property list contains type:\n"
                    + "  <FLOAT>\n"
                    + "with actual value:\n"
                    + "  <[3.11, 3.12, 3.13]>"
            );

        }
    }

    @Nested
    class ElementsTests {

        @Test
        void should_generate_an_aggregate_error_message() {
            // GIVEN
            final DbNode node1 = Drivers.node().id(1).property("key", emptyList()).build();
            final DbNode node2 = Drivers.node().id(2).property("key", singletonList("v-2.1.1")).build();
            final DbNode node3 = Drivers.node().id(3).property("key", asList(3.11, 3.12)).build();
            final DbNode node4 = Drivers.node().id(4).property("key", asList(4, 5, 6)).build();

            final List<DbNode> actual = Randomize.listOf(node1, node2, node3, node4);
            final GroupingEntityErrorFactory<DbNode> elements = ShouldHavePropertyListOfType
                    .elements(actual, "key", ValueType.STRING);

            // WHEN
            final ErrorMessageFactory error = elements.notSatisfies(Randomize.listOf(node3, node4));

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\n"
                    + "Expecting nodes:\n"
                    + "  <[\"NODE{id=1}\", \"NODE{id=2}\", \"NODE{id=3}\", \"NODE{id=4}\"]>\n"
                    + "to have a composite property list named \"key\" containing only type:\n"
                    + "  <STRING>\n"
                    + "but some nodes have a composite list containing others type:\n"
                    + "\n"
                    + "  1) NODE{id=3}\n"
                    + "    - Actual list value type: FLOAT\n"
                    + "    - Actual value: [3.11, 3.12]\n"
                    + "\n"
                    + "  2) NODE{id=4}\n"
                    + "    - Actual list value type: INTEGER\n"
                    + "    - Actual value: [4, 5, 6]"
            );

        }
    }
}
