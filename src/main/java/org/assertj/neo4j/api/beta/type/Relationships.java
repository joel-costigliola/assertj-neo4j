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

import java.util.Map;

/**
 * {@link Relationships.DbRelationship} entities loader definition.
 *
 * @author Patrick Allain - 31/10/2020
 */
public interface Relationships extends DataLoader<Relationships.DbRelationship> {

    static Relationships of(final Driver driver, final String type) {
        return new RelationshipLoader(driver, type);
    }

    /**
     * TODO : Maybe extract method here in a interface to be able to decorate a Relationship from driver - This may have
     * impact for comparing relationship.
     */
    class DbRelationship extends DbEntity {

        private final Long start;
        private final Long end;
        private final String type;

        DbRelationship(final Long id, final String type, final Long start, final Long end,
                       final Map<String, DbValue> properties) {
            super(RecordType.RELATIONSHIP, id, properties);
            this.start = start;
            this.end = end;
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public Long getStart() {
            return start;
        }

        public Long getEnd() {
            return end;
        }

        @Override
        public String toString() {
            return entityRepresentation("type='" + type + "', start=" + start + ", end=" + end);
        }

    }

    class DbRelationshipBuilder extends DbEntity.DbEntityBuilder<DbRelationship, DbRelationshipBuilder> {

        private String type;
        private Long start;
        private Long end;

        protected DbRelationshipBuilder() {
            super(DbRelationshipBuilder.class);
        }

        public DbRelationshipBuilder type(final String type) {
            this.type = type;
            return this;
        }

        public DbRelationshipBuilder start(final int start) {
            return start((long) start);
        }

        public DbRelationshipBuilder start(final Long start) {
            this.start = start;
            return this;
        }

        public DbRelationshipBuilder end(final int end) {
            return end((long) end);
        }

        public DbRelationshipBuilder end(final Long end) {
            this.end = end;
            return this;
        }

        public DbRelationship build() {
            return new DbRelationship(id, type, start, end, properties);
        }

    }
}
