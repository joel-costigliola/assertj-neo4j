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
import org.assertj.neo4j.api.beta.util.Presentations;

import java.util.List;
import java.util.stream.Collectors;

/**
 * See how the
 */
public abstract class DbEntitiesMessageFactory<E extends DbEntity<E>, EXPECTED, ITEM> extends BasicErrorMessageFactory {

    /**
     * Creates a new <code>{@link BasicErrorMessageFactory}</code>.
     */
    protected DbEntitiesMessageFactory(
            final RecordType recordType,
            final String title,
            final String but,
            final List<E> actual, final EXPECTED expected,
            final List<ITEM> items,
            final ItemMessageFactory<EXPECTED, ITEM> factory) {
        super(
                "%nExpecting %s:%n"
                + "  %s%n"
                + "%s:%n"
                + "  %s%n"
                + "%s:%n%n"
                + Strings.escapePercent(describeItems(expected, items, factory)),
                unquotedString(recordType.pluralForm()),
                Presentations.outputIds(actual),
                unquotedString(title),
                expected,
                unquotedString(but)
        );
    }

    private static <EXPECTED, ELEMENT> String describeItems(
            final EXPECTED expected, final List<ELEMENT> items,
            final ItemMessageFactory<EXPECTED, ELEMENT> factory) {
        return items.stream()
                .map(item -> factory.describeItem(expected, item))
                .collect(Collectors.joining(String.format("%n%n")));
    }

    @FunctionalInterface
    interface ItemMessageFactory<EXPECTED, ITEM> {

        String describeItem(EXPECTED expected, ITEM item);

    }

}
