package org.assertj.neo4j.api.beta;

import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.IndexedObjectEnumerableAssert;
import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ObjectAssert;

import java.util.List;

/**
 * @author patouche - 26/01/2021
 */
//@formatter:off
public class NavigableListAssert<ACTUAL, PARENT, ROOT>
    extends AbstractListAssert<NavigableListAssert<ACTUAL, PARENT, ROOT> , List<ACTUAL>, ACTUAL, ObjectAssert<ACTUAL>>
        implements Navigable<PARENT, ROOT> {
//@formatter:on
public NavigableListAssert(List<  ACTUAL> actual) {
    super(actual, NavigableListAssert.class);
}

    @Override
    public PARENT toParentAssert() {
        return null;
    }

    @Override
    public ROOT toRootAssert() {
        return null;
    }

    @Override
    protected ObjectAssert<ACTUAL> toAssert(ACTUAL value, String description) {
        return null;
    }

    @Override
    protected NavigableListAssert<ACTUAL, PARENT, ROOT> newAbstractIterableAssert(Iterable<? extends ACTUAL> iterable) {
        return null;
    }
}
