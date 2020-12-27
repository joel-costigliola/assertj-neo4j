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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

/**
 * Describe all the possible type that can be stored as property.
 * <p/>
 * For more information, you can consult the documentation page related to neo4j types:
 * <ul>
 *     <li>
 *         <a href='https://neo4j.com/docs/cypher-manual/current/syntax/values/#property-types'>property types</a>
 *     </li>
 * </ul>
 *
 * @author patouche - 27/11/2020
 */
public enum ValueType {

    INTEGER(Long.class, Function.identity(),
            new Converter<>(Integer.class, Integer::longValue),
            new Converter<>(Short.class, Short::longValue),
            new Converter<>(Byte.class, Byte::longValue)
    ),

    FLOAT(Double.class, Function.identity(), new Converter<>(Float.class, Float::doubleValue)),

    STRING(String.class, Function.identity(), new Converter<>(Character.class, Object::toString)),

    BOOLEAN(Boolean.class, Function.identity()),

    DATE(LocalDate.class, Function.identity()),

    DATE_TIME(ZonedDateTime.class, Function.identity(), new Converter<>(OffsetDateTime.class, OffsetDateTime::toZonedDateTime)),

    LOCAL_DATE_TIME(LocalDateTime.class, Function.identity()),

    TIME(OffsetTime.class, Function.identity()),

    LOCAL_TIME(LocalTime.class, Function.identity()),

    DURATION(IsoDuration.class, Function.identity(),
            new Converter<>(Duration.class, i -> Values.value(i).asIsoDuration()),
            new Converter<>(Period.class, i -> Values.value(i).asIsoDuration())
    ),

    POINT(Point.class, Function.identity()),

    LIST(List.class, listFactory());

    private final Class<?> targetClass;
    private final Function<Object, Object> objectFactory;
    private final List<Converter<?, ?>> converters;

    /**
     * Enum constructor.
     *
     * @param targetClass
     * @param converters
     * @param <T>
     */
    <T> ValueType(final Class<T> targetClass, final Function<T, ?> objectFactory, final Converter<?, T>... converters) {
        this.targetClass = targetClass;
        this.objectFactory = (Function<Object, Object>) objectFactory;
        this.converters = converters(targetClass, converters);
    }

    private static <T> List<Converter<?, ?>> converters(final Class<T> targetClass, Converter<?, T>[] converters) {
        return concat(
                Stream.of(new Converter<>(targetClass, Function.identity())),
                Arrays.stream(converters)
        ).collect(Collectors.toList());
    }

    private static Function<List, List<DbValue>> listFactory() {
        return l -> (List<DbValue>) l.stream().map(ValueType::convert).collect(Collectors.toList());
    }

    /**
     * Convert a object into a {@link DbValue}.
     *
     * @param object the object to convert
     * @param <T>    the object type
     * @return a new instance of {@link DbValue}
     */
    static <T> DbValue convert(final T object) {
        if (object == null) {
            return null;
        }

        // Convert property types - aka simple types.
        for (final ValueType value : values()) {
            for (Converter<?, ?> converter : value.converters) {
                if (converter.fromClass.isInstance(object)) {
                    final Object converted = converter.convert(object);
                    return DbValue.propValue(value, value.objectFactory.apply(converted));
                }
            }
        }

        return null;
    }

    /**
     * Convert a {@code properties} {@link Map} into a {@link DbValue} map.
     *
     * @param properties the properties map to be converted
     * @return a map having the same keys than the provided one with {@link DbValue} values.
     */
    static Map<String, DbValue> convertMap(final Map<String, Object> properties) {
        Map<String, DbValue> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            result.put(entry.getKey(), convert(entry.getValue()));
        }
        return result;
    }

    static class Converter<I, O> {

        private final Class<I> fromClass;
        private final Function<I, O> function;

        public Converter(final Class<I> fromClass, final Function<I, O> function) {
            this.fromClass = fromClass;
            this.function = function;
        }

        @SuppressWarnings("unchecked")
        public O convert(final Object input) {
            if (!fromClass.isInstance(input)) {
                throw new UnsupportedOperationException("Cannot convert object " + input + " as it isn't a instance "
                                                        + "of " + fromClass);
            }
            return this.function.apply((I) input);
        }
    }

}
