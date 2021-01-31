package org.assertj.neo4j.api.beta.util;

import org.assertj.core.api.Assertions;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Values;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author patouche - 30/01/2021
 */
@DisplayName("Predicates")
class PredicatesTests {

    private static final Nodes.DbNode SAMPLE_ENTITY = Drivers.node()
            .id(42)
            .property("key-boolean", true)
            .property("key-long", 42L)
            .property("key-string", "value-42")
            .property("key-duration", Values.value(Duration.ofDays(42)).asObject())
            .property("key-date", LocalDate.now())
            .property("key-date-time", ZonedDateTime.now())
            .property("key-local-date-time", LocalDateTime.now())
            .property("key-time", OffsetTime.now())
            .property("key-local-time", LocalTime.now())
            .property("key-list-long", IntStream.range(0, 42).boxed().collect(Collectors.toList()))
            .build();

    @Nested
    class PropertyKeyExistsTests {

        @Test
        void should_return_false() {
            // GIVEN
            final String key = "other-key";
            final Nodes.DbNode entity = Drivers.node().property("key", "value").build();
            final Predicate<Nodes.DbNode> predicate = Predicates.<Nodes.DbNode>propertyKeyExists(key);

            // WHEN
            final boolean result = predicate.test(entity);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final String key = "key";
            final Nodes.DbNode entity = Drivers.node().property("key", "value").build();
            final Predicate<Nodes.DbNode> predicate = Predicates.<Nodes.DbNode>propertyKeyExists(key);

            // WHEN
            final boolean result = predicate.test(entity);

            // THEN
            assertThat(result).isTrue();
        }
    }

    @Nested
    class PropertyKeysExistsTests {

        @Test
        void should_return_false() {
            // GIVEN
            final List<String> keys = Arrays.asList("key-1", "key-2", "key-3", "other-key");
            final Predicate<Nodes.DbNode> predicate = Predicates.<Nodes.DbNode>propertyKeysExists(keys);

            // WHEN
            final boolean result = predicate.test(SAMPLE_ENTITY);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final List<String> keys = Arrays.asList("key-boolean", "key-long", "key-list-long");
            final Predicate<Nodes.DbNode> predicate = Predicates.<Nodes.DbNode>propertyKeysExists(keys);

            // WHEN
            final boolean result = predicate.test(SAMPLE_ENTITY);

            // THEN
            assertThat(result).isTrue();
        }
    }

    @Nested
    class PropertyValueTypeTests {

        @Test
        void should_return_false() {
            // GIVEN
            final List<String> keys = Arrays.asList("key-1", "key-2", "key-3", "other-key");

            // WHEN
            final boolean result = Predicates.<Nodes.DbNode>propertyKeysExists(keys).test(SAMPLE_ENTITY);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final Predicate<Nodes.DbNode> predicate = Predicates.propertyValueType("key-duration", ValueType.DURATION);

            // WHEN
            final boolean result = predicate.test(SAMPLE_ENTITY);

            // THEN
            assertThat(result).isTrue();
        }
    }

    @Nested
    class PropertyValueInstanceOfTests {

        @Test
        void should_return_false() {
            // GIVEN
            final Predicate<Nodes.DbNode> predicate = Predicates
                    .propertyValueInstanceOf("key-date-time", LocalDateTime.class);

            // WHEN
            final boolean result = predicate.test(SAMPLE_ENTITY);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final Predicate<Nodes.DbNode> predicate = Predicates
                    .propertyValueInstanceOf("key-date-time", ZonedDateTime.class);

            // WHEN
            final boolean result = predicate.test(SAMPLE_ENTITY);

            // THEN
            assertThat(result).isTrue();
        }

    }

    @Nested
    class PropertyValueTests {

        @Test
        void should_return_false() {
            // GIVEN
            final Predicate<Nodes.DbNode> predicate = Predicates.propertyValue("key-string", "bad-value");

            // WHEN
            final boolean result = predicate.test(SAMPLE_ENTITY);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final Predicate<Nodes.DbNode> predicate = Predicates.propertyValue("key-string", "value-42");

            // WHEN
            final boolean result = predicate.test(SAMPLE_ENTITY);

            // THEN
            assertThat(result).isTrue();
        }

    }

    @Nested
    class PropertyListContainsValueTypeTests {

        @Test
        void should_return_false() {
            // GIVEN
            final Predicate<Nodes.DbNode> predicate = Predicates
                    .propertyListContainsValueType("key-list-long", ValueType.STRING);

            // WHEN
            final boolean result = predicate.test(SAMPLE_ENTITY);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final Predicate<Nodes.DbNode> predicate = Predicates
                    .propertyListContainsValueType("key-list-long", ValueType.INTEGER);

            // WHEN
            final boolean result = predicate.test(SAMPLE_ENTITY);

            // THEN
            assertThat(result).isTrue();
        }
    }

    @Nested
    class XxxTests {

        @Test
        void should_return_true() {
            // GIVEN
            // WHEN
            // THEN
            Assertions.fail("TODO");
        }

        @Test
        void should_return_false() {
            // GIVEN
            // WHEN
            // THEN
            Assertions.fail("TODO");
        }

    }

}
