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

import java.util.List;
import java.util.Map;

/**
 * @author patouche - 31/10/2020
 */
public class Relationships extends LoadingType<Relationships.DbRelationship> {

    /** The relationship type. */
    private final String type;

    public Relationships(final Driver driver, final String type) {
        super(driver, RecordType.RELATIONSHIP);
        this.type = type;
    }

    public static Relationships.DbRelationshipBuilder relation() {
        return new DbRelationshipBuilder();
    }

    @Override
    public List<Relationships.DbRelationship> load() {
        return null;
    }

    public static class DbRelationship extends DbEntity<DbRelationship> {

        private final String type;

        public DbRelationship(final String type, final Map<String, Object> properties) {
            this(null, type, properties);
        }

        public DbRelationship(final Long id, final String type, final Map<String, Object> properties) {
            super(RecordType.RELATIONSHIP, id, properties);
            this.type = type;
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

        private Map<String, Object> properties;

        private DbRelationshipBuilder() {
        }

        public DbRelationshipBuilder id(final int id) {
            return this.id((long) id);
        }

        public DbRelationshipBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public DbRelationshipBuilder property(final String key, final Object value) {
            this.properties.put(key, value);
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
