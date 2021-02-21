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
package org.assertj.neo4j.api.beta.type.loader;

import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.assertj.neo4j.api.beta.type.ObjectType;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Query;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Concrete implementation for loading {@link DbRelationship} entities.
 *
 * @author Patrick Allain - 31/10/2020
 */
class RelationshipLoader extends AbstractDataLoader<DbRelationship> implements Relationships {

    /**
     * Create a new relationships for assertions.
     *
     * @param driver the neo4j driver
     * @param type   the relationships type.
     */
    public RelationshipLoader(final Driver driver, final String type) {
        this(driver, Queries.relationships(type));
    }

    RelationshipLoader(final Driver driver, final Query query) {
        super(driver, ObjectType.RELATIONSHIP, query);
    }

    /** {@inheritDoc} */
    @Override
    public List<DbRelationship> load(final List<Record> records) {
        return records.stream()
                .map(Record::values)
                .flatMap(Collection::stream)
                .map(Value::asRelationship)
                .map(ObjectType.RELATIONSHIP::convert)
                .map(DbRelationship.class::cast)
                .collect(Collectors.toList());
    }

}
