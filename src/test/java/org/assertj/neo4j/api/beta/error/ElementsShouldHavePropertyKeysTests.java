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

import org.assertj.core.api.Assertions;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.type.Nodes.DbNode;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.util.Entities;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author patouhe - 26/11/2020
 */
class ElementsShouldHavePropertyKeysTests {

    @Nested
    @DisplayName("missingPropertyKeys(Entity, List<String>)")
    class EntityMissingPropertyKeysTests {

        @Test
        void should_return_a_missing_with_no_data() {
            // GIVEN
            final List<String> keys = Arrays.asList("k-1", "k-2", "k-3");
            final DbNode entity = Drivers.node()
                    .property("k-1", "val-1")
                    .property("k-2", "val-2")
                    .property("k-3", "val-3")
                    .build();
            // WHEN
            final Missing<DbNode, String> result = ElementsShouldHavePropertyKeys.missingPropertyKeys(entity, keys);

            // THEN
            assertThat(result).isNotNull();
            assertThat(result.getEntity()).isSameAs(entity);
            assertThat(result.getData()).isNotNull();
            assertThat(result.hasMissing()).isFalse();
        }

        @Test
        void should_return_a_missing_with_data() {
            // GIVEN
            final List<String> keys = Arrays.asList("k-1", "k-2", "k-3");
            final DbNode entity = Drivers.node().property("k-1", "val-1").build();

            // WHEN
            final Missing<DbNode, String> result = ElementsShouldHavePropertyKeys.missingPropertyKeys(entity, keys);

            // THEN
            assertThat(result).isNotNull();
            assertThat(result.getEntity()).isSameAs(entity);
            assertThat(result.getData()).isNotNull().containsExactly("k-2", "k-3");
            assertThat(result.hasMissing()).isTrue();
        }

    }

    @Nested
    @DisplayName("missingPropertyKeys(List<Entity>, List<String>)")
    class MissingPropertyKeysTests {

        @Test
        void should_return_an_empty_list() {
            // GIVEN
            final List<String> keys = Arrays.asList("k-1", "k-2", "k-3");
            final List<DbNode> entities = IntStream.range(0, 10)
                    .mapToObj(i -> Drivers.node()
                            .property("k-1", "val-1-" + i)
                            .property("k-2", "val-2-" + i)
                            .property("k-3", "val-3-" + i)
                            .build())
                    .collect(Collectors.toList());

            // WHEN
            final List<Missing<DbNode, String>> result = ElementsShouldHavePropertyKeys.missingPropertyKeys(entities, keys);

            // THEN
            assertThat(result).isNotNull().isEmpty();
        }

        @Test
        void should_return_a_list_of_missing_nodes() {
            // GIVEN
            final List<String> keys = Arrays.asList("k-1", "k-2", "k-3");
            final List<DbNode> entities = Arrays.asList(
                    Drivers.node().property("k-1", "val-1-1").property("k-2", "val-1-2").build(),
                    Drivers.node().property("k-1", "val-2-1").property("k-3", "val-2-3").build(),
                    Drivers.node().property("k-2", "val-3-2").property("k-3", "val-3-3").build(),
                    Drivers.node().property("k-1", "val-4-1").build()
            );

            // WHEN
            final List<Missing<DbNode, String>> result = ElementsShouldHavePropertyKeys.missingPropertyKeys(entities, keys);

            // THEN
            assertThat(result)
                    .hasSize(4)
                    .containsExactly(
                            new Missing<>(entities.get(0), Arrays.asList("k-3")),
                            new Missing<>(entities.get(1), Arrays.asList("k-2")),
                            new Missing<>(entities.get(2), Arrays.asList("k-1")),
                            new Missing<>(entities.get(3), Arrays.asList("k-2", "k-3"))
                    );
        }
    }

    @Test
    void create_single_node() {
        // GIVEN
        final List<String> expectedKeys = Arrays.asList("k-1", "k-2", "k-3");
        final List<DbNode> nodes = Arrays.asList(
                Drivers.node().id(1).property("k-1", "v-1").property("k-2", "v-2").property("k-3", "v-3").build(),
                Drivers.node().id(2).property("k-1", "v-1").property("k-2", "v-2").build(),
                Drivers.node().id(3).property("k-1", "v-1").property("k-2", "v-2").property("k-3", "v-3").build()
        );

        // WHEN
        final ElementsShouldHavePropertyKeys<?> error = ElementsShouldHavePropertyKeys.create(
                RecordType.NODE, nodes, expectedKeys
        );

        // THEN
        Assertions.assertThat(error.create()).isEqualToNormalizingNewlines(
                "\nExpecting nodes:\n"
                + "  [\"NODE{id=1}\", \"NODE{id=2}\", \"NODE{id=3}\"]\n"
                + "to have all the following property keys:\n"
                + "  [\"k-1\", \"k-2\", \"k-3\"]\n"
                + "but some property keys were missing on:\n"
                + "\n"
                + "  - NODE{id=2} have missing property keys: [k-3]\n"
                + "      Actual  : <[k-1, k-2]>\n"
                + "      Expected: <[k-1, k-2, k-3]>"
        );

    }

    @Test
    void create_multiple_nodes() {
        // GIVEN
        final List<String> expectedKeys = Arrays.asList("k-1", "k-2", "k-3");
        final List<DbNode> nodes = Arrays.asList(
                Drivers.node().id(1).property("k-3", "v-3").property("k-2", "v-2").build(),
                Drivers.node().id(2).property("k-3", "v-3").property("k-1", "v-1").build(),
                Drivers.node().id(3).property("k-2", "v-2").property("k-1", "v-1").build(),
                Drivers.node().id(4).property("k-3", "v-3").build()
        );

        // WHEN
        final ElementsShouldHavePropertyKeys<?> error = ElementsShouldHavePropertyKeys.create(
                RecordType.NODE, nodes, expectedKeys
        );

        // THEN
        Assertions.assertThat(error.create()).isEqualToNormalizingNewlines(
                "\nExpecting nodes:\n"
                + "  [\"NODE{id=1}\", \"NODE{id=2}\", \"NODE{id=3}\", \"NODE{id=4}\"]\n"
                + "to have all the following property keys:\n"
                + "  [\"k-1\", \"k-2\", \"k-3\"]\n"
                + "but some property keys were missing on:\n"
                + "\n"
                + "  - NODE{id=1} have missing property keys: [k-1]\n"
                + "      Actual  : <[k-2, k-3]>\n"
                + "      Expected: <[k-1, k-2, k-3]>\n"
                + "\n"
                + "  - NODE{id=2} have missing property keys: [k-2]\n"
                + "      Actual  : <[k-1, k-3]>\n"
                + "      Expected: <[k-1, k-2, k-3]>\n"
                + "\n"
                + "  - NODE{id=3} have missing property keys: [k-3]\n"
                + "      Actual  : <[k-1, k-2]>\n"
                + "      Expected: <[k-1, k-2, k-3]>\n"
                + "\n"
                + "  - NODE{id=4} have missing property keys: [k-1, k-2]\n"
                + "      Actual  : <[k-3]>\n"
                + "      Expected: <[k-1, k-2, k-3]>"
        );

    }
}
