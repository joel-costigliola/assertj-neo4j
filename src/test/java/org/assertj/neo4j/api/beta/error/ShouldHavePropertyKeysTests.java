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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author patouhe - 26/11/2020
 */
class ShouldHavePropertyKeysTests {

    private static final List<String> KEYS = Randomize.listOf("k-1", "k-2", "k-3");

    @Nested
    class CreateTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final DbNode entity = Drivers.node().id(42).property("k-1", 1).property("k-4", 4).build();

            // WHEN
            final ErrorMessageFactory error = ShouldHavePropertyKeys.create(entity, KEYS);

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting NODE{id=42} with property keys:\n"
                    + "  <[\"k-1\", \"k-4\"]>\n"
                    + "to have property keys:\n"
                    + "  <[\"k-1\", \"k-2\", \"k-3\"]>\n"
                    + "but the following property keys cannot be found:\n"
                    + "  <[\"k-2\", \"k-3\"]>"
            );
        }

    }

    @Nested
    class ElementsTests {

        @Test
        void should_generate_an_aggregate_error_message() {
            // GIVEN
            final List<String> keys = Arrays.asList("k-1", "k-2", "k-3");
            final DbNode node1 = Drivers.node().id(12).property("k-1", 1).build();
            final DbNode node2 = Drivers.node().id(18).property("k-2", 2).build();
            final DbNode node3 = Drivers.node().id(42).property("k-3", 3).build();
            final DbNode node4 = Drivers.node().id(51).property("k-1", 1).property("k-3", 3).build();
            final DbNode node5 = Drivers.node().id(69).property("k-2", 2).property("k-3", 3).build();
            final DbNode node6 = Drivers.node().id(95).property("k-1", 1).property("k-2", 2).property("k-3", 4).build();
            final List<DbNode> entities = Randomize.listOf(node1, node2, node3, node4, node5, node6);

            // WHEN
            final ErrorMessageFactory error = ShouldHavePropertyKeys
                    .elements(entities, keys)
                    .notSatisfies(Randomize.listOf(node1, node2, node3, node4, node5));

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\n"
                    + "Expecting nodes:\n"
                    + "  <[\"NODE{id=12}\",\n"
                    + "    \"NODE{id=18}\",\n"
                    + "    \"NODE{id=42}\",\n"
                    + "    \"NODE{id=51}\",\n"
                    + "    \"NODE{id=69}\",\n"
                    + "    \"NODE{id=95}\"]>\n"
                    + "to have properties with keys:\n"
                    + "  <[\"k-1\", \"k-2\", \"k-3\"]>\n"
                    + "but some nodes don't have this properties:\n"
                    + "\n"
                    + "  1) NODE{id=12}\n"
                    + "    - Actual property keys: [k-1]\n"
                    + "    - Missing property keys: [k-2, k-3]\n"
                    + "\n"
                    + "  2) NODE{id=18}\n"
                    + "    - Actual property keys: [k-2]\n"
                    + "    - Missing property keys: [k-1, k-3]\n"
                    + "\n"
                    + "  3) NODE{id=42}\n"
                    + "    - Actual property keys: [k-3]\n"
                    + "    - Missing property keys: [k-1, k-2]\n"
                    + "\n"
                    + "  4) NODE{id=51}\n"
                    + "    - Actual property keys: [k-1, k-3]\n"
                    + "    - Missing property keys: [k-2]\n"
                    + "\n"
                    + "  5) NODE{id=69}\n"
                    + "    - Actual property keys: [k-2, k-3]\n"
                    + "    - Missing property keys: [k-1]"
            );
        }
    }

}
