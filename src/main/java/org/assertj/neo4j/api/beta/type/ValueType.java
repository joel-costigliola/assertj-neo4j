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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Describe all the possible type that can be stored as property.
 * <p/>
 * As structural type cannot be store as property, the following type will not be represent here :
 * <ul>
 *    <li>{@link org.neo4j.driver.types.Node}</li>
 *    <li>{@link org.neo4j.driver.types.Relationship}</li>
 *    <li>{@link org.neo4j.driver.types.Path}</li>
 * </ul>
 * <p/>
 * For more information, you can consult the documentation page related to neo4j types:
 * <ul>
 *     <li><a href='https://neo4j.com/docs/cypher-manual/current/syntax/values/#property-types'>property types</a></li>
 *     <li><a href='https://neo4j.com/docs/cypher-manual/current/syntax/values/#composite-types'>composite types</a></li>
 * </ul>
 *
 * @author Patrick Allain - 27/11/2020
 */
public enum ValueType implements Converter<Object> {

    // Property types

    INTEGER(Long.class, String::valueOf, Function.identity(),
            new ConverterType<>(Integer.class, Integer::longValue),
            new ConverterType<>(Short.class, Short::longValue),
            new ConverterType<>(Byte.class, Byte::longValue)
    ),

    FLOAT(Double.class, String::valueOf, Function.identity(),
            new ConverterType<>(Float.class, Float::doubleValue)
    ),

    STRING(String.class, (v) -> String.format("\"%s\"", v), Function.identity(),
            new ConverterType<>(Character.class, Object::toString)
    ),

    BOOLEAN(Boolean.class, String::valueOf, Function.identity()),

    DATE(LocalDate.class, String::valueOf, Function.identity()),

    DATE_TIME(ZonedDateTime.class, String::valueOf, Function.identity(),
            new ConverterType<>(OffsetDateTime.class, OffsetDateTime::toZonedDateTime)
    ),

    LOCAL_DATE_TIME(LocalDateTime.class, String::valueOf, Function.identity()),

    TIME(OffsetTime.class, String::valueOf, Function.identity()),

    LOCAL_TIME(LocalTime.class, String::valueOf, Function.identity()),

    DURATION(IsoDuration.class, String::valueOf, Function.identity(),
            new ConverterType<>(Duration.class, i -> Values.value(i).asIsoDuration()),
            new ConverterType<>(Period.class, i -> Values.value(i).asIsoDuration())
    ),

    POINT(Point.class, String::valueOf, Function.identity()),

    // Composite types

    LIST(List.class, ValueType::listFormatter, ValueType::listFactory),

    ;

    // Structural types

    // NODE(DbNode.class, String::valueOf, Function.identity(),
    //         new ConverterType<>(
    //                 Node.class,
    //                 n -> new DbNode(n.id(), n.labels(), DbValue.fromMap(n.asMap()))
    //         )
    // ),
    //
    // RELATIONSHIP(DbRelationship.class, String::valueOf, Function.identity(),
    //         new ConverterType<>(
    //                 Relationship.class,
    //                 r -> new DbRelationship(r.id(), r.type(), r.startNodeId(), r.endNodeId(),
    //                         DbValue.fromMap(r.asMap())
    //                 )
    //         )
    // );

    private final Class<?> targetClass;
    private final Function<Object, String> formatter;
    private final Function<Object, Object> objectFactory;
    private final Converter<?> converter;

    /**
     * Enum constructor.
     *
     * @param targetClass   the target class.
     * @param formatter     the formatter for a string representation of a value object.
     * @param objectFactory the object factory to apply for the {@link DbValue} content.
     * @param converters    the converter that will convert from the provided input type into the target type.
     * @param <T>           the target class type
     */
    @SafeVarargs
    <T> ValueType(final Class<T> targetClass,
                  final Function<T, String> formatter,
                  final Function<T, ?> objectFactory,
                  final ConverterType<?, T>... converters) {
        this.targetClass = targetClass;
        this.formatter = (Function<Object, String>) formatter;
        this.objectFactory = (Function<Object, Object>) objectFactory;
        this.converter = aggregateConverters(targetClass, converters);
    }

    private static <T> Converter<?> aggregateConverters(final Class<T> targetClass,
                                                        final ConverterType<?, T>[] converters) {
        final List<Converter<?>> aggregatedConverters = Stream
                .concat(
                        Stream.of(new ConverterType<>(targetClass, Function.identity())),
                        Arrays.stream(converters)
                ).collect(Collectors.toList());
        return new CompositeConverter<>(aggregatedConverters);
    }

    private static <T> String listFormatter(final List<T> values) {
        if (values.isEmpty()) {
            return "EMPTY";
        }
        if (values.stream().allMatch(DbValue.class::isInstance)) {
            final List<DbValue> dbValues = values.stream().map(DbValue.class::cast).collect(Collectors.toList());
            final ValueType valueType = dbValues.stream().map(DbValue::getType).findFirst().orElse(null);
            if (valueType != null && dbValues.stream().map(DbValue::getType).allMatch(i -> i == valueType)) {
                final List<String> stringValues = dbValues.stream()
                        .map(DbValue::getContent)
                        .map(valueType.formatter)
                        .collect(Collectors.toList());
                return String.format("%sS%s", valueType, stringValues);
            }
        }
        return "UNDEFINED" + values;
    }

    private static <T> List<DbValue> listFactory(final List<T> list) {
        return list.stream().map(DbValue::fromObject).collect(Collectors.toList());
    }

    public String format(final Object value) {
        return String.format("%s{%s}", name(), this.formatter.apply(value));
    }

    @Override
    public boolean support(Object object) {
        return this.converter.support(object);
    }

    @Override
    public Object convert(Object input) {
        final Object converted = this.converter.convert(input);
        return this.objectFactory.apply(converted);
    }

}
