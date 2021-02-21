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
import org.assertj.neo4j.api.beta.type.DbNode;
import org.assertj.neo4j.api.beta.type.Models;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author patouhe - 26/11/2020
 */
class ShouldEntityHavePropertyKeysTests {

    private static final List<String> KEYS = Randomize.listOf("k-1", "k-2", "k-3");

    @Nested
    class CreateTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final DbNode entity = Models.node().id(42).property("k-1", 1).property("k-4", 4).build();

            // WHEN
            final ErrorMessageFactory error = ShouldEntityHavePropertyKeys.create(entity, KEYS);

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting node with property keys:\n"
                    + "  <[\"k-1\", \"k-4\"]>\n"
                    + "to have property keys:\n"
                    + "  <[\"k-1\", \"k-2\", \"k-3\"]>\n"
                    + "but the following property keys cannot be found:\n"
                    + "  <[\"k-2\", \"k-3\"]>\n"
            );
        }

    }

    @Nested
    class ElementsTests {

        @Test
        void should_generate_an_aggregate_error_message() {
            // GIVEN
            final List<String> keys = Arrays.asList("k-1", "k-2", "k-3");
            final DbNode node1 = Models.node().id(12).property("k-1", 1).build();
            final DbNode node2 = Models.node().id(18).property("k-2", 2).build();
            final DbNode node3 = Models.node().id(42).property("k-3", 3).build();
            final DbNode node4 = Models.node().id(51).property("k-1", 1).property("k-3", 3).build();
            final DbNode node5 = Models.node().id(69).property("k-2", 2).property("k-3", 3).build();
            final DbNode node6 = Models.node().id(95).property("k-1", 1).property("k-2", 2).property("k-3", 4).build();
            final List<DbNode> entities = Randomize.listOf(node1, node2, node3, node4, node5, node6);

            // WHEN
            final ErrorMessageFactory error = ShouldEntityHavePropertyKeys
                    .elements(entities, keys)
                    .notSatisfies(Randomize.listOf(node1, node2, node3, node4, node5));

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting nodes:\n"
                    + "  <[NODE{id=12L, labels=[], properties={k-1=INTEGER{1}}},\n"
                    + "    NODE{id=18L, labels=[], properties={k-2=INTEGER{2}}},\n"
                    + "    NODE{id=42L, labels=[], properties={k-3=INTEGER{3}}},\n"
                    + "    NODE{id=51L, labels=[], properties={k-3=INTEGER{3}, k-1=INTEGER{1}}},\n"
                    + "    NODE{id=69L, labels=[], properties={k-3=INTEGER{3}, k-2=INTEGER{2}}},\n"
                    + "    NODE{id=95L, labels=[], properties={k-3=INTEGER{4}, k-2=INTEGER{2}, k-1=INTEGER{1}}}]>\n"
                    + "to have properties with keys:\n"
                    + "  <[\"k-1\", \"k-2\", \"k-3\"]>\n"
                    + "but some nodes don't have this properties:\n"
                    + "\n"
                    + "  1) NODE{id=12L, labels=[], properties={k-1=INTEGER{1}}}\n"
                    + "    - Actual property keys: [\"k-1\"]\n"
                    + "    - Missing property keys: [\"k-2\", \"k-3\"]\n"
                    + "\n"
                    + "  2) NODE{id=18L, labels=[], properties={k-2=INTEGER{2}}}\n"
                    + "    - Actual property keys: [\"k-2\"]\n"
                    + "    - Missing property keys: [\"k-1\", \"k-3\"]\n"
                    + "\n"
                    + "  3) NODE{id=42L, labels=[], properties={k-3=INTEGER{3}}}\n"
                    + "    - Actual property keys: [\"k-3\"]\n"
                    + "    - Missing property keys: [\"k-1\", \"k-2\"]\n"
                    + "\n"
                    + "  4) NODE{id=51L, labels=[], properties={k-3=INTEGER{3}, k-1=INTEGER{1}}}\n"
                    + "    - Actual property keys: [\"k-1\", \"k-3\"]\n"
                    + "    - Missing property keys: [\"k-2\"]\n"
                    + "\n"
                    + "  5) NODE{id=69L, labels=[], properties={k-3=INTEGER{3}, k-2=INTEGER{2}}}\n"
                    + "    - Actual property keys: [\"k-2\", \"k-3\"]\n"
                    + "    - Missing property keys: [\"k-1\"]\n"
            );
        }
    }

}
