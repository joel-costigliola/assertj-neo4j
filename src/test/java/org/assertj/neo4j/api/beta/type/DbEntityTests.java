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

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.neo4j.driver.Values;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author patouche - 27/12/2020
 */
class DbEntityTests {

    private static final ZonedDateTime SAMPLE_TIME = ZonedDateTime.of(2020, 2, 3, 4, 5, 6, 7, ZoneId.of("Australia"
                                                                                                        + "/Sydney"));

    public static final Nodes.DbNode SAMPLE_NODE = Drivers.node()
            .property("boolean", true)
            .property("string", "str-value")
            .property("long", 42)
            .property("double", 4.2)
            .property("date", SAMPLE_TIME.toLocalDate())
            .property("datetime", SAMPLE_TIME)
            .property("localdatetime", SAMPLE_TIME.toLocalDateTime())
            .property("time", SAMPLE_TIME.toLocalTime().atOffset(ZoneOffset.UTC))
            .property("localtime", SAMPLE_TIME.toLocalTime())
            .property("duration", Duration.ofDays(3))
            .property("point_2d", Values.point(0, 42L, 12L).asObject())
            .property("point_3d", Values.point(0, 42L, 12L, 69L).asObject())
            .build();

    public static final Nodes.DbNode SAMPLE_LIST_NODE = Drivers.node()
            .property("list_boolean", Arrays.asList(true, false))
            .property("list_string", Arrays.asList("str-1", "str-2", "str-3"))
            .property("list_long", intList(i -> i))
            .property("list_double", Arrays.asList(1.1, 2.2, 3.3))
            .property("list_date", timeList(ZonedDateTime::toLocalDate))
            .property("list_datetime", timeList(Function.identity()))
            .property("list_localdatetime", timeList(ZonedDateTime::toLocalDateTime))
            .property("list_time", timeList(i -> i.toLocalTime().atOffset(ZoneOffset.UTC)))
            .property("list_localtime", timeList(ZonedDateTime::toLocalTime))
            .property("list_duration", intList(Duration::ofDays))
            .property("list_point_2d", intList(i -> Values.point(i, 2 * i, 3 * i)))
            .property("list_point_3d", intList(i -> Values.point(i, 2 * i, 3 * i, 4 * i)))
            .build();

    private static <T> List<T> intList(final IntFunction<T> func) {
        return IntStream.range(0, 10).mapToObj(func).collect(Collectors.toList());
    }

    private static <T> List<T> timeList(final Function<ZonedDateTime, T> func) {
        return IntStream.range(0, 10).mapToObj(SAMPLE_TIME::plusHours).map(func).collect(Collectors.toList());
    }

    @Nested
    @DisplayName("#getPropertyKeys")
    class GetPropertyKeysTests {

        @Test
        void should_return_a_list_of_sorted_the_property_keys() {
            // WHEN & THEN
            Assertions.assertThat(SAMPLE_NODE.getPropertyKeys())
                    .hasSize(12)
                    .isSorted()
                    .containsExactly(
                            "boolean",
                            "date",
                            "datetime",
                            "double",
                            "duration",
                            "localdatetime",
                            "localtime",
                            "long",
                            "point_2d",
                            "point_3d",
                            "string",
                            "time"
                    );
        }

    }

    @Nested
    @DisplayName("getPropertyValue")
    @ExtendWith(SoftAssertionsExtension.class)
    class GetPropertyValueTests {

        @Test
        void should_return_the_object_for_simple_type(final SoftAssertions softly) {
            // WHEN & THEN
            softly.assertThat(SAMPLE_NODE.getPropertyValue("boolean")).isEqualTo(true);
            softly.assertThat(SAMPLE_NODE.getPropertyValue("string")).isEqualTo("str-value");
            softly.assertThat(SAMPLE_NODE.getPropertyValue("long")).isEqualTo(42L);
            softly.assertThat(SAMPLE_NODE.getPropertyValue("double")).isEqualTo(4.2);
            softly.assertThat(SAMPLE_NODE.getPropertyValue("date")).isEqualTo(SAMPLE_TIME.toLocalDate());
            softly.assertThat(SAMPLE_NODE.getPropertyValue("datetime")).isEqualTo(SAMPLE_TIME);
            softly.assertThat(SAMPLE_NODE.getPropertyValue("localdatetime")).isEqualTo(SAMPLE_TIME.toLocalDateTime());
            softly.assertThat(SAMPLE_NODE.getPropertyValue("time")).isEqualTo(SAMPLE_TIME.toLocalTime().atOffset(ZoneOffset.UTC));
            softly.assertThat(SAMPLE_NODE.getPropertyValue("localtime")).isEqualTo(SAMPLE_TIME.toLocalTime());
            softly.assertThat(SAMPLE_NODE.getPropertyValue("duration")).isEqualTo(Values.value(Duration.ofDays(3)).asObject());
            softly.assertThat(SAMPLE_NODE.getPropertyValue("point_2d")).isEqualTo(Values.point(0, 42, 12).asPoint());
            softly.assertThat(SAMPLE_NODE.getPropertyValue("point_3d")).isEqualTo(Values.point(0, 42, 12, 69).asPoint());
        }
    }

    @Nested
    @DisplayName("getPropertyType")
    @ExtendWith(SoftAssertionsExtension.class)
    class GetPropertyTypeTests {

        @Test
        void should_return_the_right_property_type(final SoftAssertions softly) {
            // WHEN & THEN
            softly.assertThat(SAMPLE_NODE.getPropertyType("boolean")).isEqualTo(ValueType.BOOLEAN);
            softly.assertThat(SAMPLE_NODE.getPropertyType("string")).isEqualTo(ValueType.STRING);
            softly.assertThat(SAMPLE_NODE.getPropertyType("long")).isEqualTo(ValueType.INTEGER);
            softly.assertThat(SAMPLE_NODE.getPropertyType("double")).isEqualTo(ValueType.FLOAT);
            softly.assertThat(SAMPLE_NODE.getPropertyType("date")).isEqualTo(ValueType.DATE);
            softly.assertThat(SAMPLE_NODE.getPropertyType("datetime")).isEqualTo(ValueType.DATE_TIME);
            softly.assertThat(SAMPLE_NODE.getPropertyType("localdatetime")).isEqualTo(ValueType.LOCAL_DATE_TIME);
            softly.assertThat(SAMPLE_NODE.getPropertyType("time")).isEqualTo(ValueType.TIME);
            softly.assertThat(SAMPLE_NODE.getPropertyType("localtime")).isEqualTo(ValueType.LOCAL_TIME);
            softly.assertThat(SAMPLE_NODE.getPropertyType("duration")).isEqualTo(ValueType.DURATION);
            softly.assertThat(SAMPLE_NODE.getPropertyType("point_2d")).isEqualTo(ValueType.POINT);
            softly.assertThat(SAMPLE_NODE.getPropertyType("point_3d")).isEqualTo(ValueType.POINT);
        }

    }

    @Nested
    @DisplayName("getPropertyList")
    @ExtendWith(SoftAssertionsExtension.class)
    class GetPropertyListTests {

        @Test
        void should_throw_an_exception(final SoftAssertions softly) {
            // WHEN & THEN
            softly.assertThatThrownBy(() -> SAMPLE_LIST_NODE.getPropertyList("key_doesnt_exist"))
                    .hasMessage("Property key \"key_doesnt_exist\" doesn't exist")
                    .hasNoCause();
        }

        @Test
        void should_return_the_right_property_type(final SoftAssertions softly) {
            // WHEN & THEN
            softly.assertThat(SAMPLE_LIST_NODE.getPropertyList("list_long"))
                    .hasSize(10)
                    .contains(IntStream.range(0, 10).mapToObj(ValueType::convert).toArray(DbValue[]::new));
            softly.assertThat(SAMPLE_LIST_NODE.getPropertyList("list_string"))
                    .hasSize(3)
                    .contains(ValueType.convert("str-1"), ValueType.convert("str-2"), ValueType.convert("str-3"));
        }

    }

}
