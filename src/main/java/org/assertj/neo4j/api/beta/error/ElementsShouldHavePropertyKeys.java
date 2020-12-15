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

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.util.Strings;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.util.Entities;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author patouche - 26/11/2020
 */
public class ElementsShouldHavePropertyKeys<E extends DbEntity<E>> extends BasicErrorMessageFactory {

    /**
     * Creates a new <code>{@link BasicErrorMessageFactory}</code>.
     */
    public ElementsShouldHavePropertyKeys(
            final RecordType recordType, final List<E> actual, final List<String> expectedKeys,
            final List<Missing<E, String>> missingActual) {
        super("%nExpecting " + recordType.pluralForm() + ":%n"
              + "  <%s> to have all the following property keys:%n"
              + "  <%s>%n"
              + "but some property keys were missing on:%n%n"
              + Strings.escapePercent(describeItems(expectedKeys, missingActual)),
              Entities.outputIds(actual), expectedKeys);
    }

    public static <E extends DbEntity<E>> ElementsShouldHavePropertyKeys<E> create(
            final RecordType recordType, final List<E> actual, final List<String> expectedKeys,
            final List<Missing<E, String>> missingActual) {
        return new ElementsShouldHavePropertyKeys<>(recordType, actual, expectedKeys, missingActual);
    }

    private static <E extends DbEntity<E>> String describeItems(
            final List<String> expectedLabels, final List<Missing<E, String>> items) {
        return items.stream()
                .map(item -> describeItem(expectedLabels, item))
                .collect(Collectors.joining(String.format("%n%n")));
    }

    private static <E extends DbEntity<E>> String describeItem(
            final List<String> expectedKeys, final Missing<E, String> missing) {
        final E entity = missing.getEntity();
        final List<String> actualKeys = entity.getProperties().keySet().stream().sorted().collect(Collectors.toList());
        return String.format(
                "  - %s have missing property keys: %s%n"
                + "    Actual: <%s>%n"
                + "    Expected: <%s>",
                Entities.outputId(entity), missing.getData(), actualKeys, expectedKeys
        );
    }

}
