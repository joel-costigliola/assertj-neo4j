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
package org.assertj.neo4j.api.beta.util;

import org.assertj.neo4j.api.beta.error.Missing;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author patouche - 15/11/2020
 */
class NodeLabelsTests {

    @Nested
    @DisplayName("hasMissing")
    class HasMissingTests {

        @Test
        void should_return_false() {
            // GIVEN
            final List<String> labels = Arrays.asList("LBL_1", "LBL_2", "LBL_3");
            final Nodes.DbNode entity = Drivers.node().id(1).label("LBL_1").label("LBL_2").label("LBL_3").build();

            // WHEN
            final boolean result = NodeLabels.hasLabels(entity, labels);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final List<String> labels = Arrays.asList("LBL_1", "LBL_2", "LBL_3");
            final Nodes.DbNode entity = Drivers.node().id(1).label("LBL_1").label("LBL_3").build();

            // WHEN
            final boolean result = NodeLabels.hasLabels(entity, labels);

            // THEN
            assertThat(result).isTrue();
        }

    }

    @Nested
    @DisplayName("haveMissing")
    class HaveMissingTests {

        @Test
        void should_return_false() {
            // GIVEN
            final List<String> labels = Arrays.asList("LBL_1", "LBL_2", "LBL_3");
            final List<Nodes.DbNode> entities = Arrays.asList(
                    Drivers.node().id(1).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Drivers.node().id(2).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Drivers.node().id(3).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Drivers.node().id(4).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Drivers.node().id(5).label("LBL_1").label("LBL_2").label("LBL_3").build()
            );

            // WHEN
            final boolean result = NodeLabels.haveLabels(entities, labels);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final List<String> labels = Arrays.asList("LBL_1", "LBL_2", "LBL_3");
            final List<Nodes.DbNode> entities = Arrays.asList(
                    Drivers.node().id(1).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Drivers.node().id(2).label("LBL_1").label("LBL_3").build(),
                    Drivers.node().id(3).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Drivers.node().id(4).label("LBL_1").label("LBL_2").build(),
                    Drivers.node().id(5).label("LBL_1").build()
            );

            // WHEN
            final boolean result = NodeLabels.haveLabels(entities, labels);

            // THEN
            assertThat(result).isTrue();
        }

    }

    @Nested
    @DisplayName("missing(DbNode, Iterable<String>)")
    class NodeMissingTests {

        @Test
        void should_return_a_new_instance() {
            // GIVEN
            final List<String> labels = Arrays.asList("LBL_1", "LBL_2", "LBL_3", "LBL_4");
            final Nodes.DbNode node = Drivers.node().id(1).label("LBL_1").label("LBL_3").build();

            // WHEN
            final Missing<Nodes.DbNode, String> result = NodeLabels.missing(node, labels);

            // THEN
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(new Missing<>(node, Arrays.asList("LBL_2", "LBL_4")));
        }

    }

    @Nested
    @DisplayName("missing(List<DbNode>, Iterable<String>)")
    class ListMissingTests {
        @Test
        void should_return_an_empty_list() {
            // GIVEN
            final List<String> labels = Arrays.asList("LBL_1", "LBL_2", "LBL_3");
            final List<Nodes.DbNode> entities = Arrays.asList(
                    Drivers.node().id(1).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Drivers.node().id(2).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Drivers.node().id(3).label("LBL_1").label("LBL_2").label("LBL_3").build()
            );

            // WHEN
            final List<Missing<Nodes.DbNode, String>> result = NodeLabels.missing(entities, labels);

            // THEN
            assertThat(result).isNotNull().isEmpty();
        }


        @Test
        void should_return_a_list_of_missing_nodes() {
            // GIVEN
            final List<String> labels = Arrays.asList("LBL_1", "LBL_2", "LBL_3");
            final List<Nodes.DbNode> entities = Arrays.asList(
                    Drivers.node().id(1).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Drivers.node().id(2).label("LBL_1").label("LBL_3").build(),
                    Drivers.node().id(3).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Drivers.node().id(4).label("LBL_1").label("LBL_2").build(),
                    Drivers.node().id(5).label("LBL_1").build()
            );
            // WHEN
            final List<Missing<Nodes.DbNode, String>> result = NodeLabels.missing(entities, labels);

            // THEN
            assertThat(result)
                    .hasSize(3)
                    .contains(
                            new Missing<>(entities.get(1), Arrays.asList("LBL_2")),
                            new Missing<>(entities.get(3), Arrays.asList("LBL_3")),
                            new Missing<>(entities.get(4), Arrays.asList("LBL_2", "LBL_3"))
                    );
        }

    }

}
