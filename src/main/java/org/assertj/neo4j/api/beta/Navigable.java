package org.assertj.neo4j.api.beta;

/**
 * @author patouche - 01/01/2021
 */
public interface Navigable<PARENT_ASSERT, ROOT_ASSERT> {

    PARENT_ASSERT toParentAssert();

    ROOT_ASSERT toRootAssert();
}
