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

import org.assertj.neo4j.api.beta.error.ShouldRelationshipHaveType;
import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.assertj.neo4j.api.beta.type.ObjectType;
import org.assertj.neo4j.api.beta.type.loader.DataLoader;
import org.assertj.neo4j.api.beta.type.loader.LoaderFactory;
import org.assertj.neo4j.api.beta.type.loader.Nodes;
import org.assertj.neo4j.api.beta.util.DbObjectUtils;
import org.assertj.neo4j.api.beta.util.Predicates;

import java.util.Arrays;
import java.util.List;

/**
 * Abstract assertion on {@link DbRelationship}.
 *
 * @param <SELF>          the assertion self type
 * @param <NEW_SELF>      the new self assertion type
 * @param <ROOT_ASSERT>   the root assert type
 * @param <PARENT_ASSERT> the parent assertion type
 * @author Patrick Allain - 24/11/2020
 */
//@formatter:off
public abstract class AbstractRelationshipsAssert<SELF extends AbstractRelationshipsAssert<SELF,
                                                                                           NEW_SELF,
                                                                                           PARENT_ASSERT,
                                                                                           ROOT_ASSERT>,
                                                  NEW_SELF extends Navigable<SELF, ROOT_ASSERT>,
                                                  PARENT_ASSERT extends ParentAssert<ROOT_ASSERT>,
                                                  ROOT_ASSERT>
        extends AbstractEntitiesAssert<SELF, DbRelationship, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT> {
//@formatter:on

    protected AbstractRelationshipsAssert(
            final Class<?> selfType,
            final List<DbRelationship> entities,
            final DataLoader<DbRelationship> loader,
            final EntitiesAssertFactory<SELF, DbRelationship, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT> newSelfFactory,
            final PARENT_ASSERT parentAssert,
            final ROOT_ASSERT rootAssert) {
        super(ObjectType.RELATIONSHIP, entities, selfType, loader, newSelfFactory, parentAssert, rootAssert);
    }

    /**
     * Is it really useful ?
     *
     * @param expectedType the expected type
     * @return {@code this} assertion object
     */
    public SELF haveType(final String expectedType) {
        return shouldAllVerify(
                Predicates.relationshipIsOfType(expectedType),
                (notSatisfies -> ShouldRelationshipHaveType.elements(actual, expectedType).notSatisfies(notSatisfies))
        );
    }

    /**
     * Is it really useful ?
     *
     * @param types the expected types
     * @return {@code this} assertion object
     */
    public SELF haveAnyOfTypes(final String... types) {
        return shouldAllVerify(
                Predicates.isAnyOfTypes(types),
                (notSatisfies -> ShouldRelationshipHaveType
                        .elements(actual, Arrays.asList(types))
                        .notSatisfies(notSatisfies))
        );
    }

    /**
     * Retrieve all nodes that are the start node of any actual relationships.
     * <p/>
     * Example:
     * <pre><code class='java'>
     * Relationships relationships = new Relationships(driver, "KNOWS");
     * assertThat(relationships)
     *   .startingNodes("Person")
     *   .haveLabels("TeamMember")
     * </code></pre>
     *
     * @param labels
     * @return
     */
    public ChildrenDriverNodeAssert<SELF, ROOT_ASSERT> startingNodes(final String... labels) {
        final Nodes nodes = dataLoader.chain(LoaderFactory.nodeByIds(DbObjectUtils.arrayStartNodeIds(actual)));
        return new ChildrenDriverNodeAssert<>(nodes.load(), nodes, myself)
                .filteredOnLabels(labels)
                .withParent(myself);
    }

    /**
     * Retrieve all nodes that are the end node of any actual relationships.
     * <p/>
     * Example:
     * <pre><code class='java'>
     * Relationships relationships = new Relationships(driver, "KNOWS");
     * assertThat(relationships)
     *   .endingNodes("Person")
     *   .haveLabels("TeamMember")
     * </code></pre>
     *
     * @param labels
     * @return
     */
    public ChildrenDriverNodeAssert<SELF, ROOT_ASSERT> endingNodes(final String... labels) {
        final Nodes nodes = dataLoader.chain(LoaderFactory.nodeByIds(DbObjectUtils.arrayEndNodeIds(actual)));
        return new ChildrenDriverNodeAssert<>(nodes.load(), nodes, myself)
                .filteredOnLabels(labels)
                .withParent(myself);
    }

    public SELF haveNoStartingNodes(final String... labels) {
        startingNodes(labels).isEmpty();
        return myself;
    }

    public SELF haveNoEndingNodes(final String... labels) {
        endingNodes(labels).isEmpty();
        return myself;
    }

}
