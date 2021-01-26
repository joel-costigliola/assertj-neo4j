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
import org.assertj.neo4j.api.beta.error.Missing;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.ValueType;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author patouche - 25/11/2020
 */
public final class Entities {

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
                .map(Entities::outputId)
                .collect(Collectors.toList());
    }

    /**
     * Check if a {@link DbEntity} has the a property with the given {@code key}.
     *
     * @param entity the entity to check
     * @param key    the key to search for
     * @param <E>    the type of entity
     * @return True if a property with the given key exist. False otherwise
     */
    public static <E extends DbEntity<E>> boolean hasKey(final E entity, final String key) {
        return entity.getProperties().containsKey(key);
    }

    /**
     * Check if a {@link DbEntity} has for its properties all the {@code keys}.
     *
     * @param entity the entity to check
     * @param keys   a list of key to search for
     * @param <E>    the type of entity
     * @return True if all keys can be found in properties. False otherwise
     */
    public static <E extends DbEntity<E>> boolean hasAllKeys(final E entity, final Iterable<String> keys) {
        return Streams.stream(keys).allMatch(key -> hasKey(entity, key));
    }

    /**
     * Check if all entities have the provided keys.
     *
     * @param entities the entities to check
     * @param keys     the keys to test
     * @param <E>      the entity type
     * @return true if all entities have the provided keys.
     */
    public static <E extends DbEntity<E>> boolean haveAllKeys(final List<E> entities, final Iterable<String> keys) {
        return entities.stream().allMatch(entity -> hasAllKeys(entity, keys));
    }

    public static <E extends DbEntity<E>> boolean haveAllPropertyType(final List<E> entities, final String key,
                                                                   final ValueType type) {
        throw Wip.TODO(Entities.class);
    }




}
