package org.assertj.neo4j.api.beta.util;

import org.assertj.core.util.Streams;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.DbValue;
import org.assertj.neo4j.api.beta.type.ValueType;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author patouche - 30/01/2021
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
    static <E extends DbEntity<E>> Predicate<E> propertySize(final  int size) {
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
}
