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
package org.assertj.neo4j.api.beta.testing;

import org.assertj.neo4j.api.beta.type.DbNode;
import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.assertj.neo4j.api.beta.type.Entities;
import org.neo4j.driver.Values;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
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
 * @author Patrick Allain - 10/02/2021
 */
public final class Samples {

    public static final List<String> STRING_LIST =
            IntStream.rangeClosed(0, 10).mapToObj(i -> "str-" + i).collect(Collectors.toList());

    public static final ZonedDateTime ZONED_DATE_TIME = ZonedDateTime
            .of(2020, 2, 3, 4, 5, 6, 7, ZoneId.of("Australia/Sydney"));

    public static final LocalDateTime LOCAL_DATE_TIME = ZONED_DATE_TIME.toLocalDateTime();

    public static final LocalDate LOCAL_DATE = ZONED_DATE_TIME.toLocalDate();

    public static final OffsetTime OFFSET_TIME = ZONED_DATE_TIME.toLocalTime().atOffset(ZoneOffset.UTC);

    public static final OffsetDateTime OFFSET_DATE_TIME = ZONED_DATE_TIME.toOffsetDateTime();

    public static final LocalTime LOCAL_TIME = ZONED_DATE_TIME.toLocalTime();

    public static final List<String> LABELS = Randomize.listOf("LBL_1", "LBL_2", "LBL_3", "LBL_4");

    public static final DbNode NODE = Entities.node()
            .id(42)
            .labels(LABELS.toArray(new String[0]))
            .property("boolean", true)
            .property("string", "str-value")
            .property("long", 42)
            .property("double", 4.2)
            .property("date", Samples.LOCAL_DATE)
            .property("datetime", Samples.ZONED_DATE_TIME)
            .property("localdatetime", Samples.LOCAL_DATE_TIME)
            .property("time", Samples.OFFSET_TIME)
            .property("localtime", Samples.LOCAL_TIME)
            .property("duration", Duration.ofDays(3))
            .property("point_2d", Values.point(0, 42L, 12L).asObject())
            .property("point_3d", Values.point(0, 42L, 12L, 69L).asObject())
            .build();

    public static final DbRelationship RELATIONSHIP = Entities.relationship()
            .id(42)
            .type("SAMPLE_TYPE")
            .property("boolean", true)
            .property("string", "str-value")
            .property("long", 42)
            .property("double", 4.2)
            .property("date", Samples.LOCAL_DATE)
            .property("datetime", Samples.ZONED_DATE_TIME)
            .property("localdatetime", Samples.LOCAL_DATE_TIME)
            .property("time", Samples.OFFSET_TIME)
            .property("localtime", Samples.LOCAL_TIME)
            .property("duration", Duration.ofDays(3))
            .property("point_2d", Values.point(0, 42L, 12L).asObject())
            .property("point_3d", Values.point(0, 42L, 12L, 69L).asObject())
            .build();

    public static final DbNode NODE_LIST = Entities.node()
            .id(69)
            .labels(LABELS.toArray(new String[0]))
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
        return IntStream.range(0, 10).mapToObj(Samples.ZONED_DATE_TIME::plusHours).map(func).collect(Collectors.toList());
    }

}
