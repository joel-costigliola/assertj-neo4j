package org.assertj.neo4j.api.beta.error;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.RecordType;

import java.util.List;

/**
 * @author patouche - 31/01/2021
 */
public class ElementsShouldHavePropertyValue<E extends DbEntity<E>>
        extends DbEntitiesMessageFactory<E, String, E> {

    private ElementsShouldHavePropertyValue(RecordType recordType, List<E> actual, List<E> notSatisfies,
                                            String key, Object expectedValue) {
        super(
                recordType,
                "",
                "",
                actual,
                "notSatisfies", notSatisfies, (e, v) -> "todo");
    }

    public static <ENTITY extends DbEntity<ENTITY>> ErrorMessageFactory create(
            final RecordType recordType,
            final List<ENTITY> actual,
            final List<ENTITY> notSatisfies,
            final String key,
            final Object expectedValue) {
        return new ElementsShouldHavePropertyValue<>(recordType,
                actual,
                notSatisfies,
                key,
                expectedValue);
    }
}
