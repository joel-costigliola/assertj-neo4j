package org.assertj.neo4j.api;

import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.index.Index;

public class IndexAssert <T extends PropertyContainer> {
    private final Index<T> actual;

    public IndexAssert(Index<T> index) {
        actual = index;
    }

    public Index<T> getActual() {
        return actual;
    }
}
