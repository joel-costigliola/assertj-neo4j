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

import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.assertj.neo4j.api.beta.type.loader.DataLoader;

import java.util.List;
import java.util.Objects;

/**
 * @author Patrick Allain - 02/01/2021
 */
//@formatter:off
public class ChildrenDriverRelationshipsAssert<PARENT_ASSERT extends ParentalAssert, ROOT_ASSERT>
        extends AbstractRelationshipsAssert<ChildrenDriverRelationshipsAssert<PARENT_ASSERT, ROOT_ASSERT>,
                                            ChildrenDriverRelationshipsAssert<ChildrenDriverRelationshipsAssert<PARENT_ASSERT, ROOT_ASSERT>, ROOT_ASSERT>,
                                            PARENT_ASSERT,
                                            ROOT_ASSERT>
        implements Adoptable<ROOT_ASSERT> {
//@formatter:on

    protected ChildrenDriverRelationshipsAssert(final List<DbRelationship> entities,
                                                final DataLoader<DbRelationship> loader,
                                                final PARENT_ASSERT parentAssert,
                                                final ROOT_ASSERT rootAssert) {
        super(
                ChildrenDriverRelationshipsAssert.class,
                entities,
                loader,
                factory(),
                parentAssert,
                Objects.requireNonNull(rootAssert)
        );
    }

    //@formatter:off
    private static <PARENT_ASSERT extends ParentalAssert, ROOT_ASSERT> EntitiesAssertFactory<
            ChildrenDriverRelationshipsAssert<PARENT_ASSERT, ROOT_ASSERT>,
            DbRelationship,
            ChildrenDriverRelationshipsAssert<ChildrenDriverRelationshipsAssert<PARENT_ASSERT, ROOT_ASSERT>, ROOT_ASSERT>,
            PARENT_ASSERT,
            ROOT_ASSERT> factory() {
    //@formatter:on
        return (entities, loader, s) -> new ChildrenDriverRelationshipsAssert<>(entities, loader, s, s.toRootAssert());
    }

    /** {@inheritDoc} */
    @Override
    public <NEW_PARENT extends ParentalAssert> ChildrenDriverRelationshipsAssert<NEW_PARENT, ROOT_ASSERT> withParent(final NEW_PARENT parentAssert) {
        return new ChildrenDriverRelationshipsAssert<>(actual, dataLoader, parentAssert, toRootAssert());
    }

    /** {@inheritDoc} */
    @Override
    public ROOT_ASSERT toRootAssert() {
        return rootAssert().orElseThrow(() -> new IllegalArgumentException("Root assertion shouldn't be null !"));
    }

}
