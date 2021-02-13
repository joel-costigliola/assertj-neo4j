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

import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Values;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 25/11/2020
 */
class PresentationsTests {

    public static final SecureRandom SECURE_RANDOM = new SecureRandom();





    @Nested
    class IdTests {

        @Test
        void should_return_the_type_with_its_id() {
            // GIVEN
            final Nodes.DbNode entity = Drivers.node().id(42).label("TEST").property("prop", "value").build();

            // WHEN
            final String representation = Presentations.outputId(entity);

            // THEN
            assertThat(representation).isEqualTo("NODE{id=42}");
        }
    }

    @Nested
    class IdsTests {

        @Test
        void should_return_a_list_with_sorted_id() {
            // GIVEN
            final List<Nodes.DbNode> entities = IntStream.range(0, 10)
                    .mapToObj(i -> Drivers.node().id(i).label("TEST").property("prop", "value").build())
                    .sorted((o1, o2) -> 1 - SECURE_RANDOM.nextInt(2))
                    .collect(Collectors.toList());

            // WHEN
            final List<String> representation = Presentations.outputIds(entities);

            // THEN
            assertThat(representation).containsExactly(
                    "NODE{id=0}",
                    "NODE{id=1}",
                    "NODE{id=2}",
                    "NODE{id=3}",
                    "NODE{id=4}",
                    "NODE{id=5}",
                    "NODE{id=6}",
                    "NODE{id=7}",
                    "NODE{id=8}",
                    "NODE{id=9}"
            );
        }

        @Test
        void should_support_null() {
            // GIVEN
            final List<Nodes.DbNode> entities = Arrays.asList(
                    Drivers.node().id(1).label("TEST").property("prop-1", "value-1").build(),
                    Drivers.node().label("TEST").property("prop-2", "value-2").build(),
                    Drivers.node().id(2).label("TEST").property("prop-3", "value-3").build(),
                    Drivers.node().label("TEST").property("prop-4", "value-4").build()
            );

            // WHEN
            final List<String> representation = Presentations.outputIds(entities);

            // THEN
            assertThat(representation).containsExactly(
                    "NODE{id=null}",
                    "NODE{id=null}",
                    "NODE{id=1}",
                    "NODE{id=2}"
            );
        }
    }




}
