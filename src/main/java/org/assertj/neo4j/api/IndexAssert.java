package org.assertj.neo4j.api;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.index.Index;

import static org.assertj.neo4j.error.ShouldBeWriteable.shouldBeWriteable;

public class IndexAssert <T extends PropertyContainer> extends AbstractAssert<IndexAssert<T>, Index<T>> {

    public IndexAssert(Index<T> index) {
        super(index, IndexAssert.class);
    }

    public Index<T> getActual() {
        return actual;
    }

    public IndexAssert<T> isWriteable() {
        Objects.instance().assertNotNull(info, actual);

        if (!actual.isWriteable()) {
            throw Failures.instance().failure(info, shouldBeWriteable(actual));
        }
        return this;
    }
}
