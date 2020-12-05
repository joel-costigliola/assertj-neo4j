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
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.util.NodeLabels;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author patouche - 08/11/2020
 */
public class DriverNodesAssert extends AbstractEntitiesAssert<DriverNodesAssert, Nodes, Nodes.DbNode> {

    /**
     * Create new assertions on {@link Nodes}.
     *
     * @param nodes the nodes to assert
     */
    public DriverNodesAssert(final Nodes nodes) {
        this(nodes, null);
    }

    protected DriverNodesAssert(final Nodes nodes, final DriverNodesAssert parent) {
        this(nodes.load(), nodes, parent);
    }

    protected DriverNodesAssert(List<Nodes.DbNode> dbNodes, final Nodes nodes, final DriverNodesAssert parent) {
        super(RecordType.NODE, nodes, dbNodes, DriverNodesAssert.class, parent);
    }

    /** {@inheritDoc} */
    @Override
    public DriverNodesAssert ignoringIds() {
        final List<Nodes.DbNode> nodes = actual.stream().map(Nodes.DbNode::withoutId).collect(Collectors.toList());
        return new DriverNodesAssert(nodes, this.loadingType, this);
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
     * @return this {@link DriverNodesAssert} for assertions chaining
     * @throws IllegalArgumentException if <code>expectedLabels</code> is {@code null} or empty.
     * @throws AssertionError           if the one of the actual nodes does not contain the given label
     */
    public DriverNodesAssert haveLabels(final String... expectedLabels) {
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
     * @return this {@link DriverNodesAssert} for assertions chaining
     * @throws IllegalArgumentException if <code>expectedLabels</code> is {@code null} or empty.
     * @throws AssertionError           if the one of the actual nodes does not contain the given label
     */
    public DriverNodesAssert haveLabels(final Iterable<String> expectedLabels) {
        if (IterableUtil.isNullOrEmpty(expectedLabels)) {
            throw new IllegalArgumentException("The iterable of values to look for should not be empty");
        }
        if (NodeLabels.haveLabels(actual, expectedLabels)) {
            final ArrayList<String> labels = Lists.newArrayList(expectedLabels);
            throwAssertionError(ElementsShouldHaveLabels.create(actual, labels, NodeLabels.missing(actual, labels)));
        }
        return myself;
    }

}
