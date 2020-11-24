package org.assertj.neo4j.api.beta;

import org.assertj.neo4j.api.beta.type.Relationships;
import org.assertj.neo4j.api.beta.util.Wip;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author patouche - 24/11/2020
 */
public class DriverRelationshipsAssert
        extends AbstractEntitiesAssert<DriverRelationshipsAssert, Relationships.DbRelationship> {

    /**
     * Create new assertions on {@link Relationships}.
     *
     * @param relationships the relationships to assert
     */
    public DriverRelationshipsAssert(final Relationships relationships) {
        this(relationships.load(), null);
    }

    protected DriverRelationshipsAssert(final List<Relationships.DbRelationship> nodes,
            final DriverRelationshipsAssert parent) {
        super(nodes, DriverRelationshipsAssert.class, parent);
    }

    /** {@inheritDoc} */
    public DriverRelationshipsAssert ignoringIds() {
        final List<Relationships.DbRelationship> entities = actual.stream()
                .map(Relationships.DbRelationship::withoutId)
                .collect(Collectors.toList());
        return new DriverRelationshipsAssert(entities, this);
    }

    public DriverRelationshipsAssert haveType(final String expectedType) {
        throw Wip.TODO(this);
    }
}
