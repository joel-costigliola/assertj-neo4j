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

import org.assertj.neo4j.api.beta.testing.Samples;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.neo4j.driver.Values;

import java.time.Duration;
import java.time.Period;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 16/02/2021
 */
class DbValueTests {

    static class TypeArgumentProvider implements ArgumentsProvider {

        private static <S> Arguments testCase(final S object, final String representation) {
            final Class<S> fromClass = (Class<S>) object.getClass();
            final DbValue dbValue = ValueType.convert(object);
            return Arguments.arguments(
                    dbValue.getType(),
                    fromClass,
                    object,
                    dbValue,
                    representation
            );
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    testCase(true, "BOOLEAN{true}"),
                    testCase("value", "STRING{\"value\"}"),
                    testCase('c', "STRING{\"c\"}"),
                    testCase(42L, "INTEGER{42}"),
                    testCase((short) 42, "INTEGER{42}"),
                    testCase((byte) 42, "INTEGER{42}"),
                    testCase(42, "INTEGER{42}"),
                    testCase(3.141592653589793, "FLOAT{3.141592653589793}"),
                    testCase(3.14159F, "FLOAT{3.141590118408203}"),
                    testCase(Samples.LOCAL_DATE, "DATE{2020-02-03}"),
                    testCase(Samples.OFFSET_TIME, "TIME{04:05:06.000000007Z}"),
                    testCase(Samples.LOCAL_TIME, "LOCAL_TIME{04:05:06.000000007}"),
                    testCase(Samples.LOCAL_DATE_TIME, "LOCAL_DATE_TIME{2020-02-03T04:05:06.000000007}"),
                    testCase(Samples.OFFSET_DATE_TIME, "DATE_TIME{2020-02-03T04:05:06.000000007+11:00}"),
                    testCase(Samples.ZONED_DATE_TIME,
                            "DATE_TIME{2020-02-03T04:05:06.000000007+11:00[Australia/Sydney]}"
                    ),
                    testCase(Values.isoDuration(1, 2, 3, 4).asIsoDuration(),
                            "DURATION{P1M2DT3.000000004S}"
                    ),
                    testCase(Period.ofYears(1).plusMonths(2).plusDays(3), "DURATION{P14M3DT0S}"),
                    testCase(
                            Duration.ofDays(2).plusHours(3).plusMinutes(4).plusSeconds(5).plusNanos(6),
                            "DURATION{P0M0DT183845.000000006S}"
                    ),
                    testCase(Values.point(1, 48.868829858, 2.309832094).asPoint(),
                            "POINT{Point{srid=1, x=48.868829858, y=2.309832094}}"
                    ),
                    testCase(Values.point(1, 48.868829858, 2.309832094, 12.1234).asPoint(),
                            "POINT{Point{srid=1, x=48.868829858, y=2.309832094, z=12.1234}}"
                    ),
                    testCase(Collections.emptyList(), "LIST{EMPTY}"),
                    testCase(Arrays.asList(4, 3, 2, 1), "LIST{INTEGERS[4, 3, 2, 1]}"),
                    testCase(Arrays.asList("4", "3", "2", "1"), "LIST{STRINGS[\"4\", \"3\", \"2\", \"1\"]}")
            );
        }
    }

    @Nested
    @DisplayName("#toString")
    class FormatterTests {

        @ParameterizedTest(name = "[{index}] {0} : {1}({2}) => {4}")
        @ArgumentsSource(TypeArgumentProvider.class)
        void should_convert_from_target_type(ArgumentsAccessor accessor) {
            // GIVEN
            final DbValue value = accessor.get(3, DbValue.class);
            final String representation = accessor.getString(4);

            // WHEN
            final String result = value.toString();

            // THEN
            assertThat(result)
                    .as("format %s using object %s", value.getType(), value.getContent())
                    .isEqualTo(representation);
        }

    }
}
