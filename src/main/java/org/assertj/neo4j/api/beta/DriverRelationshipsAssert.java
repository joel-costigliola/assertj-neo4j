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

import org.assertj.core.util.VisibleForTesting;
import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.assertj.neo4j.api.beta.type.loader.DataLoader;
import org.assertj.neo4j.api.beta.type.loader.Relationships;

import java.util.List;

/**
 * @author Patrick Allain - 24/11/2020
 */
//@formatter:off
public class DriverRelationshipsAssert
        extends AbstractRelationshipsAssert<DriverRelationshipsAssert,
                                            DriverRelationshipsAssert,
                                            DriverRelationshipsAssert,
                                            DriverRelationshipsAssert> {
//@formatter:on

    /**
     * Create new assertions on {@link Relationships}.
     *
     * @param relationships the relationships to assert
     */
    public DriverRelationshipsAssert(final Relationships relationships) {
        this(relationships.load(), relationships, null);
    }

    /**
     * FIXME : To be removed.
     *
     * @param entities
     */
    @VisibleForTesting
    protected DriverRelationshipsAssert(final List<DbRelationship> entities) {
        this(entities, null, null);
    }

    private DriverRelationshipsAssert(final List<DbRelationship> entities,
                                      final DataLoader<DbRelationship> loader,
                                      final DriverRelationshipsAssert parent) {
        super(
                DriverRelationshipsAssert.class,
                entities,
                loader,
                DriverRelationshipsAssert::new,
                parent,
                Navigable.rootAssert(parent)
        );
    }

    /** {@inheritDoc} */
    @Override
    public DriverRelationshipsAssert toRootAssert() {
        return rootAssert().orElse(this);
    }
}
