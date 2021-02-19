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

import org.assertj.core.description.Description;
import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.presentation.Representation;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.DbObject;
import org.assertj.neo4j.api.beta.type.ObjectType;
import org.assertj.neo4j.api.beta.util.Checks;
import org.assertj.neo4j.api.beta.util.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;

/**
 * Grouping entity error factory in design to create a new {@link ErrorMessageFactory} based on a list of failing
 * entities provided in an {@link DbErrorMessageFactory}.
 *
 * @author Patrick Allain - 05/02/2021
 */
public class BasicDbGroupingErrorFactory<ACTUAL extends DbObject<ACTUAL>> implements GroupingDbErrorFactory<ACTUAL> {

    private final List<ACTUAL> actual;
    private final Function<ACTUAL, DbErrorMessageFactory<ACTUAL>> mapper;
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
     *                  DbErrorMessageFactory}
     * @param format    the message format
     * @param arguments any other remaining arguments
     */
    public BasicDbGroupingErrorFactory(
            final List<ACTUAL> actual,
            final Function<ACTUAL, DbErrorMessageFactory<ACTUAL>> mapper,
            final String format,
            final Object... arguments) {
        this.actual = Checks.notNullOrEmpty(actual, "The list of entities cannot be null");
        this.mapper = mapper;
        this.format = format;
        this.arguments = arguments;
    }

    @Override
    public ErrorMessageFactory notSatisfies(final List<ACTUAL> notSatisfies) {
        return new GroupingEntityErrorMessageFactory<ACTUAL>(
                this.mapper,
                this.format,
                Utils.sorted(this.actual),
                Utils.sorted(notSatisfies),
                Utils.objectType(this.actual),
                arguments
        );
    }

    private static class GroupingEntityErrorMessageFactory<ACTUAL extends DbObject<ACTUAL>>
            extends BasicErrorMessageFactory {

        private final List<DbErrorMessageFactory<ACTUAL>> entityErrorMessageFactories;

        public GroupingEntityErrorMessageFactory(
                final Function<ACTUAL, DbErrorMessageFactory<ACTUAL>> mapper,
                final String format,
                final List<ACTUAL> actual,
                final List<ACTUAL> notSatisfies,
                final ObjectType type,
                Object... arguments) {
            super(format, toArguments(actual, notSatisfies, type, arguments));
            this.entityErrorMessageFactories = notSatisfies.stream().map(mapper).collect(Collectors.toList());
        }

        private static <A extends DbObject<A>> Object[] toArguments(
                final List<A> actual, final List<A> notSatisfies, final ObjectType type, final Object[] arguments) {
            return Stream
                    .concat(
                            Stream.of(actual, notSatisfies, unquotedString(type.format(actual.size()))),
                            Arrays.stream(arguments)
                    ).toArray();
        }

        private String withItemDetails(Representation representation, String message) {
            return message + String.format("%n%n") + this.formatItems(representation) + String.format("%n");
        }

        @Override
        public String create() {
            return withItemDetails(CONFIGURATION_PROVIDER.representation(), super.create());
        }

        @Override
        public String create(Description d) {
            return withItemDetails(CONFIGURATION_PROVIDER.representation(), super.create(d));
        }

        @Override
        public String create(Description d, Representation representation) {
            return withItemDetails(representation, super.create(d, representation));
        }

        private String formatItems(final Representation representation) {
            return IntStream.range(0, this.entityErrorMessageFactories.size())
                    .mapToObj(idx -> this.formatItem(idx, representation, this.entityErrorMessageFactories.get(idx)))
                    .collect(Collectors.joining(String.format("%n%n")));
        }

        private String formatItem(
                final int index,
                final Representation representation,
                final DbErrorMessageFactory<ACTUAL> dbErrorMessageFactory) {
            final List<DbErrorMessageFactory.ArgDetail> details = dbErrorMessageFactory.details();
            return Stream
                    .concat(
                            Stream.of(String.format(
                                    "%d) %s",
                                    index + 1, representation.toStringOf(dbErrorMessageFactory.actual())
                            )),
                            details.stream().map(i -> String.format(
                                    "  - %s: %s",
                                    i.title(), representation.toStringOf(i.value())
                            ))
                    )
                    .map(i -> "  " + i)
                    .collect(Collectors.joining(String.format("%n")));
        }
    }

}
