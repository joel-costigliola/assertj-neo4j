/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2013-2020 the original author or authors.
 */
package org.assertj.neo4j.api.beta;

import org.assertj.neo4j.api.beta.error.ShouldHaveNodeLabels;
import org.assertj.neo4j.api.beta.error.ShouldNodeHaveNoRelatedRelationships;
import org.assertj.neo4j.api.beta.type.DataLoader;
import org.assertj.neo4j.api.beta.type.LoaderFactory;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.type.Relationships;
import org.assertj.neo4j.api.beta.util.Checks;
import org.assertj.neo4j.api.beta.util.EntityUtils;
import org.assertj.neo4j.api.beta.util.Predicates;

import java.util.List;

/**
 * @author Patrick Allain - 08/11/2020
 */
//@formatter:off
public abstract class AbstractNodesAssert<SELF extends AbstractNodesAssert<SELF, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT>,
                                          NEW_SELF extends Navigable<SELF, ROOT_ASSERT>,
                                          PARENT_ASSERT extends ParentAssert,
                                          ROOT_ASSERT>
        extends AbstractEntitiesAssert<SELF, Nodes.DbNode, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT> {
//@formatter:on

    protected AbstractNodesAssert(
            final Class<SELF> selfType,
            final List<Nodes.DbNode> entities,
            final DataLoader<Nodes.DbNode> dataLoader,
            final boolean ignoreIds,
            final EntitiesAssertFactory<SELF, Nodes.DbNode, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT> factory,
            final PARENT_ASSERT parentAssert,
            final ROOT_ASSERT rootAssert) {
        super(RecordType.NODE, selfType, dataLoader, entities, ignoreIds, factory, parentAssert, rootAssert);
    }

    /**
     * Verifies that all nodes have the expected label names.
     * <p/>
     * Example:
     * <pre><code class='java'>
     * Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes).haveLabels("Committer", "Developer")
     * </code></pre>
     * <p/>
     * If the <code>expectedLabels</code> is {@code null} or empty, an {@link IllegalArgumentException} is thrown.
     * <p/>
     *
     * @param expectedLabels the labels name to search on nodes
     * @return this {@link AbstractNodesAssert} for assertions chaining
     * @throws IllegalArgumentException if <code>expectedLabels</code> is {@code null} or empty.
     * @throws AssertionError           if the one of the actual nodes does not contain the given label
     */
    public SELF haveLabels(final String... expectedLabels) {
        return haveLabels(Checks.notNullOrEmpty(expectedLabels, "The labels to look for should not be null or empty"));
    }

    /**
     * Verifies that all nodes have the expected label names.
     * <p/>
     * Example:
     * <pre><code class='java'>
     * Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes).haveLabels("Committer", "Developer")
     * </code></pre>
     * <p/>
     * If the <code>expectedLabels</code> is {@code null} or empty, an {@link IllegalArgumentException} is thrown.
     * <p/>
     *
     * @param expectedLabels the labels name to search on nodes
     * @return this {@link AbstractNodesAssert} for assertions chaining
     * @throws IllegalArgumentException if <code>expectedLabels</code> is {@code null} or empty.
     * @throws AssertionError           if the one of the actual nodes does not contain the given label
     */
    public SELF haveLabels(final Iterable<String> expectedLabels) {
        final List<String> labels = Checks
                .notNullOrEmpty(expectedLabels, "The iterable of values to look for should not be empty");
        return shouldAllVerify(
                Predicates.labelsExists(expectedLabels),
                (notSatisfies) -> ShouldHaveNodeLabels.elements(actual, labels).notSatisfies(notSatisfies)
        );
    }

    /**
     * Create a new assertions on incoming relationships.
     *
     * @param types the relation types
     * @return a new assertions on incoming relationships
     */
    public ChildrenDriverRelationshipsAssert<SELF, ROOT_ASSERT> incomingRelationships(final String... types) {
        final List<Long> nodeIds = entityIds();
        final Relationships relationships = this.dataLoader.chain(LoaderFactory.relationships(types));
        return new ChildrenDriverRelationshipsAssert<>(relationships.load(), relationships, false, myself,
                toRootAssert())
                .filteredOn(r -> nodeIds.contains(r.getEnd()))
                .withParent(myself);
    }

    /**
     * Create a new assertions on outgoing relationships.
     *
     * @param types the type of relationship.
     * @return a new assertions on incoming relationships
     */
    public ChildrenDriverRelationshipsAssert<SELF, ROOT_ASSERT> outgoingRelationships(final String... types) {
        final List<Long> nodeIds = entityIds();
        final Relationships relationships = this.dataLoader.chain(LoaderFactory.relationships(types));
        return new ChildrenDriverRelationshipsAssert<>(relationships.load(), relationships, false, myself,
                toRootAssert())
                .filteredOn(r -> nodeIds.contains(r.getStart()))
                .withParent(myself);
    }

    /**
     * Verify that there is no incoming relationships on all actual nodes.
     *
     * @param types the type of relationship
     * @return {@code this} assertion object.
     */
    public SELF haveNoIncomingRelationships(String... types) {
        return incomingRelationships(types)
                .isEmpty((relationships) -> ShouldNodeHaveNoRelatedRelationships
                        .incomingElements(actual, relationships)
                        .notSatisfies(EntityUtils.havingIncomingRelationships(actual, relationships))
                )
                .toParentAssert();
    }

    /**
     * Verify that there is no outgoing relationships on all actual nodes.
     *
     * @param types the type of relationship
     * @return {@code this} assertion object.
     */
    public SELF haveNoOutgoingRelationships(String... types) {
        return outgoingRelationships(types)
                .isEmpty((relationships) -> ShouldNodeHaveNoRelatedRelationships
                        .outgoingElements(actual, relationships)
                        .notSatisfies(EntityUtils.havingOutgoingRelationships(actual, relationships))
                )
                .toParentAssert();
    }

}
