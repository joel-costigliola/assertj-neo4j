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
package org.assertj.neo4j.api.beta.util;

import org.assertj.neo4j.api.beta.type.Nodes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Patrick Allain - 14/02/2021
 */
public class NodeComparisonStrategy extends EntityComparisonStrategy<Nodes.DbNode> {

    private final Set<String> ignoreLabels;

    private NodeComparisonStrategy(final boolean ignoreId, final Set<String> ignoreLabels,
                                   final Set<String> ignoreProperties) {
        super(Nodes.DbNode.class, ignoreId, ignoreProperties);
        this.ignoreLabels = ignoreLabels;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean areEntityEqual(final Nodes.DbNode actual, final Nodes.DbNode other) {
        return Utils.lazyDeepEquals(this::nonIgnoredLabels).test(actual, other);
    }

    private Set<String> nonIgnoredLabels(final Nodes.DbNode entity) {
        final HashSet<String> labels = new HashSet<>(entity.getLabels());
        ignoreLabels.forEach(labels::remove);
        return labels;
    }

    /**
     * Create a new builder for a {@link NodeComparisonStrategy}.
     *
     * @return the new builder instance.
     */
    public static NodeComparisonStrategyBuilder builder() {
        return new NodeComparisonStrategyBuilder();
    }

    //@formatter:off
    public static class NodeComparisonStrategyBuilder
            extends EntityComparisonStrategyBuilder<Nodes.DbNode,
                                                    NodeComparisonStrategy,
                                                    NodeComparisonStrategyBuilder> {
    //@formatter:on

        private final Set<String> ignoreLabels = new HashSet<>();

        private NodeComparisonStrategyBuilder() {
            super(NodeComparisonStrategyBuilder.class);
        }

        public NodeComparisonStrategyBuilder ignoreLabels(String... labels) {
            this.ignoreLabels.addAll(Arrays.asList(labels));
            return myself;
        }

        public NodeComparisonStrategy build() {
            return new NodeComparisonStrategy(this.ignoreId, this.ignoreLabels, this.ignoreProperties);
        }

    }

}
