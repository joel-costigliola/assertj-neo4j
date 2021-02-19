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

import org.assertj.neo4j.api.beta.util.Formats;

import java.util.Comparator;
import java.util.Map;

/**
 * TODO : Maybe extract method here in a interface to be able to decorate a Relationship from driver - This may have
 * impact for comparing relationship.
 */
public class DbRelationship extends DbEntity<DbRelationship> {

    public static final Comparator<DbRelationship> RELATIONSHIP_COMPARATOR =
            Comparator.comparing(DbRelationship::getId, Comparator.nullsFirst(Comparator.naturalOrder()));

    private final Long start;
    private final Long end;
    private final String type;

    DbRelationship(final Long id, final String type, final Long start, final Long end,
                   final Map<String, DbValue> properties) {
        super(ObjectType.RELATIONSHIP, id, properties);
        this.start = start;
        this.end = end;
        this.type = type;
    }

    @Override
    public String detailed() {
        return objectType + "{"
               + "id=" + Formats.number(id) + ", type='" + type + "', start=" + start + ", "
               + "end=" + end + ", properties=" + properties + '}';
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
    public int compareTo(DbRelationship o) {
        return RELATIONSHIP_COMPARATOR.compare(this, o);
    }

    /** Builder for {@link DbRelationship}. */
    public static class DbRelationshipBuilder extends DbEntityBuilder<DbRelationship, DbRelationshipBuilder> {

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
