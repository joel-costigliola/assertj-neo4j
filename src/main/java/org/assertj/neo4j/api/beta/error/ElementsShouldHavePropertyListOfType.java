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
import org.assertj.neo4j.api.beta.type.DbValue;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.assertj.neo4j.api.beta.util.Entities;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ElementsShouldHavePropertyListOfType<E extends DbEntity<E>>
        extends DbEntitiesMessageFactory<E, ValueType, E> {

    private ElementsShouldHavePropertyListOfType(final RecordType recordType, final List<E> actual, final String key,
                                                 final ValueType expectedType) {
        super(recordType,
                String.format("to have property list \"%s\" containing type", key),
                String.format("but some %s have for the property list \"%s\" have other types",
                        recordType.pluralForm(), key),
                actual,
                expectedType,
                actual.stream()
                        .filter(e -> otherTypeInList(e, key, expectedType))
                        .collect(Collectors.toList()),
                itemFactory(key)
        );
    }

    // FIXME : Externalize ?
    private static <E extends DbEntity<E>> Stream<DbValue> streamValues(E entity, String key) {
        final Object value = entity.getPropertyValue(key);
        if (!(value instanceof List)) {
            throw new IllegalArgumentException("Should have a list value");
        }
        return ((List<DbValue>) value).stream();
    }

    private static <E extends DbEntity<E>> boolean otherTypeInList(final E entity, final String key,
                                                                   final ValueType expectedType) {
        return !streamValues(entity, key).map(DbValue::getType).allMatch(t -> t == expectedType);
    }

    private static <E extends DbEntity<E>> ItemMessageFactory<ValueType, E> itemFactory(final String key) {
        return (expected, entity) -> String.format(
                "  - %s don't have property list \"%s\" containing type:%n"
                + "      Expected: %s%n"
                + "      Actual  : %s%n"
                + "      Value   : %s",
                Entities.outputId(entity),
                key,
                expected,
                streamValues(entity, key).findFirst().map(DbValue::getType).orElse(null),
                streamValues(entity, key).map(DbValue::getContent).collect(Collectors.toList())
        );
    }

    public static <E extends DbEntity<E>> ErrorMessageFactory create(
            final RecordType recordType, final List<E> actual, final String key, ValueType expectedType) {
        return new ElementsShouldHavePropertyListOfType<>(recordType, actual, key, expectedType);
    }
}
