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

import org.assertj.neo4j.api.beta.error.ElementsShouldHaveType;
import org.assertj.neo4j.api.beta.type.DataLoader;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.type.Relationships;
import org.assertj.neo4j.api.beta.util.RelationshipTypes;

import java.util.List;

/**
 * @author patouche - 24/11/2020
 */
//@formatter:off
public abstract class AbstractRelationshipsAssert<SELF extends AbstractRelationshipsAssert<SELF,PARENT_ASSERT, ROOT_ASSERT>,
                                                  PARENT_ASSERT,
                                                  ROOT_ASSERT>
        extends AbstractEntitiesAssert<SELF, Relationships.DbRelationship, PARENT_ASSERT, ROOT_ASSERT>
        implements Navigable<PARENT_ASSERT, ROOT_ASSERT> {
//@formatter:on

    protected AbstractRelationshipsAssert(
            final Class<?> selfType,
            final List<Relationships.DbRelationship> dbRelationships,
            final DataLoader<Relationships.DbRelationship> dbData,
            final boolean ignoringIds,
            final EntitiesAssertFactory<SELF, Relationships.DbRelationship, PARENT_ASSERT, ROOT_ASSERT> factory,
            final PARENT_ASSERT parentAssert,
            final ROOT_ASSERT rootAssert) {
        super(RecordType.RELATIONSHIP, selfType, dbData, dbRelationships, ignoringIds, factory, parentAssert, rootAssert);
    }

    /**
     * Is it really useful ?
     *
     * @param expectedType
     * @return
     */
    public SELF haveType(final String expectedType) {
        if (!RelationshipTypes.are(expectedType, actual)) {
            throwAssertionError(ElementsShouldHaveType.create(actual, expectedType));
        }
        return myself;
    }



}
