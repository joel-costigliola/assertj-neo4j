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
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.util.Presentations;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Basic entity error message factory.
 *
 * @param <ENTITY> the entity type
 * @author Patrick Allain - 03/02/2021
 */
class BasicEntityErrorMessageFactory<ENTITY extends DbEntity>
        extends BasicErrorMessageFactory
        implements EntityErrorMessageFactory<ENTITY> {

    private final ENTITY actual;

    private final List<ArgDetail> details;

    /**
     * Creates a new  {@link BasicErrorMessageFactory} for the {@code actual} {@link ENTITY}.
     * <p/>
     *
     * @param format  the format string.
     * @param actual  the actual entity involve.
     * @param details details error that will be used as argument for formatting and preprocessing.
     */
    protected BasicEntityErrorMessageFactory(final String format, final ENTITY actual, final ArgDetail... details) {
        super(format, toArguments(actual, details));
        this.actual = actual;
        this.details = Arrays.stream(details).filter(ArgDetail::isIncluded).collect(Collectors.toList());
    }

    /**
     * Create the arguments for the message formatter.
     *
     * @param entity  the entity.
     * @param details the error details
     * @return an array for the provided arguments
     */
    protected static <E extends DbEntity> Object[] toArguments(final E entity, final ArgDetail... details) {
        return Stream
                .concat(
                        Stream.of(unquotedString(Presentations.outputId(entity))),
                        Arrays.stream(details).map(ArgDetail::value)
                )
                .toArray();
    }

    /** {@inheritDoc} */
    @Override
    public ENTITY entity() {
        return this.actual;
    }

    @Override
    public List<ArgDetail> details() {
        return details;
    }
}
