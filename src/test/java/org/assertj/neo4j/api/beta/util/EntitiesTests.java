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

import org.assertj.core.api.Assertions;
import org.assertj.neo4j.api.beta.error.MissingNodeLabels;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author patouche - 15/11/2020
 */
class EntitiesTests {

    @Nested
    @DisplayName("hasMissingLabels")
    class HasMissingNodeLabelsTests {

        @Test
        void shouldReturnFalse() {
            // GIVEN
            final List<String> labels = Arrays.asList("LBL_1", "LBL_2", "LBL_3");
            final Nodes.DbNode entity = Nodes.node().id(1).label("LBL_1").label("LBL_2").label("LBL_3").build();

            // WHEN
            final boolean result = Entities.hasMissingLabels(entity, labels);

            // THEN
            Assertions.assertThat(result).isFalse();
        }

        @Test
        void shouldReturnTrue() {
            // GIVEN
            final List<String> labels = Arrays.asList("LBL_1", "LBL_2", "LBL_3");
            final Nodes.DbNode entity = Nodes.node().id(1).label("LBL_1").label("LBL_3").build();

            // WHEN
            final boolean result = Entities.hasMissingLabels(entity, labels);

            // THEN
            Assertions.assertThat(result).isTrue();
        }

    }

    @Nested
    @DisplayName("haveMissingLabels")
    class HaveMissingNodeLabelsTests {

        @Test
        void shouldReturnFalse() {
            // GIVEN
            final List<String> labels = Arrays.asList("LBL_1", "LBL_2", "LBL_3");
            final List<Nodes.DbNode> entities = Arrays.asList(
                    Nodes.node().id(1).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Nodes.node().id(2).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Nodes.node().id(3).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Nodes.node().id(4).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Nodes.node().id(5).label("LBL_1").label("LBL_2").label("LBL_3").build()
            );
            // WHEN
            final boolean result = Entities.haveMissingLabels(entities, labels);

            // THEN
            Assertions.assertThat(result).isFalse();
        }

        @Test
        void shouldReturnTrue() {
            // GIVEN
            final List<String> labels = Arrays.asList("LBL_1", "LBL_2", "LBL_3");
            final List<Nodes.DbNode> entities = Arrays.asList(
                    Nodes.node().id(1).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Nodes.node().id(2).label("LBL_1").label("LBL_3").build(),
                    Nodes.node().id(3).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Nodes.node().id(4).label("LBL_1").label("LBL_2").build(),
                    Nodes.node().id(5).label("LBL_1").build()
            );
            // WHEN
            final boolean result = Entities.haveMissingLabels(entities, labels);

            // THEN
            Assertions.assertThat(result).isTrue();
        }

    }

    @Nested
    @DisplayName("hasMissingLabels")
    class MissingNodeLabelsTests {

        @Test
        void shouldReturnNewInstance() {
            // GIVEN
            final List<String> labels = Arrays.asList("LBL_1", "LBL_2", "LBL_3", "LBL_4");
            final Nodes.DbNode entity = Nodes.node().id(1).label("LBL_1").label("LBL_3").build();

            // WHEN
            final MissingNodeLabels result = Entities.missingLabels(entity, labels);

            // THEN
            Assertions.assertThat(result)
                    .isNotNull()
                    .isEqualTo(new MissingNodeLabels(entity, Arrays.asList("LBL_2", "LBL_4")));
        }

    }

    @Nested
    @DisplayName("hasMissingLabels")
    class HavingMissingNodeLabelsTests {

        @Test
        void shouldReturnNewInstance() {
            // GIVEN
            final List<String> labels = Arrays.asList("LBL_1", "LBL_2", "LBL_3");
            final List<Nodes.DbNode> entities = Arrays.asList(
                    Nodes.node().id(1).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Nodes.node().id(2).label("LBL_1").label("LBL_3").build(),
                    Nodes.node().id(3).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                    Nodes.node().id(4).label("LBL_1").label("LBL_2").build(),
                    Nodes.node().id(5).label("LBL_1").build()
            );
            // WHEN
            final List<MissingNodeLabels> result = Entities.havingMissingLabels(entities, labels);

            // THEN
            Assertions.assertThat(result)
                    .hasSize(3)
                    .contains(
                            new MissingNodeLabels(entities.get(1), Arrays.asList("LBL_2")),
                            new MissingNodeLabels(entities.get(3), Arrays.asList("LBL_3")),
                            new MissingNodeLabels(entities.get(4), Arrays.asList("LBL_2", "LBL_3"))
                    );
        }

    }

}
