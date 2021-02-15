package org.assertj.neo4j.api.beta;

import org.assertj.core.api.AbstractAssert;
import org.assertj.neo4j.api.beta.type.Nodes;

/**
 * @author patouche - 15/02/2021
 */
public class NodeAssert extends AbstractAssert<NodeAssert, Nodes.DbNode> {
    public NodeAssert(Nodes.DbNode node) {
        super(node, NodeAssert.class);
    }
}
