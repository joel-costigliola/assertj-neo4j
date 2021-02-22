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
package org.assertj.neo4j.api.beta.type;

import org.assertj.neo4j.api.beta.testing.Builders;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * @author Patrick Allain - 20/02/2021
 */
public class DbObjectTests {

    private static class FromValueArgumentProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return TestCase.CASES.stream().map(tc -> arguments(
                    tc.getFromClass(),
                    Values.value(tc.getObject()).getClass(),
                    Values.value(tc.getObject()),
                    tc.getType()
            ));
        }
    }

    @Nested
    class FromValueTests {

        @Test
        void should_return_null() {
            // WHEN
            final DbObject<?> result = DbObject.fromValue(null);

            // THEN
            assertThat(result).isNull();
        }

        @ParameterizedTest(name = "[{index}] {0} : {1}({2}) => {3}")
        @ArgumentsSource(FromValueArgumentProvider.class)
        void should_return_a_db_value(ArgumentsAccessor accessor) {
            // GIVEN
            final Class<?> fromClass = accessor.get(0, Class.class);
            final Class<?> valueClass = accessor.get(1, Class.class);
            final Value value = accessor.get(2, Value.class);
            final ValueType type = accessor.get(3, ValueType.class);

            // WHEN
            final DbObject<?> result = DbObject.fromValue(value);

            // THEN
            assertThat(result)
                    .as("fromValue with %s(%s) => %s", valueClass.getName(), value, type)
                    .isInstanceOf(DbValue.class)
                    .satisfies(i -> assertThat(i.objectType()).isEqualTo(ObjectType.VALUE))
                    .satisfies(i -> assertThat(((DbValue) i).getType()).isEqualTo(type));
        }

        @Test
        void should_return_a_db_node() {
            // GIVEN
            final Node node = Builders.node()
                    .id(1)
                    .labels("lbl-1", "lbl-2")
                    .property("key", Values.value("val"))
                    .build();
            final Value value = Values.value(node);

            // WHEN
            final DbObject<?> result = DbObject.fromValue(value);

            // THEN
            assertThat(result)
                    .isInstanceOf(DbNode.class)
                    .satisfies(i -> assertThat(i.objectType()).isEqualTo(ObjectType.NODE));
        }

        @Test
        void should_return_a_db_relationship() {
            // GIVEN
            final Relationship relationship = Builders
                    .relation("TYPE")
                    .id(1)
                    .properties("key", Values.value("val"))
                    .build();
            final Value value = Values.value(relationship);

            // WHEN
            final DbObject<?> result = DbObject.fromValue(value);

            // THEN
            assertThat(result)
                    .isInstanceOf(DbRelationship.class)
                    .satisfies(i -> assertThat(i.objectType()).isEqualTo(ObjectType.RELATIONSHIP));
        }

    }

}
