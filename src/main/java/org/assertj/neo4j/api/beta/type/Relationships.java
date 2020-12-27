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
package org.assertj.neo4j.api.beta.type;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionConfig;
import org.neo4j.driver.Value;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author patouche - 31/10/2020
 */
public class Relationships extends AbstractDbData<Relationships.DbRelationship> {

    /** The relationship type. */
    private final String type;

    /**
     * Create a new relationships for assertions.
     *
     * @param driver the neo4j driver
     * @param type   the relationships type.
     */
    public Relationships(final Driver driver, final String type) {
        super(driver, RecordType.RELATIONSHIP);
        this.type = type;
    }

    /** {@inheritDoc} */
    @Override
    public List<Relationships.DbRelationship> load() {
        try (Session session = this.driver.session()) {
            final String query = String.format("MATCH ()-[r:%s]->() RETURN r", type);
            final Result result = session.run(query, TransactionConfig.builder().build());
            final List<Record> records = result.list();
            return records.stream()
                    .map(Record::values)
                    .flatMap(Collection::stream)
                    .map(Value::asRelationship)
                    .map(r -> new Relationships.DbRelationship(r.id(), r.type(), ValueType.convertMap(r.asMap())))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Create a new {@link DbRelationshipBuilder}.
     *
     * @return a new {@link DbRelationshipBuilder} with the same {@link #type} of the current relationships.
     */
    public DbRelationshipBuilder create() {
        return new DbRelationshipBuilder().type(type);
    }

    public static class DbRelationship extends DbEntity<DbRelationship> {

        private final String type;

        DbRelationship(final String type, final Map<String, DbValue> properties) {
            this(null, type, properties);
        }

        DbRelationship(final Long id, final String type, final Map<String, DbValue> properties) {
            super(RecordType.RELATIONSHIP, id, properties);
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return entityRepresentation("type='" + type + "'");
        }

        @Override
        public DbRelationship withoutId() {
            return new DbRelationship(this.type, this.properties);
        }

    }

    public static class DbRelationshipBuilder {

        private Long id;

        private String type;

        private final Map<String, DbValue> properties = new HashMap<>();

        DbRelationshipBuilder() {
        }

        public DbRelationshipBuilder id(final int id) {
            return this.id((long) id);
        }

        public DbRelationshipBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public DbRelationshipBuilder property(final String key, final Object value) {
            this.properties.put(key, ValueType.convert(value));
            return this;
        }

        public DbRelationshipBuilder type(final String type) {
            this.type = type;
            return this;
        }

        public DbRelationship build() {
            return new DbRelationship(id, type, properties);
        }

    }
}
