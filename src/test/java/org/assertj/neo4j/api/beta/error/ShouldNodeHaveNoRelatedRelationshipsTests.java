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
package org.assertj.neo4j.api.beta.error;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.neo4j.api.beta.testing.Randomize;
import org.assertj.neo4j.api.beta.type.DbNode;
import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.assertj.neo4j.api.beta.type.Entities;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 13/02/2021
 */
class ShouldNodeHaveNoRelatedRelationshipsTests {

    @Nested
    class CreateIncomingTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final DbRelationship relationship1 = Entities.relationship().id(1).end(42).build();
            final DbRelationship relationship2 = Entities.relationship().id(2).end(42).build();
            final DbRelationship relationship3 = Entities.relationship().id(3).end(42).build();
            final List<DbRelationship> relationships = Randomize.listOf(relationship1, relationship2, relationship3);
            final DbNode actual = Entities.node().id(42).build();

            // WHEN
            final ErrorMessageFactory error = ShouldNodeHaveNoRelatedRelationships
                    .createIncoming(actual, relationships);

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting node:\n"
                    + "  <NODE{id=42L, labels=[], properties={}}>\n"
                    + "to have no incoming relationships but found:\n"
                    + "  <[RELATIONSHIP{id=1L, type='null', start=null, end=42, properties={}},\n"
                    + "    RELATIONSHIP{id=2L, type='null', start=null, end=42, properties={}},\n"
                    + "    RELATIONSHIP{id=3L, type='null', start=null, end=42, properties={}}]>\n"
            );
        }

    }

    @Nested
    class CreateOutgoingTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final DbRelationship relationship1 = Entities.relationship().id(1).start(42).build();
            final DbRelationship relationship2 = Entities.relationship().id(2).start(42).build();
            final DbRelationship relationship3 = Entities.relationship().id(3).start(42).build();
            final List<DbRelationship> relationships = Randomize.listOf(relationship1, relationship2, relationship3);
            final DbNode actual = Entities.node().id(42).build();

            // WHEN
            final ErrorMessageFactory error = ShouldNodeHaveNoRelatedRelationships
                    .createOutgoing(actual, relationships);

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting node:\n"
                    + "  <NODE{id=42L, labels=[], properties={}}>\n"
                    + "to have no outgoing relationships but found:\n"
                    + "  <[RELATIONSHIP{id=1L, type='null', start=42, end=null, properties={}},\n"
                    + "    RELATIONSHIP{id=2L, type='null', start=42, end=null, properties={}},\n"
                    + "    RELATIONSHIP{id=3L, type='null', start=42, end=null, properties={}}]>\n"
            );
        }

    }

    @Nested
    class IncomingElementsTests {

        @Test
        void should_generate_an_aggregate_error_message() {
            // GIVEN
            final DbRelationship relationship1 = Entities.relationship().id(1).end(22).build();
            final DbRelationship relationship2 = Entities.relationship().id(2).end(42).build();
            final DbRelationship relationship3 = Entities.relationship().id(3).end(69).build();
            final List<DbRelationship> relationships = Randomize.listOf(relationship1, relationship2, relationship3);
            final DbNode node1 = Entities.node().id(22).build();
            final DbNode node2 = Entities.node().id(29).build();
            final DbNode node3 = Entities.node().id(35).build();
            final DbNode node4 = Entities.node().id(42).build();
            final DbNode node5 = Entities.node().id(56).build();
            final DbNode node6 = Entities.node().id(69).build();
            final List<DbNode> actual = Randomize.listOf(node1, node2, node3, node4, node5, node6);

            // WHEN
            final ErrorMessageFactory error = ShouldNodeHaveNoRelatedRelationships
                    .incomingElements(actual, relationships)
                    .notSatisfies(Randomize.listOf(node1, node4, node6));

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting nodes:\n"
                    + "  <[NODE{id=22L, labels=[], properties={}},\n"
                    + "    NODE{id=29L, labels=[], properties={}},\n"
                    + "    NODE{id=35L, labels=[], properties={}},\n"
                    + "    NODE{id=42L, labels=[], properties={}},\n"
                    + "    NODE{id=56L, labels=[], properties={}},\n"
                    + "    NODE{id=69L, labels=[], properties={}}]>\n"
                    + "to have no incoming relationships but found:\n"
                    + "  <[RELATIONSHIP{id=1L, type='null', start=null, end=22, properties={}},\n"
                    + "    RELATIONSHIP{id=2L, type='null', start=null, end=42, properties={}},\n"
                    + "    RELATIONSHIP{id=3L, type='null', start=null, end=69, properties={}}]>\n"
                    + "which are incoming relationships to nodes:\n"
                    + "\n"
                    + "  1) NODE{id=22L, labels=[], properties={}}\n"
                    + "    - Incoming relationships:: [RELATIONSHIP{id=1L, type='null', start=null, end=22, "
                    + "properties={}}]\n"
                    + "\n"
                    + "  2) NODE{id=42L, labels=[], properties={}}\n"
                    + "    - Incoming relationships:: [RELATIONSHIP{id=2L, type='null', start=null, end=42, "
                    + "properties={}}]\n"
                    + "\n"
                    + "  3) NODE{id=69L, labels=[], properties={}}\n"
                    + "    - Incoming relationships:: [RELATIONSHIP{id=3L, type='null', start=null, end=69, "
                    + "properties={}}]\n"
            );
        }
    }

    @Nested
    class OutgoingElementsTests {

        @Test
        void should_generate_an_aggregate_error_message() {
            // GIVEN
            final DbRelationship relationship1 = Entities.relationship().id(1).start(22).build();
            final DbRelationship relationship2 = Entities.relationship().id(2).start(42).build();
            final DbRelationship relationship3 = Entities.relationship().id(3).start(69).build();
            final List<DbRelationship> relationships = Randomize.listOf(relationship1, relationship2, relationship3);
            final DbNode node1 = Entities.node().id(22).build();
            final DbNode node2 = Entities.node().id(29).build();
            final DbNode node3 = Entities.node().id(35).build();
            final DbNode node4 = Entities.node().id(42).build();
            final DbNode node5 = Entities.node().id(56).build();
            final DbNode node6 = Entities.node().id(69).build();
            final List<DbNode> actual = Randomize.listOf(node1, node2, node3, node4, node5, node6);

            // WHEN
            final ErrorMessageFactory error = ShouldNodeHaveNoRelatedRelationships
                    .outgoingElements(actual, relationships)
                    .notSatisfies(Randomize.listOf(node1, node4, node6));

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting nodes:\n"
                    + "  <[NODE{id=22L, labels=[], properties={}},\n"
                    + "    NODE{id=29L, labels=[], properties={}},\n"
                    + "    NODE{id=35L, labels=[], properties={}},\n"
                    + "    NODE{id=42L, labels=[], properties={}},\n"
                    + "    NODE{id=56L, labels=[], properties={}},\n"
                    + "    NODE{id=69L, labels=[], properties={}}]>\n"
                    + "to have no outgoing relationships but found:\n"
                    + "  <[RELATIONSHIP{id=1L, type='null', start=22, end=null, properties={}},\n"
                    + "    RELATIONSHIP{id=2L, type='null', start=42, end=null, properties={}},\n"
                    + "    RELATIONSHIP{id=3L, type='null', start=69, end=null, properties={}}]>\n"
                    + "which are outgoing relationships to nodes:\n"
                    + "\n"
                    + "  1) NODE{id=22L, labels=[], properties={}}\n"
                    + "    - Outgoing relationships:: [RELATIONSHIP{id=1L, type='null', start=22, end=null, "
                    + "properties={}}]\n"
                    + "\n"
                    + "  2) NODE{id=42L, labels=[], properties={}}\n"
                    + "    - Outgoing relationships:: [RELATIONSHIP{id=2L, type='null', start=42, end=null, "
                    + "properties={}}]\n"
                    + "\n"
                    + "  3) NODE{id=69L, labels=[], properties={}}\n"
                    + "    - Outgoing relationships:: [RELATIONSHIP{id=3L, type='null', start=69, end=null, "
                    + "properties={}}]\n"
            );
        }
    }

}
