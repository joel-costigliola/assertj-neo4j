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
import org.assertj.core.util.Streams;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.DbValue;
import org.assertj.neo4j.api.beta.type.Nodes.DbNode;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.type.Relationships.DbRelationship;
import org.assertj.neo4j.api.beta.type.ValueType;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Patrick Allain - 13/02/2021
 */
public class EntityUtils {

    /** Entity comparator. */
    public static final Comparator<? extends DbEntity> ENTITY_COMPARATOR = Comparator
            .comparing(DbEntity::getId, Comparator.nullsFirst(Comparator.naturalOrder()));

    public static <E extends DbEntity> String recordTypeSingular(final E actual) {
        return actual.getRecordType().name().toLowerCase();
    }

    public static <E extends DbEntity> String recordTypePlural(final E actual) {
        return recordTypeSingular(actual) + "s";
    }

    public static <E extends DbEntity> String recordTypePlural(final Iterable<E> iterable) {
        return recordType(iterable).name().toLowerCase() + "s";
    }

    /**
     * Return the {@link RecordType} of entities.
     *
     * @param iterable the entities iterable
     * @param <E>      the type of entity
     * @return the record type.
     */
    public static <E extends DbEntity> RecordType recordType(final Iterable<E> iterable) {
        return Streams.stream(iterable)
                .map(DbEntity::getRecordType)
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    public static <E extends DbEntity> Comparator<? super E> comparator() {
        return (Comparator<? super E>) ENTITY_COMPARATOR;
    }

    public static <E extends DbEntity> Map<String, Object> propertyObjects(final E entity) {
        return propertyObjects(entity.getProperties());
    }

    public static Map<String, Object> propertyObjects(final Map<String, DbValue> properties) {
        return properties.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getContent()));
    }

    public static <E extends DbEntity> List<DbValue> propertyList(final E entity, final String key) {
        final DbValue property = Objects
                .requireNonNull(entity.getProperty(key), "Property key \"" + key + "\" doesn't exist");
        final Object content = property.getContent();
        if (property.getType() != ValueType.LIST || !(content instanceof List)) {
            throw new IllegalArgumentException("Property key \"" + key + "\" is not a list composite type");
        }
        return (List<DbValue>) content;
    }

    public static <E extends DbEntity> ValueType properlyLisType(final E entity, final String key) {
        return propertyList(entity, key).stream()
                .map(DbValue::getType)
                .findFirst()
                .orElse(null);
    }

    public static <E extends DbEntity> List<Object> properlyListValues(final E entity, final String key) {
        return propertyList(entity, key).stream().map(DbValue::getContent).collect(Collectors.toList());
    }

    /**
     * Sort a entities list.
     *
     * @param entities the entities
     * @param <E>      the type of entity
     * @return a list of sorted entities
     */
    public static <E extends DbEntity> List<E> sorted(final Iterable<E> entities) {
        return IterableUtil.toCollection(entities).stream().sorted(comparator()).collect(Collectors.toList());
    }

    /**
     * Filter incoming relationships for a given node.
     *
     * @param node          the node
     * @param relationships the relationships to filter
     * @return the filtered list of relationships
     */
    public static List<DbRelationship> areIncomingForNode(DbNode node, List<DbRelationship> relationships) {
        return relationships.stream()
                .filter(r -> Objects.equals(r.getEnd(), node.getId()))
                .sorted(comparator())
                .collect(Collectors.toList());
    }

    /**
     * Filter outgoing relationships for a given node.
     *
     * @param node          the node
     * @param relationships the relationships to filter
     * @return the filtered list of relationships
     */
    public static List<DbRelationship> areOutgoingForNode(DbNode node, List<DbRelationship> relationships) {
        return relationships.stream()
                .filter(r -> Objects.equals(r.getStart(), node.getId()))
                .sorted(comparator())
                .collect(Collectors.toList());
    }

    /**
     * Filter nodes which have incoming relationships with the provided relationships list.
     *
     * @param nodes         the nodes
     * @param relationships the relationships
     * @return the filtered nodes
     */
    public static List<DbNode> havingIncomingRelationships(final List<DbNode> nodes,
                                                           final List<DbRelationship> relationships) {
        final List<Long> endNodeIds = EntityUtils.endNodeIds(relationships);
        return nodes.stream()
                .filter(n -> endNodeIds.contains(n.getId()))
                .sorted(comparator())
                .collect(Collectors.toList());
    }

    /**
     * Filter nodes which have outgoing relationships with the provided relationships list.
     *
     * @param nodes         the nodes
     * @param relationships the relationships
     * @return the filtered nodes
     */
    public static List<DbNode> havingOutgoingRelationships(final List<DbNode> nodes,
                                                           final List<DbRelationship> relationships) {
        final List<Long> startNodeIds = EntityUtils.startNodeIds(relationships);
        return nodes.stream()
                .filter(n -> startNodeIds.contains(n.getId()))
                .sorted(comparator())
                .collect(Collectors.toList());
    }

    /**
     * For a relationships list, return the starting node ids.
     * <p/>
     * For a relationship, this is the node id where the relationship is starting.
     *
     * @param relationships the list of relationships
     * @return the starting node ids
     */
    public static List<Long> startNodeIds(List<DbRelationship> relationships) {
        return relationships.stream()
                .map(DbRelationship::getStart)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * For a relationships list, return the ending node ids.
     * <p/>
     * For a relationship, this is the node id where the relationship is ending.
     *
     * @param relationships the list of relationships
     * @return the starting node ids
     */
    public static List<Long> endNodeIds(List<DbRelationship> relationships) {
        return relationships.stream()
                .map(DbRelationship::getEnd)
                .sorted()
                .collect(Collectors.toList());
    }

    private EntityUtils() {
    }
}
