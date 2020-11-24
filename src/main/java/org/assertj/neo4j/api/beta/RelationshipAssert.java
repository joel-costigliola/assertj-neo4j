package org.assertj.neo4j.api.beta;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AssertFactory;
import org.assertj.neo4j.api.beta.type.Relationships;

/**
 * @author pallain - 24/11/2020
 */
public class RelationshipAssert extends AbstractAssert<RelationshipAssert, Relationships.DbRelationship> {

    public static class Factory implements AssertFactory<Relationships.DbRelationship, RelationshipAssert> {

        @Override
        public RelationshipAssert createAssert(final Relationships.DbRelationship record) {
            return null;
        }
    }

    public RelationshipAssert(final Relationships.DbRelationship node) {
        super(node, NodeAssert.class);
    }

}
