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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.neo4j.api.beta.type.DbValue.propValue;

class ValueTypeTests {

    private static final ZonedDateTime SYDNEY_NOW = ZonedDateTime.now(ZoneId.of("Australia/Sydney"));

    static class TypeArgumentProvider implements ArgumentsProvider {

        private static <S> Arguments testCase(S object, ValueType type) {
            final Class<S> fromClass = (Class<S>) object.getClass();
            return testCase(fromClass, object, type);
        }

        private static <S> Arguments testCase(final Class<S> fromClass, S object, ValueType type) {
            return Arguments.arguments(fromClass, object, propValue(type, Values.value(object).asObject()));
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    testCase(true, ValueType.BOOLEAN),
                    testCase("value", ValueType.STRING),
                    testCase('c', ValueType.STRING),
                    testCase(42L, ValueType.INTEGER),
                    testCase((short) 42, ValueType.INTEGER),
                    testCase((byte) 42, ValueType.INTEGER),
                    testCase(42, ValueType.INTEGER),
                    testCase(3.141592653589793, ValueType.FLOAT),
                    testCase(3.14159F, ValueType.FLOAT),
                    testCase(LocalDate.class, SYDNEY_NOW.toLocalDate(), ValueType.DATE),
                    testCase(OffsetTime.class, SYDNEY_NOW.toOffsetDateTime().toOffsetTime(), ValueType.TIME),
                    testCase(LocalTime.class, SYDNEY_NOW.toLocalTime(), ValueType.LOCAL_TIME),
                    testCase(LocalDateTime.class, SYDNEY_NOW.toLocalDateTime(), ValueType.LOCAL_DATE_TIME),
                    testCase(OffsetDateTime.class, SYDNEY_NOW.toOffsetDateTime(), ValueType.DATE_TIME),
                    testCase(ZonedDateTime.class, SYDNEY_NOW, ValueType.DATE_TIME),
                    testCase(Values.isoDuration(1, 2, 3, 4).asIsoDuration(), ValueType.DURATION),
                    testCase(Period.ofYears(1).plusMonths(2).plusDays(3), ValueType.DURATION),
                    testCase(Duration.ofDays(2).plusHours(3).plusMinutes(4).plusSeconds(5).plusNanos(6), ValueType.DURATION),
                    testCase(Values.point(1, 48.868829858, 2.309832094).asPoint(), ValueType.POINT),
                    testCase(Values.point(1, 48.868829858, 2.309832094, 12.1234).asPoint(), ValueType.POINT)
            );
        }
    }

    @Nested
    @DisplayName("#convert")
    class ConvertTests {

        @ParameterizedTest(name = "[{index}] {0} : {1} => {2}")
        @ArgumentsSource(TypeArgumentProvider.class)
        void should_convert_from_target_type(Class<?> fromClass, Object object, DbValue value) {
            // WHEN
            DbValue result = ValueType.convert(object);

            // THEN
            assertThat(result)
                    .as("convert from %s into => %s", fromClass.getName(), value.getType())
                    .isEqualTo(value);
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
            map.put("k-9", SYDNEY_NOW.toLocalDate());
            map.put("k-10", SYDNEY_NOW.toOffsetDateTime().toOffsetTime());
            map.put("k-11", SYDNEY_NOW.toLocalTime());
            map.put("k-12", SYDNEY_NOW.toLocalDateTime());
            map.put("k-13", SYDNEY_NOW.toOffsetDateTime());
            map.put("k-14", SYDNEY_NOW);
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
                    .containsEntry("k-9", propValue(ValueType.DATE, SYDNEY_NOW.toLocalDate()))
                    .containsEntry("k-10", propValue(ValueType.TIME, SYDNEY_NOW.toOffsetDateTime().toOffsetTime()))
                    .containsEntry("k-11", propValue(ValueType.LOCAL_TIME, SYDNEY_NOW.toLocalTime()))
                    .containsEntry("k-12", propValue(ValueType.LOCAL_DATE_TIME, SYDNEY_NOW.toLocalDateTime()))
                    .containsEntry("k-13", propValue(ValueType.DATE_TIME, SYDNEY_NOW.toOffsetDateTime().toZonedDateTime()))
                    .containsEntry("k-14", propValue(ValueType.DATE_TIME, SYDNEY_NOW))
                    .containsEntry("k-15", propValue(ValueType.DURATION, isoDuration))
                    .containsEntry("k-16", propValue(ValueType.DURATION, Values.value(duration).asIsoDuration()))
                    .containsEntry("k-17", propValue(ValueType.DURATION, Values.value(period).asIsoDuration()))
                    .containsEntry("k-18", propValue(ValueType.POINT, point2d))
                    .containsEntry("k-19", propValue(ValueType.POINT, point3d))

            ;
        }
    }
}
