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

import org.assertj.neo4j.api.beta.error.ShouldRelationshipHaveType;
import org.assertj.neo4j.api.beta.type.DataLoader;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.type.Relationships;
import org.assertj.neo4j.api.beta.util.Predicates;

import java.util.List;

/**
 * @author Patrick Allain - 24/11/2020
 */
//@formatter:off
public abstract class AbstractRelationshipsAssert<SELF extends AbstractRelationshipsAssert<SELF, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT>,
                                                  NEW_SELF extends Navigable<SELF, ROOT_ASSERT>,
                                                  PARENT_ASSERT extends ParentalAssert,
                                                  ROOT_ASSERT>
        extends AbstractEntitiesAssert<SELF, Relationships.DbRelationship, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT>
        implements Navigable<PARENT_ASSERT, ROOT_ASSERT> {
//@formatter:on

    protected AbstractRelationshipsAssert(
            final Class<?> selfType,
            final List<Relationships.DbRelationship> dbRelationships,
            final DataLoader<Relationships.DbRelationship> dbData,
            final EntitiesAssertFactory<SELF, Relationships.DbRelationship, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT> factory,
            final PARENT_ASSERT parentAssert,
            final ROOT_ASSERT rootAssert) {
        super(RecordType.RELATIONSHIP, selfType, dbData, dbRelationships, factory, parentAssert, rootAssert);
    }

    /**
     * Is it really useful ?
     *
     * @param expectedType
     * @return
     */
    public SELF haveType(final String expectedType) {
        return shouldAllVerify(
                Predicates.isType(expectedType),
                (notSatisfies -> ShouldRelationshipHaveType.elements(actual, expectedType).notSatisfies(notSatisfies))
        );
    }

}
