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

import org.assertj.neo4j.api.beta.type.DataLoader;
import org.assertj.neo4j.api.beta.type.Relationships;

import java.util.List;
import java.util.Objects;

/**
 * @author Patrick Allain - 02/01/2021
 */
//@formatter:off
public class ChildrenDriverRelationshipsAssert<PARENT_ASSERT, ROOT_ASSERT>
        extends AbstractRelationshipsAssert<ChildrenDriverRelationshipsAssert< PARENT_ASSERT, ROOT_ASSERT>,
                                            PARENT_ASSERT,
                                            ROOT_ASSERT>
        implements Adoptable<ChildrenDriverRelationshipsAssert<PARENT_ASSERT, ROOT_ASSERT>, PARENT_ASSERT> {
//@formatter:on

    protected ChildrenDriverRelationshipsAssert(final List<Relationships.DbRelationship> entities,
                                                final DataLoader<Relationships.DbRelationship> loader,
                                                final boolean ignoringIds,
                                                final ROOT_ASSERT rootAssert) {
        this(entities, loader, ignoringIds, null, rootAssert);
    }

    protected ChildrenDriverRelationshipsAssert(final List<Relationships.DbRelationship> entities,
                                                final DataLoader<Relationships.DbRelationship> loader,
                                                final boolean ignoringIds,
                                                final PARENT_ASSERT parentAssert,
                                                final ROOT_ASSERT rootAssert) {
        super(
                ChildrenDriverRelationshipsAssert.class,
                entities,
                loader,
                ignoringIds,
                factory(),
                parentAssert,
                Objects.requireNonNull(rootAssert)
        );
    }

    private static <PARENT_ASSERT, ROOT_ASSERT> EntitiesAssertFactory<ChildrenDriverRelationshipsAssert<PARENT_ASSERT
            , ROOT_ASSERT>,
            Relationships.DbRelationship, PARENT_ASSERT, ROOT_ASSERT> factory() {
        return (entities, loader, ignoringIds, self) -> new ChildrenDriverRelationshipsAssert<>(
                entities,
                loader,
                ignoringIds,
                self.toRootAssert()
        );
    }

    /** {@inheritDoc} */
    @Override
    public ChildrenDriverRelationshipsAssert<PARENT_ASSERT, ROOT_ASSERT> withParent(PARENT_ASSERT parentAssert) {
        return new ChildrenDriverRelationshipsAssert<>(actual, dataLoader, ignoreIds, parentAssert, toRootAssert());
    }

    /** {@inheritDoc} */
    @Override
    public ROOT_ASSERT toRootAssert() {
        return rootAssert().orElseThrow(() -> new IllegalArgumentException("Root assertion shouldn't be null !"));
    }
}
