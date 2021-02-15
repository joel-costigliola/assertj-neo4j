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

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.assertj.neo4j.api.beta.testing.Samples;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.neo4j.driver.Values;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 27/12/2020
 */
class DbEntityTests {



    @Nested
    @DisplayName("#getPropertyKeys")
    class GetPropertyKeysTests {

        @Test
        void should_return_a_list_of_sorted_the_property_keys() {
            // WHEN & THEN
            assertThat(Samples.NODE.getPropertyKeys())
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
            softly.assertThat(Samples.NODE.getPropertyValue("boolean")).isEqualTo(true);
            softly.assertThat(Samples.NODE.getPropertyValue("string")).isEqualTo("str-value");
            softly.assertThat(Samples.NODE.getPropertyValue("long")).isEqualTo(42L);
            softly.assertThat(Samples.NODE.getPropertyValue("double")).isEqualTo(4.2);
            softly.assertThat(Samples.NODE.getPropertyValue("date")).isEqualTo(Samples.LOCAL_DATE);
            softly.assertThat(Samples.NODE.getPropertyValue("datetime")).isEqualTo(Samples.ZONED_DATE_TIME);
            softly.assertThat(Samples.NODE.getPropertyValue("localdatetime")).isEqualTo(Samples.LOCAL_DATE_TIME);
            softly.assertThat(Samples.NODE.getPropertyValue("time")).isEqualTo(Samples.TIME);
            softly.assertThat(Samples.NODE.getPropertyValue("localtime")).isEqualTo(Samples.LOCAL_TIME);
            softly.assertThat(Samples.NODE.getPropertyValue("duration")).isEqualTo(Values.value(Duration.ofDays(3)).asObject());
            softly.assertThat(Samples.NODE.getPropertyValue("point_2d")).isEqualTo(Values.point(0, 42, 12).asPoint());
            softly.assertThat(Samples.NODE.getPropertyValue("point_3d")).isEqualTo(Values.point(0, 42, 12, 69).asPoint());
        }
    }

    @Nested
    @DisplayName("getPropertyType")
    @ExtendWith(SoftAssertionsExtension.class)
    class GetPropertyTypeTests {

        @Test
        void should_return_the_right_property_type(final SoftAssertions softly) {
            // WHEN & THEN
            softly.assertThat(Samples.NODE.getPropertyType("boolean")).isEqualTo(ValueType.BOOLEAN);
            softly.assertThat(Samples.NODE.getPropertyType("string")).isEqualTo(ValueType.STRING);
            softly.assertThat(Samples.NODE.getPropertyType("long")).isEqualTo(ValueType.INTEGER);
            softly.assertThat(Samples.NODE.getPropertyType("double")).isEqualTo(ValueType.FLOAT);
            softly.assertThat(Samples.NODE.getPropertyType("date")).isEqualTo(ValueType.DATE);
            softly.assertThat(Samples.NODE.getPropertyType("datetime")).isEqualTo(ValueType.DATE_TIME);
            softly.assertThat(Samples.NODE.getPropertyType("localdatetime")).isEqualTo(ValueType.LOCAL_DATE_TIME);
            softly.assertThat(Samples.NODE.getPropertyType("time")).isEqualTo(ValueType.TIME);
            softly.assertThat(Samples.NODE.getPropertyType("localtime")).isEqualTo(ValueType.LOCAL_TIME);
            softly.assertThat(Samples.NODE.getPropertyType("duration")).isEqualTo(ValueType.DURATION);
            softly.assertThat(Samples.NODE.getPropertyType("point_2d")).isEqualTo(ValueType.POINT);
            softly.assertThat(Samples.NODE.getPropertyType("point_3d")).isEqualTo(ValueType.POINT);
        }

    }

}
