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
 * @author patouche - 25/11/2020
 */
class EntitiesTests {

    public static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static final Nodes.DbNode SAMPLE_ENTITY = node(42);

    private static final List<Nodes.DbNode> SAMPLE_ENTITIES = IntStream.range(0, 10)
            .mapToObj(EntitiesTests::node)
            .collect(Collectors.toList());

    static Nodes.DbNode node(int idx) {
        return Drivers.node()
                .id(idx)
                .property("key-boolean", idx % 2 == 0)
                .property("key-long", idx)
                .property("key-string", "value-" + idx)
                .property("key-duration", Values.value(Duration.ofDays(idx + 1)).asObject())
                .property("key-list-long", IntStream.range(0, idx).boxed().collect(Collectors.toList()))
                .build();
    }


    @Nested
    class IdTests {

        @Test
        void should_return_the_type_with_its_id() {
            // GIVEN
            final Nodes.DbNode entity = Drivers.node().id(42).label("TEST").property("prop", "value").build();

            // WHEN
            final String representation = Entities.outputId(entity);

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
            final List<String> representation = Entities.outputIds(entities);

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
            final List<String> representation = Entities.outputIds(entities);

            // THEN
            assertThat(representation).containsExactly(
                    "NODE{id=null}",
                    "NODE{id=null}",
                    "NODE{id=1}",
                    "NODE{id=2}"
            );
        }
    }

    @Nested
    class HasKeyTests {

        @Test
        void should_return_true() {
            // GIVEN
            final String key = "key";
            final Nodes.DbNode entity = Drivers.node().property("key", "value").build();

            // WHEN
            final boolean result = Entities.hasKey(entity, key);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_false() {
            // GIVEN
            final String key = "other-key";
            final Nodes.DbNode entity = Drivers.node().property("key", "value").build();

            // WHEN
            final boolean result = Entities.hasKey(entity, key);

            // THEN
            assertThat(result).isFalse();
        }
    }

    @Nested
    class HasAllKeysTests {

        @Test
        void should_return_true() {
            // GIVEN
            final List<String> keys = Arrays.asList("key-1", "key-2", "key-3");

            // WHEN
            final boolean result = Entities.hasAllKeys(SAMPLE_ENTITY, keys);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_false() {
            // GIVEN
            final List<String> keys = Arrays.asList("key-1", "key-2", "key-3", "other-key");

            // WHEN
            final boolean result = Entities.hasAllKeys(SAMPLE_ENTITY, keys);

            // THEN
            assertThat(result).isFalse();
        }
    }

    @Nested
    class HaveAllKeysTests {

        @Test
        void should_return_true() {
            // GIVEN
            final List<String> keys = Arrays.asList("k-1", "k-2", "k-3");

            // WHEN
            final boolean result = Entities.haveAllKeys(SAMPLE_ENTITIES, keys);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_false() {
            // GIVEN
            final List<String> keys = Arrays.asList("key-1", "key-2", "key-3");
            final Nodes.DbNode other = Drivers.node()
                    .property("key-1", "val-other-1")
                    .property("key-2", "val-other-2")
                    .build();
            final List<Nodes.DbNode> entities = Stream
                    .concat(SAMPLE_ENTITIES.stream(), Stream.of(other))
                    .collect(Collectors.toList());

            // WHEN
            final boolean result = Entities.haveAllKeys(entities, keys);

            // THEN
            assertThat(result).isFalse();
        }
    }

    @Nested
    class HavePropertyTypeTests {

        void should_() {
            Assertions.fail("TODO");
        }
    }


}
