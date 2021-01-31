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
package org.assertj.neo4j.api.beta.util;

import org.assertj.core.util.Streams;
import org.assertj.neo4j.api.beta.type.DbEntity;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author patouche - 25/11/2020
 */
public final class Presentations {

    /**
     * Provide a representation of a entity with its {@link E#getId()}. This can be use to format error message.
     *
     * @param entity the entity
     * @param <E>    the type of entity
     * @return a string representing the entity with its {@link E#getId()}.
     */
    public static <E extends DbEntity<E>> String outputId(final E entity) {
        return entity.getRecordType() + "{id=" + entity.getId() + "}";
    }

    /**
     * Provide a representation of entities with theirs {@link E#getId()}. This can be use to format error message.
     *
     * @param entities the entities
     * @param <E>      the type of entity
     * @return a list of string representing entities with theirs {@link E#getId()}.
     */
    public static <E extends DbEntity<E>> List<String> outputIds(final Iterable<E> entities) {
        return Streams.stream(entities)
                .sorted(Comparator.comparing(i -> Optional.ofNullable(i.getId()).orElse(Long.MIN_VALUE)))
                .map(Presentations::outputId)
                .collect(Collectors.toList());
    }

}
