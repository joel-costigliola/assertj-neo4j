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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    class DbNode extends DbEntity<DbNode> {

        protected List<String> labels;

        DbNode(final List<String> labels, final Map<String, DbValue> properties) {
            this(null, labels, properties);
        }

        DbNode(final Long id, final List<String> labels, final Map<String, DbValue> properties) {
            super(RecordType.NODE, id, properties);
            this.labels = labels;
        }

        public List<String> getLabels() {
            return labels;
        }

        public List<String> getSortedLabels() {
            return labels.stream().sorted().collect(Collectors.toList());
        }

        public void setLabels(final List<String> labels) {
            this.labels = labels;
        }

        @Override
        public DbNode withoutId() {
            return new DbNode(this.labels, this.properties);
        }

        @Override
        public String toString() {
            return entityRepresentation("labels=" + labels);
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
    class DbNodeBuilder {

        private Long id = null;

        private final List<String> labels = new ArrayList<>();

        private final Map<String, DbValue> properties = new HashMap<>();

        protected DbNodeBuilder() {
        }

        public DbNodeBuilder id(final int id) {
            return this.id((long) id);
        }

        public DbNodeBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public DbNodeBuilder property(final String key, final Object value) {
            this.properties.put(key, ValueType.convert(value));
            return this;
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

