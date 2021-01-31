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
package org.assertj.neo4j.api.beta.error;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.assertj.neo4j.api.beta.util.Presentations;

import java.util.List;
import java.util.stream.Collectors;

public class ElementsShouldHavePropertyValueType<E extends DbEntity<E>>
        extends DbEntitiesMessageFactory<E, ValueType, E> {

    private ElementsShouldHavePropertyValueType(final RecordType recordType, final List<E> actual, final String key,
                                                final ValueType expectedType) {
        super(recordType,
                String.format("to have property \"%s\" with type", key),
                String.format("but some %s have for the property \"%s\" another type", recordType.pluralForm(), key),
                actual,
                expectedType,
                actual.stream().filter(e -> e.getPropertyType(key) != expectedType).collect(Collectors.toList()),
                itemFactory(key)
        );
    }

    private static <E extends DbEntity<E>> ItemMessageFactory<ValueType, E> itemFactory(final String key) {
        return (expected, entity) -> String.format(
                "  - %s have property \"%s\" of type:%n"
                + "      Expected: %s%n"
                + "      Actual  : %s%n"
                + "      Value   : %s",
                Presentations.outputId(entity), key, expected, entity.getPropertyType(key), entity.getPropertyValue(key)
        );
    }

    public static <E extends DbEntity<E>> ErrorMessageFactory create(
            final RecordType recordType,
            final List<E> actual,
            final String key,
            final ValueType expectedType) {
        return new ElementsShouldHavePropertyValueType<>(recordType, actual, key, expectedType);
    }
}
