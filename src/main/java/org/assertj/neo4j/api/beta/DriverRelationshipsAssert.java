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
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.type.Relationships;
import org.assertj.neo4j.api.beta.util.RelationshipTypes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author patouche - 24/11/2020
 */
public class DriverRelationshipsAssert
        extends AbstractEntitiesAssert<DriverRelationshipsAssert, Relationships, Relationships.DbRelationship> {

    /**
     * Create new assertions on {@link Relationships}.
     *
     * @param relationships the relationships to assert
     */
    public DriverRelationshipsAssert(final Relationships relationships) {
        this(relationships, null);
    }

    protected DriverRelationshipsAssert(final Relationships relationships, final DriverRelationshipsAssert parent) {
        this(relationships.load(), relationships, parent);
    }

    public DriverRelationshipsAssert(
            final List<Relationships.DbRelationship> dbRelationships, final Relationships relationships,
            final DriverRelationshipsAssert parent) {
        super(RecordType.RELATIONSHIP, relationships, dbRelationships, DriverRelationshipsAssert.class, parent);
    }

    /** {@inheritDoc} */
    public DriverRelationshipsAssert ignoringIds() {
        final List<Relationships.DbRelationship> entities = actual.stream()
                .map(Relationships.DbRelationship::withoutId)
                .collect(Collectors.toList());
        return new DriverRelationshipsAssert(entities, this.loadingType, this);
    }

    /**
     * Is it really useful ?
     *
     * @param expectedType
     * @return
     */
    public DriverRelationshipsAssert haveType(final String expectedType) {
        if (!RelationshipTypes.are(expectedType, actual)) {
            throwAssertionError(ElementsShouldHaveType.create(actual, expectedType, RelationshipTypes.others(expectedType, actual)));
        }
        return myself;
    }
}
