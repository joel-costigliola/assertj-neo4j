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

import org.assertj.core.util.Streams;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.util.Presentations;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author patouche - 26/11/2020
 */
public class ElementsShouldHavePropertyKeys<E extends DbEntity<E>>
        extends DbEntitiesMessageFactory<E, List<String>, Missing<E, String>> {

    private ElementsShouldHavePropertyKeys(
            final RecordType recordType, final List<E> actual, final List<String> expectedKeys) {
        super(
                recordType,
                "to have all the following property keys",
                "but some property keys were missing on",
                actual,
                expectedKeys,
                missingPropertyKeys(actual, expectedKeys),
                itemFactory()
        );
    }

    static <E extends DbEntity<E>> Missing<E, String> missingPropertyKeys(final E entity, final Iterable<String> keys) {
        final List<String> entityKeys = entity.getPropertyKeys();
        final List<String> missingKeys = Streams.stream(keys)
                .filter(expectedLabel -> !entityKeys.contains(expectedLabel))
                .collect(Collectors.toList());
        return new Missing<>(entity, missingKeys);
    }

    static <E extends DbEntity<E>> List<Missing<E, String>> missingPropertyKeys(
            final List<E> entities, final Iterable<String> keys) {
        return entities.stream()
                .map(node -> missingPropertyKeys(node, keys))
                .filter(Missing::hasMissing)
                .collect(Collectors.toList());
    }

    private static <E extends DbEntity<E>> ItemMessageFactory<List<String>, Missing<E, String>> itemFactory() {
        return (expected, missing) -> String.format(
                "  - %s have missing property keys: %s%n"
                + "      Actual  : <%s>%n"
                + "      Expected: <%s>",
                Presentations.outputId(missing.getEntity()),
                missing.getData(),
                missing.getEntity().getPropertyKeys(),
                expected
        );
    }

    public static <E extends DbEntity<E>> ElementsShouldHavePropertyKeys<E> create(
            final RecordType recordType, final List<E> actual, final List<String> expectedKeys) {
        return new ElementsShouldHavePropertyKeys<>(recordType, actual, expectedKeys);
    }

}
