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
import org.assertj.neo4j.api.beta.type.DbValue;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes.DbNode;
import org.assertj.neo4j.api.beta.type.Relationships.DbRelationship;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.junit.jupiter.api.DisplayName;
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
class EntityUtilsTests {

    @Nested
    class SortedTests {

        @Test
        void should_sort_entities_by_ids() {
            // GIVEN
            final DbNode node1 = Drivers.node().id(11).build();
            final DbNode node2 = Drivers.node().id(16).build();
            final DbNode node3 = Drivers.node().id(42).build();
            final DbNode node4 = Drivers.node().id(64).build();
            final DbNode node5 = Drivers.node().id(69).build();
            final DbNode node6 = Drivers.node().id(98).build();
            final Iterable<DbNode> entities = Randomize.listOf(node1, node2, node3, node4, node5, node6);

            // WHEN
            final List<DbNode> result = EntityUtils.sorted(entities);

            // THEN
            assertThat(result).containsExactly(node1, node2, node3, node4, node5, node6);
        }

        @Test
        void should_support_null_ids() {
            // GIVEN
            final DbNode node1 = Drivers.node().id(11).build();
            final DbNode node2 = Drivers.node().build();
            final DbNode node3 = Drivers.node().id(42).build();
            final DbNode node4 = Drivers.node().build();
            final DbNode node5 = Drivers.node().id(69).build();
            final DbNode node6 = Drivers.node().id(98).build();
            final Iterable<DbNode> entities = Randomize.listOf(node1, node2, node3, node4, node5, node6);

            // WHEN
            final List<DbNode> result = EntityUtils.sorted(entities);

            // THEN
            assertThat(result)
                    .hasSize(6)
                    .containsSubsequence(node2, node1)
                    .containsSubsequence(node4, node1)
                    .containsSubsequence(node1, node3, node5, node6);
        }

    }

    @Nested
    class AreIncomingForNodeTests {

        @Test
        void should_filter_only_incoming_relationships_of_node() {
            // GIVEN
            final DbNode node = Drivers.node().id(42).build();
            final DbRelationship relationship1 = Drivers.relation().id(1).end(22).build();
            final DbRelationship relationship2 = Drivers.relation().id(2).end(29).build();
            final DbRelationship relationship3 = Drivers.relation().id(3).end(35).build();
            final DbRelationship relationship4 = Drivers.relation().id(4).end(42).build();
            final DbRelationship relationship5 = Drivers.relation().id(5).end(56).build();
            final DbRelationship relationship6 = Drivers.relation().id(6).end(42).build();
            final List<DbRelationship> relationships = Randomize
                    .listOf(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6);

            // WHEN
            final List<DbRelationship> result = EntityUtils.areIncomingForNode(node, relationships);

            // THEN
            assertThat(result).containsExactly(relationship4, relationship6);
        }
    }

    @Nested
    class AreOutgoingForNodeTests {

        @Test
        void should_filter_only_outgoing_relationships_of_node() {
            // GIVEN
            final DbNode node = Drivers.node().id(42).build();
            final DbRelationship relationship1 = Drivers.relation().id(1).start(22).build();
            final DbRelationship relationship2 = Drivers.relation().id(2).start(29).build();
            final DbRelationship relationship3 = Drivers.relation().id(3).start(35).build();
            final DbRelationship relationship4 = Drivers.relation().id(4).start(42).build();
            final DbRelationship relationship5 = Drivers.relation().id(5).start(56).build();
            final DbRelationship relationship6 = Drivers.relation().id(6).start(42).build();
            final List<DbRelationship> relationships = Randomize
                    .listOf(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6);

            // WHEN
            final List<DbRelationship> result = EntityUtils.areOutgoingForNode(node, relationships);

            // THEN
            assertThat(result).containsExactly(relationship4, relationship6);
        }
    }

    @Nested
    class HavingIncomingRelationshipsTests {

        @Test
        void should_filter_only_node_with_incoming_relationships() {
            // GIVEN
            final DbNode node1 = Drivers.node().id(22).build();
            final DbNode node2 = Drivers.node().id(23).build();
            final DbNode node3 = Drivers.node().id(42).build();
            final DbNode node4 = Drivers.node().id(74).build();
            final DbNode node5 = Drivers.node().id(98).build();
            final List<DbNode> nodes = Randomize.listOf(node1, node2, node3, node4, node5);

            final DbRelationship relationship1 = Drivers.relation().id(1).end(22).build();
            final DbRelationship relationship2 = Drivers.relation().id(2).end(29).build();
            final DbRelationship relationship3 = Drivers.relation().id(3).end(35).build();
            final DbRelationship relationship4 = Drivers.relation().id(4).end(42).build();
            final DbRelationship relationship5 = Drivers.relation().id(5).end(56).build();
            final DbRelationship relationship6 = Drivers.relation().id(6).end(42).build();
            final List<DbRelationship> relationships = Randomize
                    .listOf(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6);

            // WHEN
            final List<DbNode> result = EntityUtils.havingIncomingRelationships(nodes, relationships);

            // THEN
            assertThat(result).containsExactly(node1, node3);
        }
    }

    @Nested
    class HavingOutgoingRelationshipsTests {

        @Test
        void should_filter_only_node_with_outgoing_relationships() {
            // GIVEN
            final DbNode node1 = Drivers.node().id(22).build();
            final DbNode node2 = Drivers.node().id(23).build();
            final DbNode node3 = Drivers.node().id(42).build();
            final DbNode node4 = Drivers.node().id(74).build();
            final DbNode node5 = Drivers.node().id(98).build();
            final List<DbNode> nodes = Randomize.listOf(node1, node2, node3, node4, node5);

            final DbRelationship relationship1 = Drivers.relation().id(1).start(22).build();
            final DbRelationship relationship2 = Drivers.relation().id(2).start(29).build();
            final DbRelationship relationship3 = Drivers.relation().id(3).start(35).build();
            final DbRelationship relationship4 = Drivers.relation().id(4).start(42).build();
            final DbRelationship relationship5 = Drivers.relation().id(5).start(56).build();
            final DbRelationship relationship6 = Drivers.relation().id(6).start(42).build();
            final List<DbRelationship> relationships = Randomize
                    .listOf(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6);

            // WHEN
            final List<DbNode> result = EntityUtils.havingOutgoingRelationships(nodes, relationships);

            // THEN
            assertThat(result).containsExactly(node1, node3);
        }
    }

    @Nested
    class StartNodeIdsTests {

        @Test
        void should_return_the_start_node_ids() {
            // GIVEN
            final DbRelationship relationship1 = Drivers.relation().id(1).start(22).build();
            final DbRelationship relationship2 = Drivers.relation().id(2).start(29).build();
            final DbRelationship relationship3 = Drivers.relation().id(3).start(35).build();
            final DbRelationship relationship4 = Drivers.relation().id(4).start(42).build();
            final DbRelationship relationship5 = Drivers.relation().id(5).start(56).build();
            final DbRelationship relationship6 = Drivers.relation().id(6).start(42).build();
            final List<DbRelationship> relationships = Randomize
                    .listOf(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6);

            // WHEN
            final List<Long> result = EntityUtils.startNodeIds(relationships);

            // THEN
            assertThat(result).containsExactly(22L, 29L, 35L, 42L, 42L, 56L);
        }

    }

    @Nested
    class EndNodeIdsTests {

        @Test
        void should_return_the_end_node_ids() {
            // GIVEN
            final DbRelationship relationship1 = Drivers.relation().id(1).end(22).build();
            final DbRelationship relationship2 = Drivers.relation().id(2).end(29).build();
            final DbRelationship relationship3 = Drivers.relation().id(3).end(35).build();
            final DbRelationship relationship4 = Drivers.relation().id(4).end(42).build();
            final DbRelationship relationship5 = Drivers.relation().id(5).end(56).build();
            final DbRelationship relationship6 = Drivers.relation().id(6).end(42).build();
            final List<DbRelationship> relationships = Randomize
                    .listOf(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6);

            // WHEN
            final List<Long> result = EntityUtils.endNodeIds(relationships);

            // THEN
            assertThat(result).containsExactly(22L, 29L, 35L, 42L, 42L, 56L);
        }

    }

    @Nested
    @DisplayName("getPropertyList")
    @ExtendWith(SoftAssertionsExtension.class)
    class PropertyListTests {

        @Test
        void should_throw_an_exception() {
            // WHEN & THEN
            assertThatThrownBy(() -> EntityUtils.propertyList(Samples.NODE_LIST, "key_doesnt_exist"))
                    .hasMessage("Property key \"key_doesnt_exist\" doesn't exist")
                    .hasNoCause();
        }

        @Test
        void should_return_the_right_property_type(final SoftAssertions softly) {
            // WHEN & THEN
            softly.assertThat(EntityUtils.propertyList(Samples.NODE_LIST, "list_long"))
                    .hasSize(10)
                    .contains(IntStream.range(0, 10).mapToObj(ValueType::convert).toArray(DbValue[]::new));

            softly.assertThat(EntityUtils.propertyList(Samples.NODE_LIST, "list_string"))
                    .hasSize(3)
                    .contains(
                            ValueType.convert("str-1"),
                            ValueType.convert("str-2"),
                            ValueType.convert("str-3")
                    );
        }

    }

}

