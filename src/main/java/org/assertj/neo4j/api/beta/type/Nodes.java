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

import org.assertj.core.util.Lists;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionConfig;
import org.neo4j.driver.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author patouche - 31/10/2020
 */
public class Nodes extends LoadingType<Nodes.DbNode> {

    /** The node labels. */
    private final List<String> labels;

    public Nodes(final Driver driver, final String... labels) {
        super(driver, RecordType.NODE);
        this.labels = Arrays.asList(labels);
    }

    public static DbNodeBuilder node() {
        return new DbNodeBuilder();
    }

    public List<DbNode> load() {
        try (Session session = this.driver.session()) {
            final String queryLabels = labels.stream().map(i -> ":" + i).collect(Collectors.joining(""));
            final String query = String.format("MATCH (n %s) RETURN n", queryLabels);
            final Result result = session.run(query, TransactionConfig.builder().build());
            final List<Record> records = result.list();
            return records.stream()
                    .map(Record::values)
                    .flatMap(Collection::stream)
                    .map(Value::asNode)
                    .map(n -> new DbNode(n.id(), Lists.newArrayList(n.labels()), n.asMap()))
                    .collect(Collectors.toList());
        }
    }

    public static class DbNode extends DbEntity<DbNode> {

        protected List<String> labels;

        public DbNode(final List<String> labels, final Map<String, Object> properties) {
            this(null, labels, properties);
        }

        public DbNode(final Long id, final List<String> labels, final Map<String, Object> properties) {
            super(RecordType.NODE, id, properties);
            this.labels = labels;
        }

        public List<String> getLabels() {
            return labels;
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

    public static class DbNodeBuilder {

        private Long id = null;

        private final List<String> labels = new ArrayList<>();

        private final Map<String, Object> properties = new HashMap<>();

        private DbNodeBuilder() {
        }

        public DbNodeBuilder id(final int id) {
            return this.id((long) id);
        }

        public DbNodeBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public DbNodeBuilder property(final String key, final Object value) {
            this.properties.put(key, value);
            return this;
        }

        public DbNodeBuilder label(final String label) {
            this.labels.add(label);
            return this;
        }

        public DbNode build() {
            return new DbNode(id, labels, properties);
        }

    }

}

