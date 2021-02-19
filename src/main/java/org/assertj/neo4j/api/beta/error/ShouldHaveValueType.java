package org.assertj.neo4j.api.beta.error;

import org.assertj.neo4j.api.beta.type.DbValue;
import org.assertj.neo4j.api.beta.type.ValueType;

import java.util.List;

/**
 * @author patouche - 18/02/2021
 */
public class ShouldHaveValueType extends BasicDbErrorMessageFactory<DbValue> {

    public ShouldHaveValueType(DbValue actual, ValueType type) {
        super("TODO", actual, ArgDetail.excluded(type));
    }

    private static ShouldHaveValueType create(DbValue actual, ValueType type) {
        return new ShouldHaveValueType(actual, type);
    }

    public static GroupingDbErrorFactory<DbValue> elements(List<DbValue> actual, ValueType type) {
        return new BasicDbGroupingErrorFactory<>(
                actual,
                (a) -> create(a, type),
                "TODO"
        );
    }
}
