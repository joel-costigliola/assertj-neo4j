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

import org.assertj.core.util.Preconditions;
import org.assertj.neo4j.api.beta.testing.Samples;
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
import java.util.List;

/**
 * @author Patrick Allain - 18/02/2021
 */
class TestCase {

    public static final List<TestCase> CASES = Arrays.asList(
            new TestCase(
                    ValueType.BOOLEAN,
                    Boolean.class,
                    true,
                    "BOOLEAN{true}"
            ),
            new TestCase(
                    ValueType.STRING,
                    String.class,
                    "value",
                    "STRING{\"value\"}"
            ),
            new TestCase(
                    ValueType.STRING,
                    Character.class,
                    'c',
                    "STRING{\"c\"}"
            ),
            new TestCase(
                    ValueType.INTEGER, Long.class,
                    42L,
                    "INTEGER{42}"
            ),
            new TestCase(
                    ValueType.INTEGER,
                    Short.class,
                    (short) 42,
                    "INTEGER{42}"
            ),
            new TestCase(
                    ValueType.INTEGER,
                    Byte.class,
                    (byte) 42,
                    "INTEGER{42}"
            ),
            new TestCase(
                    ValueType.INTEGER,
                    Integer.class,
                    42,
                    "INTEGER{42}"
            ),
            new TestCase(
                    ValueType.FLOAT,
                    Double.class,
                    3.141592653589793,
                    "FLOAT{3.141592653589793}"
            ),
            new TestCase(
                    ValueType.FLOAT,
                    Float.class,
                    3.14159F,
                    "FLOAT{3.141590118408203}"
            ),
            new TestCase(
                    ValueType.DATE,
                    LocalDate.class,
                    Samples.LOCAL_DATE,
                    "DATE{2020-02-03}"
            ),
            new TestCase(
                    ValueType.TIME,
                    OffsetTime.class,
                    Samples.OFFSET_TIME,
                    "TIME{04:05:06.000000007Z}"
            ),
            new TestCase(
                    ValueType.LOCAL_TIME,
                    LocalTime.class,
                    Samples.LOCAL_TIME,
                    "LOCAL_TIME{04:05:06.000000007}"
            ),
            new TestCase(
                    ValueType.LOCAL_DATE_TIME,
                    LocalDateTime.class,
                    Samples.LOCAL_DATE_TIME,
                    "LOCAL_DATE_TIME{2020-02-03T04:05:06.000000007}"
            ),
            new TestCase(
                    ValueType.DATE_TIME,
                    OffsetDateTime.class,
                    Samples.OFFSET_DATE_TIME,
                    "DATE_TIME{2020-02-03T04:05:06.000000007+11:00}"
            ),
            new TestCase(
                    ValueType.DATE_TIME,
                    ZonedDateTime.class,
                    Samples.ZONED_DATE_TIME,
                    "DATE_TIME{2020-02-03T04:05:06.000000007+11:00[Australia/Sydney]}"
            ),
            new TestCase(
                    ValueType.DURATION,
                    IsoDuration.class,
                    Values.isoDuration(1, 2, 3, 4).asIsoDuration(),
                    "DURATION{P1M2DT3.000000004S}"
            ),
            new TestCase(
                    ValueType.DURATION,
                    Period.class,
                    Period.ofYears(1).plusMonths(2).plusDays(3),
                    "DURATION{P14M3DT0S}"
            ),
            new TestCase(
                    ValueType.DURATION,
                    Duration.class,
                    Duration.ofDays(2).plusHours(3).plusMinutes(4).plusSeconds(5).plusNanos(6),
                    "DURATION{P0M0DT183845.000000006S}"
            ),
            new TestCase(
                    ValueType.POINT,
                    Point.class,
                    Values.point(1, 48.868829858, 2.309832094).asPoint(),
                    "POINT{Point{srid=1, x=48.868829858, y=2.309832094}}"
            ),
            new TestCase(
                    ValueType.POINT,
                    Point.class,
                    Values.point(1, 48.868829858, 2.309832094, 12.1234).asPoint(),
                    "POINT{Point{srid=1, x=48.868829858, y=2.309832094, z=12.1234}}"
            )
    );

    private final ValueType type;
    private final Class<?> fromClass;
    private final Object object;
    private final String representation;

    public <S> TestCase(final ValueType type, final S object, final String representation) {
        this(type, (Class<S>) object.getClass(), object, representation);
    }

    public <S> TestCase(final ValueType type, final Class<S> fromClass, final S object,
                        final String representation) {
        Preconditions.checkArgument(fromClass.isInstance(object), "Object is not instance of fromClass");
        this.type = type;
        this.fromClass = fromClass;
        this.object = object;
        this.representation = representation;
    }

    public ValueType getType() {
        return type;
    }

    public Class<?> getFromClass() {
        return fromClass;
    }

    public Object getObject() {
        return object;
    }

    public String getRepresentation() {
        return representation;
    }

}
