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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.neo4j.driver.Values;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ValueTypeTests {

    static class SupportArgumentProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return TestCase.CASES.stream().flatMap(this::matrix);
        }

        private Stream<Arguments> matrix(TestCase tc) {
            return TestCase.CASES.stream()
                    .map(tc2 -> Arguments.arguments(
                            tc.getType(),
                            tc2.getFromClass(),
                            tc2.getObject(),
                            tc2.getType() == tc.getType(),
                            tc2.getType()
                    ));
        }
    }

    @Nested
    @DisplayName("#support")
    class SupportTests {

        @ParameterizedTest(name = "[{index}] {0} : {1}({2}) => {3} ({4})")
        @ArgumentsSource(SupportArgumentProvider.class)
        void should_pass(ArgumentsAccessor accessor) {
            // GIVEN
            final ValueType valueType = accessor.get(0, ValueType.class);
            final Class<?> fromClass = accessor.get(1, Class.class);
            final Object object = accessor.get(2);
            final Boolean expected = accessor.getBoolean(3);
            final ValueType convertedBy = accessor.get(4, ValueType.class);

            // WHEN
            final boolean result = valueType.support(object);

            // THEN
            assertThat(result)
                    .as("%s support for value %s (%s) => %s", valueType, object, fromClass, expected, convertedBy)
                    .isEqualTo(expected);
        }
    }

    static class ConvertArgumentProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return TestCase.CASES.stream().map(tc -> Arguments.arguments(
                    tc.getType(),
                    tc.getFromClass(),
                    tc.getObject(),
                    DbValue.propValue(tc.getType(), Values.value(tc.getObject()).asObject()),
                    tc.getRepresentation()
            ));
        }
    }

    @Nested
    @DisplayName("#convert")
    class ConvertTests {

        @ParameterizedTest(name = "[{index}] {0} : {1}({2}) => {3}")
        @ArgumentsSource(ConvertArgumentProvider.class)
        void should_pass(ArgumentsAccessor accessor) {
            // GIVEN
            final ValueType valueType = accessor.get(0, ValueType.class);
            final Class<?> fromClass = accessor.get(1, Class.class);
            final Object object = accessor.get(2);
            final DbValue expected = accessor.get(3, DbValue.class);

            // WHEN
            final Object result = valueType.convert(object);

            // THEN
            assertThat(result)
                    .as("%s conversion for %s (%s) => %s", valueType, object, fromClass, expected)
                    .isEqualTo(expected.getContent());
        }

        @Test
        void should_convert_list_type() {
            // GIVEN
            final List<Integer> object = Arrays.asList(1, 2, 3, 4);

            // WHEN
            final Object result = ValueType.LIST.convert(object);

            // THEN
            assertThat(result)
                    .isEqualTo(Arrays.asList(
                            DbValue.fromObject(1),
                            DbValue.fromObject(2),
                            DbValue.fromObject(3),
                            DbValue.fromObject(4)
                    ));
        }


    }

}
