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
import org.assertj.neo4j.api.beta.type.DbValue;
import org.assertj.neo4j.api.beta.type.Nodes.DbNode;
import org.assertj.neo4j.api.beta.type.Relationships.DbRelationship;
import org.assertj.neo4j.api.beta.type.ValueType;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author Patrick Allain - 30/01/2021
 */
public interface Predicates {

    /**
     * Predicate to check if a {@link DbEntity} has the a property named {@code key}.
     * <p/>
     * This predicate will return {@code true} if a property with the given key exist. {@code false} otherwise.
     *
     * @param <E> the entity type
     */
    static <E extends DbEntity<E>> Predicate<E> propertyKeyExists(final String key) {
        return (e) -> e.getProperties().containsKey(key);
    }

    /**
     * Predicate to check if a {@link DbEntity} has for its properties all the {@code keys}.
     * <p/>
     * This predicate will return {@code true} if all keys can be found in properties. {@code false} otherwise.
     *
     * @param <E> the entity type
     */
    static <E extends DbEntity<E>> Predicate<E> propertyKeysExists(final Iterable<String> keys) {
        return Streams.stream(keys)
                .map(Predicates::<E>propertyKeyExists)
                .reduce(x -> true, Predicate::and);
    }

    /**
     * Predicate to check if a {@link DbEntity} has the expected size of properties
     * <p/>
     * This predicate will return {@code true} if there is exactly the expected number of propertie. {@code false}
     * otherwise.
     *
     * @param <E> the entity type
     */
    static <E extends DbEntity<E>> Predicate<E> propertySize(final int size) {
        return (e) -> e.getProperties().size() == size;
    }

    static <E extends DbEntity<E>> Predicate<E> propertyListContainsValueType(final String key, final ValueType type) {
        return (e) -> e.getPropertyList(key)
                .stream()
                .map(DbValue::getType)
                .allMatch(t -> type == t);
    }

    static <E extends DbEntity<E>> Predicate<E> propertyValueType(final String key, final ValueType type) {
        return (e) -> Objects.equals(e.getPropertyType(key), type);
    }

    static <E extends DbEntity<E>> Predicate<E> propertyValueInstanceOf(final String key, final Class<?> clazz) {
        return (e) -> clazz.isInstance(e.getPropertyValue(key));
    }

    static <E extends DbEntity<E>> Predicate<E> propertyValue(final String key, final Object value) {
        return (e) -> Objects.equals(e.getPropertyValue(key), value);

    }

    static <E extends DbEntity<E>, T> Predicate<E> propertyValueMatch(
            final String key, final Predicate<T> predicate) {
        return (e) -> predicate.test((T) e.getPropertyValue(key));
    }

    /**
     * Predicate to check if a {@link DbNode} has the a {@code label}.
     * <p/>
     * This predicate will return {@code true} if the label exist. {@code false} otherwise.
     *
     * @return the predicate to test a node
     */
    static Predicate<DbNode> labelExists(final String label) {
        return (node) -> node.getLabels().contains(label);
    }

    /**
     * Predicate to check if a {@link DbNode} have all the {@code labels}.
     * <p/>
     * This predicate will return {@code true} if all the labels exist. {@code false} otherwise.
     *
     * @return the predicate to test a node
     */
    static Predicate<DbNode> labelsExists(final Iterable<String> labels) {
        return Streams.stream(labels)
                .map(Predicates::labelExists)
                .reduce(x -> true, Predicate::and);
    }

    /**
     * Predicate to check if a {@link DbRelationship} is of the expected {@code type}.
     * <p/>
     * This predicate will return {@code true} if the relationship is the one we expected. {@code false} otherwise.
     *
     * @return the predicate to test a relationship
     */
    static Predicate<DbRelationship> isType(final String type) {
        return (r) -> Objects.equals(r.getType(), type);
    }
}
