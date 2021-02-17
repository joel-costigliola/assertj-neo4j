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
import org.neo4j.driver.Driver;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * {@link Nodes.DbNode} entities loader definition.
 *
 * @author Patrick Allain - 31/10/2020
 */
public interface Nodes extends DataLoader<Nodes.DbNode> {

    static Nodes of(Driver driver, String... labels) {
        return new NodeLoader(driver, labels);
    }

    /**
     * TODO : Maybe extract method here in a interface to be able to decorate a Node from driver - This may have impact
     * for comparing node.
     */
    class DbNode extends DbEntity {

        protected SortedSet<String> labels;

        DbNode(final Long id, final Set<String> labels, final Map<String, DbValue> properties) {
            super(RecordType.NODE, id, properties);
            this.labels = new TreeSet<>(labels);
        }

        public SortedSet<String> getLabels() {
            return labels;
        }

        @Override
        public String toString() {
            return entityRepresentation("labels=" + Formats.strings(labels));
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            final DbNode dbNode = (DbNode) o;
            return Objects.equals(labels, dbNode.labels);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), labels);
        }
    }

    /**
     * Builder for {@link DbNode}.
     */
    class DbNodeBuilder extends DbEntity.DbEntityBuilder<DbNode, DbNodeBuilder> {

        private final Set<String> labels = new HashSet<>();

        protected DbNodeBuilder() {
            super(DbNodeBuilder.class);
        }

        public DbNodeBuilder label(final String label) {
            this.labels.add(label);
            return this;
        }

        public DbNodeBuilder labels(final String... labels) {
            Arrays.stream(labels).forEach(this::label);
            return this;
        }

        public DbNode build() {
            return new DbNode(id, labels, properties);
        }

    }

}

