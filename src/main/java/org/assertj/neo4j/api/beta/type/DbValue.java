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

import org.assertj.neo4j.api.beta.util.Utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Wrap an object within its type.
 *
 * @author Patrick Allain - 23/12/2020
 */
public class DbValue extends DbObject<DbValue> {

    private static final Comparator<DbValue> VALUE_COMPARATOR = Utils.comparators(
            DbObject::objectType,
            DbValue::getContent,
            DbValue::detailed
    );

    private final ValueType type;
    private final Object content;

    DbValue(final ValueType type, final Object content) {
        this(ObjectType.VALUE, type, content);
    }

    DbValue(final ObjectType objectType, final ValueType type, final Object content) {
        super(objectType, DbValue.class);
        this.type = Objects.requireNonNull(type);
        this.content = content;
    }

    static DbValue propValue(final ValueType type, final Object value) {
        return new DbValue(ObjectType.VALUE, type, value);
    }

    /**
     * Convert a object into a {@link DbValue}.
     *
     * @param object the object to convert
     * @param <T>    the object type
     * @return a new instance of {@link DbValue}
     */
    public static <T> DbValue fromObject(final T object) {
        if (object == null) {
            return null;
        }

        return Arrays.stream(ValueType.values())
                .filter(v -> v.support(object))
                .map(v -> new DbValue(ObjectType.VALUE, v, v.convert(object)))
                .filter(dbValue -> dbValue.getContent() != null)
                .findFirst()
                .orElse(null);
    }

    /**
     * Convert a {@code properties} {@link Map} into a {@link DbValue} map.
     *
     * @param properties the properties map to be converted
     * @return a map having the same keys than the provided one with {@link DbValue} values.
     */
    public static Map<String, DbValue> fromMap(final Map<String, Object> properties) {
        Map<String, DbValue> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            result.put(entry.getKey(), fromObject(entry.getValue()));
        }
        return result;
    }

    @Override
    public String abbreviate() {
        return objectType.name();
    }

    @Override
    public String detailed() {
        return objectType.name();
    }

    @Override
    public int compareTo(final DbValue other) {
        Comparator<DbValue> comparator = Comparator.comparing(DbValue::getType);
        if (this.getType() == other.getType()) {
            comparator = VALUE_COMPARATOR;
        }
        return comparator.compare(this, other);
    }

    public ValueType getType() {
        return type;
    }

    public Object getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DbValue that = (DbValue) o;
        return type == that.type && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, content);
    }

    @Override
    public String toString() {
        return type.format(content);
    }

}
