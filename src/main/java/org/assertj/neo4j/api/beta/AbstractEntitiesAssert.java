package org.assertj.neo4j.api.beta;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Iterables;
import org.assertj.core.util.Arrays;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.util.Wip;

import java.util.List;

/**
 * @author pallain - 24/11/2020
 */
//@formatter:off
public abstract class AbstractEntitiesAssert<SELF extends AbstractEntitiesAssert<SELF, ENTITY>,
                                             ENTITY extends DbEntity<ENTITY>>
        extends AbstractAssert<SELF, List<ENTITY>> {
//@formatter:on

    protected Iterables iterables = Iterables.instance();

    /** Parent nodes asserts. May be {@code null}. */
    private final SELF parent;

    protected AbstractEntitiesAssert(final List<ENTITY> entities, final Class<?> selfType, SELF parent) {
        super(entities, selfType);
        this.parent = parent;
    }

    public SELF hasSize(final int expectedSize) {
        iterables.assertHasSize(info, actual, expectedSize);
        return myself;
    }

    public SELF contains(ENTITY... values) {
        iterables.assertContains(info, actual, values);
        return myself;
    }

    /**
     * Provide a easy way to create new assertions on the current list of
     * {@link org.assertj.neo4j.api.beta.type.Nodes.DbNode} without specifying the {@link Nodes.DbNode#getId()}
     *
     * @return a new instance of {@link DriverNodesAssert}
     */
    public abstract SELF ignoringIds();

    protected static <T> List<T> checkArray(final T[] items) {
        if (Arrays.isNullOrEmpty(items)) {
            throw new IllegalArgumentException("The array of values to look for should not be empty");
        }
        return java.util.Arrays.asList(items);
    }

    public SELF havePropertyKeys(final String... keys) {
        throw Wip.TODO(this);
    }

    public SELF havePropertyKeys(final Iterable<String> keys) {
        throw Wip.TODO(this);
        // return myself;
    }

    public SELF haveProperties(final String... keys) {
        return myself;
    }

    public SELF haveProperties(final Iterable<String> keys) {
        throw Wip.TODO(this);
        // return myself;
    }

}
