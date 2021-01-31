package org.assertj.neo4j.api.beta.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.ValueType;

/**
 * @author patouche - 29/01/2021
 */
public class ShouldHavePropertyWithType<ENTITY extends DbEntity<ENTITY>> extends BasicErrorMessageFactory {

    public ShouldHavePropertyWithType(final ENTITY entity, final ValueType expectedType) {
        super("", entity, expectedType);
    }

    public static <E extends DbEntity<E>> ShouldHavePropertyWithType<E> create(
            final E entity, final ValueType expectedType) {
        return new ShouldHavePropertyWithType<>(entity, expectedType);
    }
}
