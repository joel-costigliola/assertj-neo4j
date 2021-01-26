package org.assertj.neo4j.api.beta;

import org.assertj.neo4j.api.beta.type.AbstractDbData;
import org.assertj.neo4j.api.beta.type.Relationships;

import java.util.List;

/**
 * @author patouche - 02/01/2021
 */
//@formatter:off
public class ChildrenDriverRelationshipsAssert<DB_DATA extends AbstractDbData<Relationships.DbRelationship>,
                                               ROOT_ASSERT>
        extends AbstractRelationshipsAssert<ChildrenDriverRelationshipsAssert<DB_DATA, ROOT_ASSERT>, DB_DATA, ROOT_ASSERT>{
//@formatter:on

    protected ChildrenDriverRelationshipsAssert(final List<Relationships.DbRelationship> entities,
                                                DB_DATA dbData,
                                                ROOT_ASSERT rootAssert) {
        this(entities, dbData, null, rootAssert);
    }

    private ChildrenDriverRelationshipsAssert(final List<Relationships.DbRelationship> entities,
                                              final DB_DATA dbData,
                                              final ChildrenDriverRelationshipsAssert<DB_DATA, ROOT_ASSERT> parentAssert,
                                              final ROOT_ASSERT rootAssert) {
        super(ChildrenDriverRelationshipsAssert.class, entities, dbData, factory(), parentAssert, rootAssert);
    }

    private static <DB_DATA extends AbstractDbData<Relationships.DbRelationship>, ROOT_ASSERT>
    EntitiesAssertFactory<ChildrenDriverRelationshipsAssert<DB_DATA, ROOT_ASSERT>,
            Relationships.DbRelationship,
            ROOT_ASSERT>
    factory() {
        return (entities, current) -> new ChildrenDriverRelationshipsAssert<>(entities, current.dbData, current,
                current.rootAssert);
    }
}
