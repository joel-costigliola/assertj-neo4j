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

import org.assertj.neo4j.api.beta.type.DbNode;
import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.assertj.neo4j.api.beta.type.DbValue;
import org.assertj.neo4j.api.beta.type.Models;
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
 * @author Patrick Allain - 30/01/2021
 */
@DisplayName("Predicates")
class PredicatesTests {

    private static final DbNode SAMPLE_ENTITY = Models.node()
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

    private static final DbNode SAMPLE_NODE = Models.node()
            .id(69)
            .labels("LABEL_1", "LABEL_2", "LABEL_3")
            .build();

    private static final DbRelationship SAMPLE_RELATIONSHIP = Models.relation("TYPE")
            .id(42)
            .build();

    @Nested
    class PropertyKeyExistsTests {

        @Test
        void should_return_false() {
            // GIVEN
            final String key = "other-key";
            final DbNode entity = Models.node().property("key", "value").build();
            final Predicate<DbNode> predicate = Predicates.<DbNode>propertyKeyExists(key);

            // WHEN
            final boolean result = predicate.test(entity);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final String key = "key";
            final DbNode entity = Models.node().property("key", "value").build();
            final Predicate<DbNode> predicate = Predicates.<DbNode>propertyKeyExists(key);

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
            final Predicate<DbNode> predicate = Predicates.<DbNode>propertyKeysExists(keys);

            // WHEN
            final boolean result = predicate.test(SAMPLE_ENTITY);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final List<String> keys = Arrays.asList("key-boolean", "key-long", "key-list-long");
            final Predicate<DbNode> predicate = Predicates.<DbNode>propertyKeysExists(keys);

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
            final boolean result = Predicates.<DbNode>propertyKeysExists(keys).test(SAMPLE_ENTITY);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final Predicate<DbNode> predicate = Predicates.propertyValueType("key-duration", ValueType.DURATION);

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
            final Predicate<DbNode> predicate = Predicates
                    .propertyValueInstanceOf("key-date-time", LocalDateTime.class);

            // WHEN
            final boolean result = predicate.test(SAMPLE_ENTITY);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final Predicate<DbNode> predicate = Predicates
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
            final Predicate<DbNode> predicate = Predicates.propertyValue("key-string", "bad-value");

            // WHEN
            final boolean result = predicate.test(SAMPLE_ENTITY);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final Predicate<DbNode> predicate = Predicates.propertyValue("key-string", "value-42");

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
            final Predicate<DbNode> predicate = Predicates
                    .propertyListContainsValueType("key-list-long", ValueType.STRING);

            // WHEN
            final boolean result = predicate.test(SAMPLE_ENTITY);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final Predicate<DbNode> predicate = Predicates
                    .propertyListContainsValueType("key-list-long", ValueType.INTEGER);

            // WHEN
            final boolean result = predicate.test(SAMPLE_ENTITY);

            // THEN
            assertThat(result).isTrue();
        }
    }

    @Nested
    class NodeLabelExistsTests {

        @Test
        void should_return_false() {
            // GIVEN
            final Predicate<DbNode> predicate = Predicates.nodeLabelExists("LABEL_MISSING");

            // WHEN
            final boolean result = predicate.test(SAMPLE_NODE);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final Predicate<DbNode> predicate = Predicates.nodeLabelExists("LABEL_1");

            // WHEN
            final boolean result = predicate.test(SAMPLE_NODE);

            // THEN
            assertThat(result).isTrue();
        }

    }

    @Nested
    class NodeLabelsExistsTests {

        @Test
        void should_return_false() {
            // GIVEN
            final List<String> labels = Arrays.asList("LABEL_1", "LABEL_2", "LABEL_MISSING");
            final Predicate<DbNode> predicate = Predicates.nodeLabelsExists(labels);

            // WHEN
            final boolean result = predicate.test(SAMPLE_NODE);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final List<String> labels = Arrays.asList("LABEL_1", "LABEL_2", "LABEL_3");
            final Predicate<DbNode> predicate = Predicates.nodeLabelsExists(labels);

            // WHEN
            final boolean result = predicate.test(SAMPLE_NODE);

            // THEN
            assertThat(result).isTrue();
        }

    }

    @Nested
    class RelationshipIsOfTypeTests {

        @Test
        void should_return_false() {
            // GIVEN
            final Predicate<DbRelationship> predicate = Predicates.relationshipIsOfType("OTHER_TYPE");

            // WHEN
            final boolean result = predicate.test(SAMPLE_RELATIONSHIP);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final Predicate<DbRelationship> predicate = Predicates.relationshipIsOfType("TYPE");

            // WHEN
            final boolean result = predicate.test(SAMPLE_RELATIONSHIP);

            // THEN
            assertThat(result).isTrue();
        }

    }

    @Nested
    class IsAnyOfTypesTests {

        @Test
        void should_return_false() {
            // GIVEN
            final Predicate<DbRelationship> predicate = Predicates.isAnyOfTypes("TYPE_1", "TYPE_2", "TYPE_3");

            // WHEN
            final boolean result = predicate.test(SAMPLE_RELATIONSHIP);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final Predicate<DbRelationship> predicate = Predicates.isAnyOfTypes("TYPE_1", "TYPE", "TYPE_2");

            // WHEN
            final boolean result = predicate.test(SAMPLE_RELATIONSHIP);

            // THEN
            assertThat(result).isTrue();
        }

    }

    @Nested
    class IsValueTypeTests {

        @Test
        void should_return_false() {
            // GIVEN
            final Predicate<DbValue> predicate = Predicates.isValueType(ValueType.FLOAT);

            // WHEN
            final boolean result = predicate.test(DbValue.fromObject("str"));

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final Predicate<DbValue> predicate = Predicates.isValueType(ValueType.FLOAT);

            // WHEN
            final boolean result = predicate.test(DbValue.fromObject(3.14));

            // THEN
            assertThat(result).isTrue();
        }

    }

    @Nested
    class IsValueInstanceOfTest {

        @Test
        void should_return_false() {
            // GIVEN
            final Predicate<DbValue> predicate = Predicates.isValueInstanceOf(String.class);

            // WHEN
            final boolean result = predicate.test(DbValue.fromObject(true));

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final Predicate<DbValue> predicate = Predicates.isValueInstanceOf(String.class);

            // WHEN
            final boolean result = predicate.test(DbValue.fromObject("str"));

            // THEN
            assertThat(result).isTrue();
        }

    }


    @Nested
    class XxxTests {

        @Test
        void should_return_false() {
            // GIVEN
            final Predicate<Object> predicate = (x) -> false;

            // WHEN
            final boolean result = predicate.test(SAMPLE_NODE);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final Predicate<Object> predicate = (x) -> true;

            // WHEN
            final boolean result = predicate.test(SAMPLE_NODE);

            // THEN
            assertThat(result).isTrue();
        }

    }

}
