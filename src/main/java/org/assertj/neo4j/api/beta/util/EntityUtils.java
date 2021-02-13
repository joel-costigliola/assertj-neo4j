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
package org.assertj.neo4j.api.beta.util;

import org.assertj.core.util.IterableUtil;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.Nodes.DbNode;
import org.assertj.neo4j.api.beta.type.Relationships.DbRelationship;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author patouche - 13/02/2021
 */
public class EntityUtils {

    public static final Comparator<? extends DbEntity<?>> ENTITY_COMPARATOR = Comparator
            .comparing(DbEntity::getId, Comparator.nullsFirst(Comparator.naturalOrder()));

    @SuppressWarnings("unchecked")
    public static <E extends DbEntity<E>> Comparator<? super E> comparator() {
        return (Comparator<? super E>) ENTITY_COMPARATOR;
    }

    public static <E extends DbEntity<E>> List<E> sorted(final Iterable<E> entities) {
        return IterableUtil.toCollection(entities).stream().sorted(comparator()).collect(Collectors.toList());
    }

    public static List<DbRelationship> areIncomingForNode(DbNode node, List<DbRelationship> relationships) {
        return relationships.stream()
                .filter(r -> Objects.equals(r.getEnd(), node.getId()))
                .sorted(comparator())
                .collect(Collectors.toList());
    }

    public static List<DbRelationship> areOutgoingForNode(DbNode node, List<DbRelationship> relationships) {
        return relationships.stream()
                .filter(r -> Objects.equals(r.getStart(), node.getId()))
                .sorted(comparator())
                .collect(Collectors.toList());
    }

    public static List<DbNode> havingIncomingRelationships(final List<DbNode> nodes,
                                                           final List<DbRelationship> relationships) {
        final List<Long> endNodeIds = EntityUtils.endNodeIds(relationships);
        return nodes.stream()
                .filter(n -> endNodeIds.contains(n.getId()))
                .sorted(comparator())
                .collect(Collectors.toList());
    }

    public static List<DbNode> havingOutgoingRelationships(final List<DbNode> nodes,
                                                           final List<DbRelationship> relationships) {
        final List<Long> startNodeIds = EntityUtils.startNodeIds(relationships);
        return nodes.stream()
                .filter(n -> startNodeIds.contains(n.getId()))
                .sorted(comparator())
                .collect(Collectors.toList());
    }

    public static List<Long> startNodeIds(List<DbRelationship> relationships) {
        return relationships.stream()
                .map(DbRelationship::getStart)
                .sorted()
                .collect(Collectors.toList());
    }

    public static List<Long> endNodeIds(List<DbRelationship> relationships) {
        return relationships.stream()
                .map(DbRelationship::getEnd)
                .sorted()
                .collect(Collectors.toList());
    }

    private EntityUtils() {
    }
}
