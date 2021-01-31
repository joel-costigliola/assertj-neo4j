package org.assertj.neo4j.api.beta.error;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.RecordType;

import java.util.List;

/**
 * @author patouche - 01/02/2021
 */
public class ElementsShouldHavePropertySize<E extends DbEntity<E>>
        extends DbEntitiesMessageFactory<E, Integer, E> {

    public ElementsShouldHavePropertySize(RecordType recordType, List<E> actual, List<E> notSatisfies, int size) {
        super(recordType, "title", "but", actual, size, notSatisfies, (e, v) -> "todo");
    }

    public static <E extends DbEntity<E>> ErrorMessageFactory create(
            RecordType recordType, List<E> actual, List<E> notSatisfies, int size) {
        return new ElementsShouldHavePropertySize<>(recordType, actual, notSatisfies, size);
    }
}
