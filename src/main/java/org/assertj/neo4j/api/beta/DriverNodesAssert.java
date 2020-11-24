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

import org.assertj.core.util.Arrays;
import org.assertj.core.util.IterableUtil;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Streams;
import org.assertj.neo4j.api.beta.error.ElementsShouldHaveLabels;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.util.Entities;
import org.assertj.neo4j.api.beta.util.Wip;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author patouche - 08/11/2020
 */
public class DriverNodesAssert extends AbstractEntitiesAssert<DriverNodesAssert, Nodes.DbNode> {

    /**
     * Create new assertions on {@link Nodes}.
     *
     * @param nodes the nodes to assert
     */
    public DriverNodesAssert(final Nodes nodes) {
        this(nodes.load(), null);
    }

    protected DriverNodesAssert(final List<Nodes.DbNode> nodes, final DriverNodesAssert parent) {
        super(nodes, DriverNodesAssert.class, parent);
    }

    /** {@inheritDoc} */
    public DriverNodesAssert ignoringIds() {
        final List<Nodes.DbNode> nodes = actual.stream().map(Nodes.DbNode::withoutId).collect(Collectors.toList());
        return new DriverNodesAssert(nodes, this);
    }

    public DriverNodesAssert haveLabels(final String... labels) {
        return haveLabels(checkArray(labels));
    }

    public DriverNodesAssert haveLabels(final Iterable<String> labels) {
        if (IterableUtil.isNullOrEmpty(labels)) {
            throw new IllegalArgumentException("The iterable of values to look for should not be empty");
        }
        final boolean hasMissing = Streams.stream(labels)
                .anyMatch(l -> actual.stream().anyMatch(n -> !n.getLabels().contains(l)));
        if (hasMissing) {
            final ArrayList<String> expectedLabels = Lists.newArrayList(labels);
            throwAssertionError(ElementsShouldHaveLabels.create(
                    expectedLabels, actual, Entities.havingMissingLabels(actual, expectedLabels)
            ));
        }
        return myself;
    }


}
