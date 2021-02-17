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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.neo4j.driver.Values;
import org.neo4j.driver.types.IsoDuration;
import org.neo4j.driver.types.Point;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.neo4j.api.beta.type.DbValue.propValue;

class ValueTypeTests {

    static class TypeArgumentProvider implements ArgumentsProvider {

        private static <S> Arguments testCase(final ValueType type, final S object, final String representation) {
            final Class<S> fromClass = (Class<S>) object.getClass();
            return testCase(type, fromClass, object, representation);
        }

        private static <S> Arguments testCase(final ValueType type, final Class<S> fromClass, final S object,
                                              final String representation) {
            return Arguments.arguments(
                    type,
                    fromClass,
                    object,
                    propValue(type, Values.value(object).asObject()),
                    representation
            );
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    testCase(ValueType.BOOLEAN, true, "BOOLEAN{true}"),
                    testCase(ValueType.STRING, "value", "STRING{value}"),
                    testCase(ValueType.STRING, 'c', "STRING{c}"),
                    testCase(ValueType.INTEGER, 42L, "INTEGER{42}"),
                    testCase(ValueType.INTEGER, (short) 42, "INTEGER{42}"),
                    testCase(ValueType.INTEGER, (byte) 42, "INTEGER{42}"),
                    testCase(ValueType.INTEGER, 42, "INTEGER{42}"),
                    testCase(ValueType.FLOAT, 3.141592653589793, "FLOAT{3.141592653589793}"),
                    testCase(ValueType.FLOAT, 3.14159F, "FLOAT{3.141590118408203}"),
                    testCase(ValueType.DATE, LocalDate.class, Samples.LOCAL_DATE, "DATE{2020-02-03}"),
                    testCase(ValueType.TIME, OffsetTime.class, Samples.OFFSET_TIME, "TIME{04:05:06.000000007Z}"),
                    testCase(ValueType.LOCAL_TIME, LocalTime.class, Samples.LOCAL_TIME, "LOCAL_TIME{04:05:06"
                                                                                        + ".000000007}"),
                    testCase(ValueType.LOCAL_DATE_TIME, LocalDateTime.class, Samples.LOCAL_DATE_TIME,
                            "LOCAL_DATE_TIME{2020-02-03T04:05:06.000000007}"),
                    testCase(ValueType.DATE_TIME, OffsetDateTime.class, Samples.OFFSET_DATE_TIME, "DATE_TIME{2020-02"
                                                                                                  + "-03T04:05:06"
                                                                                                  + ".000000007+11:00"
                                                                                                  + "}"),
                    testCase(ValueType.DATE_TIME, ZonedDateTime.class, Samples.ZONED_DATE_TIME, "DATE_TIME{2020-02"
                                                                                                + "-03T04:05:06"
                                                                                                + ".000000007+11:00"
                                                                                                + "[Australia/Sydney"
                                                                                                + "]}"),
                    testCase(ValueType.DURATION, Values.isoDuration(1, 2, 3, 4).asIsoDuration(), "DURATION{P1M2DT3"
                                                                                                 + ".000000004S}"),
                    testCase(ValueType.DURATION, Period.ofYears(1).plusMonths(2).plusDays(3), "DURATION{P14M3DT0S}"),
                    testCase(ValueType.DURATION,
                            Duration.ofDays(2).plusHours(3).plusMinutes(4).plusSeconds(5).plusNanos(6),
                            "DURATION{P0M0DT183845.000000006S}"),
                    testCase(ValueType.POINT, Values.point(1, 48.868829858, 2.309832094).asPoint(), "POINT{Point{srid"
                                                                                                    + "=1, x=48"
                                                                                                    + ".868829858, "
                                                                                                    + "y=2.309832094"
                                                                                                    + "}}"),
                    testCase(ValueType.POINT, Values.point(1, 48.868829858, 2.309832094, 12.1234).asPoint(), "POINT"
                                                                                                             +
                                                                                                             "{Point"
                                                                                                             + "{srid"
                                                                                                             + "=1, "
                                                                                                             + "x=48"
                                                                                                             +
                                                                                                             ".868829858, y=2.309832094, z=12.1234}}")
            );
        }
    }

    @Nested
    @DisplayName("#convert")
    class ConvertTests {

        @ParameterizedTest(name = "[{index}] {0} : {1}({2}) => {3}")
        @ArgumentsSource(TypeArgumentProvider.class)
        void should_convert_from_target_type(ArgumentsAccessor accessor) {
            // GIVEN
            final Class<?> fromClass = accessor.get(1, Class.class);
            final Object object = accessor.get(2);
            final DbValue value = accessor.get(3, DbValue.class);

            // WHEN
            DbValue result = ValueType.convert(object);

            // THEN
            assertThat(result)
                    .as("convert from %s into => %s", fromClass.getName(), value.getType())
                    .isEqualTo(value);
        }

        @Test
        void should_convert_list_type() {
            // GIVEN
            final List<Integer> object = Arrays.asList(1, 2, 3, 4);

            // WHEN
            DbValue result = ValueType.convert(object);

            // THEN
            assertThat(result)
                    .isEqualTo(new DbValue(ValueType.LIST, Arrays.asList(
                            ValueType.convert(1),
                            ValueType.convert(2),
                            ValueType.convert(3),
                            ValueType.convert(4)
                    )));
        }
    }

    @Nested
    @DisplayName("#convertMap")
    class ConvertMapTests {

        @Test
        void should_convert_map() {
            // GIVEN
            final IsoDuration isoDuration = Values.isoDuration(1, 2, 3, 4).asIsoDuration();
            final Duration duration = Duration.ofDays(1).plusHours(2).plusMinutes(3).plusSeconds(4).plusNanos(6);
            final Period period = Period.ofYears(1).plusMonths(2).plusDays(3);
            final Point point2d = Values.point(1, 48.868829858, 2.309832094).asPoint();
            final Point point3d = Values.point(1, 48.868829858, 2.309832094, 12.1234).asPoint();

            final Map<String, Object> map = new HashMap<>();
            map.put("k-0", true);
            map.put("k-1", "value");
            map.put("k-2", 'v');
            map.put("k-3", 42L);
            map.put("k-4", 69);
            map.put("k-5", (short) 16);
            map.put("k-6", (byte) 64);
            map.put("k-7", 3.14159F);
            map.put("k-8", 3.141592653589793);
            map.put("k-9", Samples.LOCAL_DATE);
            map.put("k-10", Samples.OFFSET_TIME);
            map.put("k-11", Samples.LOCAL_TIME);
            map.put("k-12", Samples.LOCAL_DATE_TIME);
            map.put("k-13", Samples.OFFSET_DATE_TIME);
            map.put("k-14", Samples.ZONED_DATE_TIME);
            map.put("k-15", isoDuration);
            map.put("k-16", duration);
            map.put("k-17", period);
            map.put("k-18", point2d);
            map.put("k-19", point3d);
            map.put("k-20", Arrays.asList("toto", "tata"));

            // WHEN
            Map<String, DbValue> result = ValueType.convertMap(map);

            // THEN
            assertThat(result)
                    .isNotNull()
                    .hasSameSizeAs(map)
                    .containsEntry("k-0", propValue(ValueType.BOOLEAN, true))
                    .containsEntry("k-1", propValue(ValueType.STRING, "value"))
                    .containsEntry("k-2", propValue(ValueType.STRING, "v"))
                    .containsEntry("k-3", propValue(ValueType.INTEGER, 42L))
                    .containsEntry("k-4", propValue(ValueType.INTEGER, 69L))
                    .containsEntry("k-5", propValue(ValueType.INTEGER, 16L))
                    .containsEntry("k-6", propValue(ValueType.INTEGER, 64L))
                    .containsEntry("k-7", propValue(ValueType.FLOAT, 3.141590118408203))
                    .containsEntry("k-8", propValue(ValueType.FLOAT, 3.141592653589793))
                    .containsEntry("k-9", propValue(ValueType.DATE, Samples.LOCAL_DATE))
                    .containsEntry("k-10", propValue(ValueType.TIME, Samples.OFFSET_TIME))
                    .containsEntry("k-11", propValue(ValueType.LOCAL_TIME, Samples.LOCAL_TIME))
                    .containsEntry("k-12", propValue(ValueType.LOCAL_DATE_TIME, Samples.LOCAL_DATE_TIME))
                    .containsEntry("k-13", propValue(ValueType.DATE_TIME,
                            Samples.ZONED_DATE_TIME.toOffsetDateTime().toZonedDateTime()))
                    .containsEntry("k-14", propValue(ValueType.DATE_TIME, Samples.ZONED_DATE_TIME))
                    .containsEntry("k-15", propValue(ValueType.DURATION, isoDuration))
                    .containsEntry("k-16", propValue(ValueType.DURATION, Values.value(duration).asIsoDuration()))
                    .containsEntry("k-17", propValue(ValueType.DURATION, Values.value(period).asIsoDuration()))
                    .containsEntry("k-18", propValue(ValueType.POINT, point2d))
                    .containsEntry("k-19", propValue(ValueType.POINT, point3d))

            ;
        }
    }
}
