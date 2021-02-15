package org.assertj.neo4j.api.beta.util;

import org.assertj.neo4j.api.beta.type.Nodes;

import java.util.Objects;
import java.util.Set;

/**
 * @author patouche - 14/02/2021
 */
public class NodeComparisonStrategy extends EntityComparisonStrategy<Nodes.DbNode> {

    public NodeComparisonStrategy(final boolean ignoreId, final Set<String> ignoreProperties) {
        super(Nodes.DbNode.class, ignoreId, ignoreProperties);
    }

    @Override
    protected boolean areEqual(final Nodes.DbNode actual, final Nodes.DbNode other) {
        return this.areIdsEqual(actual, other)
               && this.arePropertiesEqual(actual, other)
               && this.areLabelsEqual(actual, other);
    }

    private boolean areLabelsEqual(Nodes.DbNode actual, Nodes.DbNode other) {
        return Objects.deepEquals(actual.getLabels(), other.getLabels());
    }

    public static NodeComparisonStrategyBuilder builder() {
        return new NodeComparisonStrategyBuilder();
    }

    //@formatter:off
    public static class NodeComparisonStrategyBuilder
            extends EntityComparisonStrategyBuilder<Nodes.DbNode,
                                                    NodeComparisonStrategy,
                                                    NodeComparisonStrategyBuilder> {
    //@formatter:on

        private NodeComparisonStrategyBuilder() {
            super(NodeComparisonStrategyBuilder.class);
        }

        public NodeComparisonStrategy build() {
            return new NodeComparisonStrategy(this.ignoreId, this.ignoredProperties);
        }

    }

}
