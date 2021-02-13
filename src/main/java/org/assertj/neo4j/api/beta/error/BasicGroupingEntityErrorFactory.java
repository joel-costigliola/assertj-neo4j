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
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.util.Strings;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.util.Checks;
import org.assertj.neo4j.api.beta.util.EntityUtils;
import org.assertj.neo4j.api.beta.util.Presentations;
import org.assertj.neo4j.api.beta.util.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Grouping entity error factory in design to create a new {@link ErrorMessageFactory} based on a list of failing
 * entities provided in an {@link EntityErrorMessageFactory}.
 *
 * @author Patrick Allain - 05/02/2021
 */
public class BasicGroupingEntityErrorFactory<ENTITY extends DbEntity<ENTITY>>
        implements GroupingEntityErrorFactory<ENTITY> {

    private final List<ENTITY> actual;
    private final Function<ENTITY, EntityErrorMessageFactory<ENTITY>> mapper;
    private final String format;
    private final Object[] arguments;

    /**
     * Class constructor for generating a grouping error message.
     * <p/>
     * Arguments will be concatenate at first :
     * <ol>
     *    <li>{@code %1$s} : The representation of actual entities</li>
     *    <li>{@code %2$s} : The representation of not satisfying entities</li>
     *    <li>{@code %3$s} : The plural form of the entities</li>
     *    <li>{@code %x$s} : All remaining arguments</li>
     * </ol>
     *
     * @param actual    the list of actual entities
     * @param mapper    the function to transform entities that don't satisfies the condition into a {@link
     *                  EntityErrorMessageFactory}
     * @param format    the message format
     * @param arguments any other remaining arguments
     */
    public BasicGroupingEntityErrorFactory(
            final List<ENTITY> actual,
            final Function<ENTITY, EntityErrorMessageFactory<ENTITY>> mapper,
            final String format,
            final Object... arguments) {
        this.actual = Checks.notNullOrEmpty(actual, "The list of entities cannot be null");
        this.mapper = mapper;
        this.format = format;
        this.arguments = arguments;
    }

    @Override
    public ErrorMessageFactory notSatisfies(final List<ENTITY> notSatisfies) {
        final RecordType type = Checks.first(this.actual, "The entity list cannot be empty").getRecordType();
        final List<ENTITY> sortedNotSatisfies = EntityUtils.sorted(notSatisfies);
        return new GroupingEntityErrorMessageFactory<>(
                this.format + "%n%n" + Strings.escapePercent(this.formatItems(sortedNotSatisfies)),
                EntityUtils.sorted(this.actual),
                sortedNotSatisfies,
                type,
                arguments
        );
    }

    private String formatItems(final List<ENTITY> notSatisfies) {
        return IntStream.range(0, notSatisfies.size())
                .mapToObj(idx -> this.formatItem(idx, notSatisfies.get(idx)))
                .collect(Collectors.joining(String.format("%n%n")));
    }

    private String formatItem(final int index, final ENTITY entity) {
        final List<EntityErrorMessageFactory.ArgDetail> details = this.mapper.apply(entity).details();
        return Stream
                .concat(
                        Stream.of(String.format("%d) %s", index + 1, Presentations.outputId(entity))),
                        details.stream().map(i -> String.format("  - %s: %s", i.title(), i.value()))
                )
                .map(i -> "  " + i)
                .collect(Collectors.joining(String.format("%n")));
    }

    private static class GroupingEntityErrorMessageFactory<E extends DbEntity<E>>
            extends BasicErrorMessageFactory {

        public GroupingEntityErrorMessageFactory(
                final String format,
                final List<E> actual,
                final List<E> notSatisfies,
                final RecordType type,
                Object... arguments) {
            super(format, toArguments(actual, notSatisfies, type, arguments));
        }

        private static <E extends DbEntity<E>> Object[] toArguments(
                final List<E> actual, final List<E> notSatisfies, final RecordType type, final Object[] arguments) {
            return Stream
                    .concat(
                            Stream.of(
                                    Presentations.outputIds(actual),
                                    Presentations.outputIds(notSatisfies),
                                    unquotedString(type.pluralForm())
                            ),
                            Arrays.stream(arguments)
                    ).toArray();
        }
    }

}
