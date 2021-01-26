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

import org.assertj.core.util.IterableUtil;
import org.assertj.core.util.Lists;
import org.assertj.neo4j.api.beta.error.ElementsShouldHaveLabels;
import org.assertj.neo4j.api.beta.type.AbstractDbData;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.type.Relationships;
import org.assertj.neo4j.api.beta.util.NodeLabels;
import org.assertj.neo4j.api.beta.util.Wip;

import java.util.ArrayList;
import java.util.List;

/**
 * @author patouche - 08/11/2020
 */
//@formatter:off
public abstract class AbstractNodesAssert<SELF extends AbstractNodesAssert<SELF, DB_DATA,  PARENT>,
                                          DB_DATA extends AbstractDbData<Nodes.DbNode>,
                                          PARENT>
        extends AbstractEntitiesAssert<SELF, DB_DATA, Nodes.DbNode, PARENT> {
//@formatter:on

    protected AbstractNodesAssert(final Class<SELF> selfType,
                                  final List<Nodes.DbNode> entities,
                                  final DB_DATA dbData,
                                  final EntitiesAssertFactory<SELF, Nodes.DbNode, PARENT> factory,
                                  final SELF parentAssert,
                                  final PARENT rootAssert) {
        super(RecordType.NODE, selfType, dbData, entities, factory, parentAssert, rootAssert);
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
        return haveLabels(checkArray(expectedLabels, "The labels to look for should not be null or empty"));
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
        if (IterableUtil.isNullOrEmpty(expectedLabels)) {
            throw new IllegalArgumentException("The iterable of values to look for should not be empty");
        }
        if (NodeLabels.haveLabels(actual, expectedLabels)) {
            final ArrayList<String> labels = Lists.newArrayList(expectedLabels);
            throwAssertionError(ElementsShouldHaveLabels.create(actual, labels, NodeLabels.missing(actual, labels)));
        }
        return myself;
    }

    public ChildrenDriverRelationshipsAssert<AbstractDbData<Relationships.DbRelationship>, SELF> incomingRelationships(String type) {

        final Relationships relationships = new Relationships(this.dbData.getDriver(), type);
//        return new ChildrenDriverRelationshipsAssert<>(relationships.load(), dbData, this)
//                .filteredOn();


        throw Wip.TODO(this);
    }

    public ChildrenDriverRelationshipsAssert<AbstractDbData<Relationships.DbRelationship>, SELF> outgoingRelationships(String type) {
        throw Wip.TODO(this);
    }

    public SELF haveNoIncomingRelationships(String ... types) {
        throw Wip.TODO(this);
    }

    public SELF haveNoOutgoingRelationships(String ... types) {
        throw Wip.TODO(this);
    }
}
