package org.assertj.neo4j.api.beta.error;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.RecordType;

import java.util.List;

/**
 * @author patouche - 30/01/2021
 */
public class ElementsShouldHavePropertyInstanceOf<E extends DbEntity<E>>
        extends DbEntitiesMessageFactory<E, Class<?>, E> {

    protected ElementsShouldHavePropertyInstanceOf(RecordType recordType, List<E> actual,
                                                   String key, Class<?> expectedClass) {
        super(recordType, "title", "but", actual, expectedClass, actual, (e, v) -> "TODO");
    }

    public static <E extends DbEntity<E>> ErrorMessageFactory create(RecordType recordType, List<E> actual,
                                                                     String key, Class<?> expectedClass) {
        return new ElementsShouldHavePropertyInstanceOf<>(recordType, actual, key, expectedClass);
    }
}
