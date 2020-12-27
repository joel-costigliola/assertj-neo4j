package org.assertj.neo4j.api.beta.error;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.assertj.neo4j.api.beta.util.Entities;

import java.util.List;
import java.util.stream.Collectors;

public class ElementsShouldHavePropertyWithType<E extends DbEntity<E>>
        extends DbEntitiesMessageFactory<E, ValueType, E> {

    private ElementsShouldHavePropertyWithType(final RecordType recordType, final List<E> actual, final String key,
                                               final ValueType expectedType) {
        super(recordType,
                String.format("to have property \"%s\" with type", key),
                String.format("but some %s have for the property \"%s\" another type", recordType.pluralForm(), key),
                actual,
                expectedType,
                actual.stream().filter(e -> e.getPropertyType(key) != expectedType).collect(Collectors.toList()),
                itemFactory(key)
        );
    }

    private static <E extends DbEntity<E>> ItemMessageFactory<ValueType, E> itemFactory(final String key) {
        return (expected, entity) -> String.format(
                "  - %s have property \"%s\" of type:%n"
                + "      Expected: %s%n"
                + "      Actual  : %s%n"
                + "      Value   : %s",
                Entities.outputId(entity), key, expected, entity.getPropertyType(key), entity.getPropertyValue(key)
        );
    }

    public static <E extends DbEntity<E>> ErrorMessageFactory create(
            final RecordType recordType, final List<E> actual, final String key, final ValueType expectedType) {
        return new ElementsShouldHavePropertyWithType<>(recordType, actual, key, expectedType);
    }
}
