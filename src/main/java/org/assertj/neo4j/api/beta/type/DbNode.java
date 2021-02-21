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

import org.assertj.core.util.IterableUtil;
import org.assertj.neo4j.api.beta.util.Formats;
import org.assertj.neo4j.api.beta.util.Utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * TODO : Maybe extract method here in a interface to be able to decorate a Node from driver - This may have impact for
 * comparing node.
 */
public class DbNode extends DbEntity<DbNode> {

    public static final Comparator<DbNode> NODE_COMPARATOR = Utils.comparators(
            Representable::objectType,
            DbNode::getId
    );

    protected SortedSet<String> labels;

    DbNode(final Long id, final Iterable<String> labels, final Map<String, DbValue> properties) {
        super(ObjectType.NODE, DbNode.class, id, properties);
        this.labels = new TreeSet<>(IterableUtil.toCollection(labels));
    }

    @Override
    public String detailed() {
        return objectType + "{"
               + "id=" + Formats.number(id) + ", labels=" + Formats.strings(labels) + ", "
               + "properties=" + properties + '}';
    }

    @Override
    public int compareTo(final DbNode other) {
        return NODE_COMPARATOR.compare(this, other);
    }

    public SortedSet<String> getLabels() {
        return labels;
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

    /** Builder for {@link DbNode}. */
    public static class DbNodeBuilder extends DbEntityBuilder<DbNode, DbNodeBuilder> {

        private final Set<String> labels = new HashSet<>();

        DbNodeBuilder() {
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
