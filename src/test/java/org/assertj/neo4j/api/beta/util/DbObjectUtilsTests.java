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

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.assertj.neo4j.api.beta.testing.Randomize;
import org.assertj.neo4j.api.beta.testing.Samples;
import org.assertj.neo4j.api.beta.type.DbNode;
import org.assertj.neo4j.api.beta.type.DbObject;
import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.assertj.neo4j.api.beta.type.DbValue;
import org.assertj.neo4j.api.beta.type.Entities;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Patrick Allain - 13/02/2021
 */
class DbObjectUtilsTests {

    @Nested
    class AreIncomingForNodeTests {

        @Test
        void should_filter_only_incoming_relationships_of_node() {
            // GIVEN
            final DbNode node = Entities.node().id(42).build();
            final DbRelationship relationship1 = Entities.relationship().id(1).end(22).build();
            final DbRelationship relationship2 = Entities.relationship().id(2).end(29).build();
            final DbRelationship relationship3 = Entities.relationship().id(3).end(35).build();
            final DbRelationship relationship4 = Entities.relationship().id(4).end(42).build();
            final DbRelationship relationship5 = Entities.relationship().id(5).end(56).build();
            final DbRelationship relationship6 = Entities.relationship().id(6).end(42).build();
            final List<DbRelationship> relationships = Randomize
                    .listOf(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6);

            // WHEN
            final List<DbRelationship> result = DbObjectUtils.areIncomingForNode(node, relationships);

            // THEN
            assertThat(result).containsExactly(relationship4, relationship6);
        }
    }

    @Nested
    class AreOutgoingForNodeTests {

        @Test
        void should_filter_only_outgoing_relationships_of_node() {
            // GIVEN
            final DbNode node = Entities.node().id(42).build();
            final DbRelationship relationship1 = Entities.relationship().id(1).start(22).build();
            final DbRelationship relationship2 = Entities.relationship().id(2).start(29).build();
            final DbRelationship relationship3 = Entities.relationship().id(3).start(35).build();
            final DbRelationship relationship4 = Entities.relationship().id(4).start(42).build();
            final DbRelationship relationship5 = Entities.relationship().id(5).start(56).build();
            final DbRelationship relationship6 = Entities.relationship().id(6).start(42).build();
            final List<DbRelationship> relationships = Randomize
                    .listOf(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6);

            // WHEN
            final List<DbRelationship> result = DbObjectUtils.areOutgoingForNode(node, relationships);

            // THEN
            assertThat(result).containsExactly(relationship4, relationship6);
        }
    }

    @Nested
    class HavingIncomingRelationshipsTests {

        @Test
        void should_filter_only_node_with_incoming_relationships() {
            // GIVEN
            final DbNode node1 = Entities.node().id(22).build();
            final DbNode node2 = Entities.node().id(23).build();
            final DbNode node3 = Entities.node().id(42).build();
            final DbNode node4 = Entities.node().id(74).build();
            final DbNode node5 = Entities.node().id(98).build();
            final List<DbNode> nodes = Randomize.listOf(node1, node2, node3, node4, node5);

            final DbRelationship relationship1 = Entities.relationship().id(1).end(22).build();
            final DbRelationship relationship2 = Entities.relationship().id(2).end(29).build();
            final DbRelationship relationship3 = Entities.relationship().id(3).end(35).build();
            final DbRelationship relationship4 = Entities.relationship().id(4).end(42).build();
            final DbRelationship relationship5 = Entities.relationship().id(5).end(56).build();
            final DbRelationship relationship6 = Entities.relationship().id(6).end(42).build();
            final List<DbRelationship> relationships = Randomize
                    .listOf(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6);

            // WHEN
            final List<DbNode> result = DbObjectUtils.havingIncomingRelationships(nodes, relationships);

            // THEN
            assertThat(result).containsExactly(node1, node3);
        }
    }

    @Nested
    class HavingOutgoingRelationshipsTests {

        @Test
        void should_filter_only_node_with_outgoing_relationships() {
            // GIVEN
            final DbNode node1 = Entities.node().id(22).build();
            final DbNode node2 = Entities.node().id(23).build();
            final DbNode node3 = Entities.node().id(42).build();
            final DbNode node4 = Entities.node().id(74).build();
            final DbNode node5 = Entities.node().id(98).build();
            final List<DbNode> nodes = Randomize.listOf(node1, node2, node3, node4, node5);

            final DbRelationship relationship1 = Entities.relationship().id(1).start(22).build();
            final DbRelationship relationship2 = Entities.relationship().id(2).start(29).build();
            final DbRelationship relationship3 = Entities.relationship().id(3).start(35).build();
            final DbRelationship relationship4 = Entities.relationship().id(4).start(42).build();
            final DbRelationship relationship5 = Entities.relationship().id(5).start(56).build();
            final DbRelationship relationship6 = Entities.relationship().id(6).start(42).build();
            final List<DbRelationship> relationships = Randomize
                    .listOf(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6);

            // WHEN
            final List<DbNode> result = DbObjectUtils.havingOutgoingRelationships(nodes, relationships);

            // THEN
            assertThat(result).containsExactly(node1, node3);
        }
    }

    @Nested
    class StartNodeIdsTests {

        @Test
        void should_return_the_start_node_ids() {
            // GIVEN
            final DbRelationship relationship1 = Entities.relationship().id(1).start(22).build();
            final DbRelationship relationship2 = Entities.relationship().id(2).start(29).build();
            final DbRelationship relationship3 = Entities.relationship().id(3).start(35).build();
            final DbRelationship relationship4 = Entities.relationship().id(4).start(42).build();
            final DbRelationship relationship5 = Entities.relationship().id(5).start(56).build();
            final DbRelationship relationship6 = Entities.relationship().id(6).start(42).build();
            final List<DbRelationship> relationships = Randomize
                    .listOf(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6);

            // WHEN
            final List<Long> result = DbObjectUtils.startNodeIds(relationships);

            // THEN
            assertThat(result).containsExactly(22L, 29L, 35L, 42L, 42L, 56L);
        }

    }

    @Nested
    class ArrayStartNodeIdsTests {

        @Test
        void should_return_the_start_node_ids() {
            // GIVEN
            final DbRelationship relationship1 = Entities.relationship().id(1).start(22).build();
            final DbRelationship relationship2 = Entities.relationship().id(2).start(29).build();
            final DbRelationship relationship3 = Entities.relationship().id(3).start(35).build();
            final DbRelationship relationship4 = Entities.relationship().id(4).start(42).build();
            final DbRelationship relationship5 = Entities.relationship().id(5).start(56).build();
            final DbRelationship relationship6 = Entities.relationship().id(6).start(42).build();
            final List<DbRelationship> relationships = Randomize
                    .listOf(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6);

            // WHEN
            final Long[] result = DbObjectUtils.arrayStartNodeIds(relationships);

            // THEN
            assertThat(result).containsExactly(22L, 29L, 35L, 42L, 42L, 56L);
        }
    }

    @Nested
    class EndNodeIdsTests {

        @Test
        void should_return_the_end_node_ids() {
            // GIVEN
            final DbRelationship relationship1 = Entities.relationship().id(1).end(22).build();
            final DbRelationship relationship2 = Entities.relationship().id(2).end(29).build();
            final DbRelationship relationship3 = Entities.relationship().id(3).end(35).build();
            final DbRelationship relationship4 = Entities.relationship().id(4).end(42).build();
            final DbRelationship relationship5 = Entities.relationship().id(5).end(56).build();
            final DbRelationship relationship6 = Entities.relationship().id(6).end(42).build();
            final List<DbRelationship> relationships = Randomize
                    .listOf(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6);

            // WHEN
            final List<Long> result = DbObjectUtils.endNodeIds(relationships);

            // THEN
            assertThat(result).containsExactly(22L, 29L, 35L, 42L, 42L, 56L);
        }

    }

    @Nested
    class ArrayEndNodeIdsTests {

        @Test
        void should_return_the_end_node_ids() {
            // GIVEN
            final DbRelationship relationship1 = Entities.relationship().id(1).end(22).build();
            final DbRelationship relationship2 = Entities.relationship().id(2).end(29).build();
            final DbRelationship relationship3 = Entities.relationship().id(3).end(35).build();
            final DbRelationship relationship4 = Entities.relationship().id(4).end(42).build();
            final DbRelationship relationship5 = Entities.relationship().id(5).end(56).build();
            final DbRelationship relationship6 = Entities.relationship().id(6).end(42).build();
            final List<DbRelationship> relationships = Randomize
                    .listOf(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6);

            // WHEN
            final Long[] result = DbObjectUtils.arrayEndNodeIds(relationships);

            // THEN
            assertThat(result).containsExactly(22L, 29L, 35L, 42L, 42L, 56L);
        }

    }

    @Nested
    @ExtendWith(SoftAssertionsExtension.class)
    class PropertyListTests {

        @Test
        void should_throw_an_exception_when_value_type_is_not_a_list() {
            // WHEN & THEN
            assertThatThrownBy(() -> DbObjectUtils.propertyList(Samples.NODE_LIST, "key_doesnt_exist"))
                    .hasMessage("Property key \"key_doesnt_exist\" doesn't exist")
                    .hasNoCause();
        }

        @Test
        void should_return_the_right_property_type(final SoftAssertions softly) {
            // WHEN & THEN
            softly.assertThat(DbObjectUtils.propertyList(Samples.NODE_LIST, "list_long"))
                    .hasSize(10)
                    .contains(IntStream.range(0, 10).mapToObj(DbValue::fromObject).toArray(DbValue[]::new));

            softly.assertThat(DbObjectUtils.propertyList(Samples.NODE_LIST, "list_string"))
                    .hasSize(3)
                    .contains(
                            DbValue.fromObject("str-1"),
                            DbValue.fromObject("str-2"),
                            DbValue.fromObject("str-3")
                    );
        }

    }

    @Nested
    class ProperlyListValuesTests {

        @Test
        void should_return_the_object() {
            // GIVEN
            final DbNode entity = Samples.NODE_LIST;

            // WHEN
            final List<Object> result = DbObjectUtils.properlyListValues(entity, "list_string");

            // THEN
            assertThat(result)
                    .hasSize(3)
                    .containsExactly("str-1", "str-2", "str-3");
        }
    }

    @Nested
    class SortedMixedTests {

        @Test
        void should_sort_mixed_db_objects() {
            // GIVEN
            final DbNode obj1 = Samples.NODE;
            final DbRelationship obj2 = Samples.RELATIONSHIP;
            final DbValue obj3 = DbValue.fromObject("value");
            final DbValue obj4 = DbValue.fromObject(true);
            final DbValue obj5 = DbValue.fromObject("other-value");
            final DbValue obj6 = DbValue.fromObject(3.14);
            final Iterable<DbObject> iterable = Randomize.listOf(obj1, obj2, obj3, obj4, obj5, obj6);

            // WHEN
            final List<DbObject> result = DbObjectUtils.sortedMixed(iterable);

            // THEN
            assertThat(result).containsExactly(obj1, obj2, obj6, obj5, obj3, obj4);
        }

    }

}

