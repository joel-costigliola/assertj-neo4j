package org.assertj.neo4j.api.beta.util;

import org.assertj.neo4j.api.beta.error.Missing;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
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
            final Nodes.DbNode entity = Drivers.node()
                    .property("key-1", "value-1")
                    .property("key-2", "value-2")
                    .property("key-3", "value-3")
                    .build();

            // WHEN
            final boolean result = Entities.hasAllKeys(entity, keys);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_false() {
            // GIVEN
            final List<String> keys = Arrays.asList("key-1", "key-2", "key-3", "other-key");
            final Nodes.DbNode entity = Drivers.node()
                    .property("key-1", "value-1")
                    .property("key-2", "value-2")
                    .property("key-3", "value-3")
                    .build();

            // WHEN
            final boolean result = Entities.hasAllKeys(entity, keys);

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
            final List<Nodes.DbNode> entities = IntStream.range(0, 10)
                    .mapToObj(i -> Drivers.node()
                            .property("k-1", "val-1-" + i)
                            .property("k-2", "val-2-" + i)
                            .property("k-3", "val-3-" + i)
                            .build())
                    .collect(Collectors.toList());

            // WHEN
            final boolean result = Entities.haveAllKeys(entities, keys);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_false() {
            // GIVEN
            final List<String> keys = Arrays.asList("key-1", "key-2", "key-3");
            final List<Nodes.DbNode> entities = Stream
                    .concat(
                            IntStream.range(0, 10).mapToObj(i -> Drivers.node()
                                    .property("key-1", "val-" + i + "-1")
                                    .property("key-2", "val-" + i + "-2")
                                    .property("key-3", "val-" + i + "-3")
                                    .build()),
                            Stream.of(
                                    Drivers.node()
                                            .property("key-1", "val-other-1")
                                            .property("key-2", "val-other-2")
                                            .build()
                            )
                    )
                    .collect(Collectors.toList());

            // WHEN
            final boolean result = Entities.haveAllKeys(entities, keys);

            // THEN
            assertThat(result).isFalse();
        }
    }

    @Nested
    class EntityMissingPropertyKeysTests {

        @Test
        void should_return_a_missing_with_no_data() {
            // GIVEN
            final List<String> keys = Arrays.asList("k-1", "k-2", "k-3");
            final Nodes.DbNode entity = Drivers.node()
                    .property("k-1", "val-1")
                    .property("k-2", "val-2")
                    .property("k-3", "val-3")
                    .build();
            // WHEN
            final Missing<Nodes.DbNode, String> result = Entities.missingPropertyKeys(entity, keys);

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
            final Nodes.DbNode entity = Drivers.node().property("k-1", "val-1").build();

            // WHEN
            final Missing<Nodes.DbNode, String> result = Entities.missingPropertyKeys(entity, keys);

            // THEN
            assertThat(result).isNotNull();
            assertThat(result.getEntity()).isSameAs(entity);
            assertThat(result.getData()).isNotNull().containsExactly("k-2", "k-3");
            assertThat(result.hasMissing()).isTrue();
        }
        
    }

    @Nested
    class ListMissingPropertyKeysTests {

        @Test
        void should_return_an_empty_list() {
            // GIVEN
            final List<String> keys = Arrays.asList("k-1", "k-2", "k-3");
            final List<Nodes.DbNode> entities = IntStream.range(0, 10)
                    .mapToObj(i -> Drivers.node()
                            .property("k-1", "val-1-" + i)
                            .property("k-2", "val-2-" + i)
                            .property("k-3", "val-3-" + i)
                            .build())
                    .collect(Collectors.toList());

            // WHEN
            final List<Missing<Nodes.DbNode, String>> result = Entities.missingPropertyKeys(entities, keys);

            // THEN
            assertThat(result).isNotNull().isEmpty();
        }

        @Test
        void should_return_a_list_of_missing_nodes() {
            // GIVEN
            final List<String> keys = Arrays.asList("k-1", "k-2", "k-3");
            final List<Nodes.DbNode> entities = Arrays.asList(
                    Drivers.node().property("k-1", "val-1-1").property("k-2", "val-1-2").build(),
                    Drivers.node().property("k-1", "val-2-1").property("k-3", "val-2-3").build(),
                    Drivers.node().property("k-2", "val-3-2").property("k-3", "val-3-3").build(),
                    Drivers.node().property("k-1", "val-4-1").build()
            );

            // WHEN
            final List<Missing<Nodes.DbNode, String>> result = Entities.missingPropertyKeys(entities, keys);

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

}
