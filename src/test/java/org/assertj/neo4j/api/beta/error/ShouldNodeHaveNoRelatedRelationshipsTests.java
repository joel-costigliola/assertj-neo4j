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
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes.DbNode;
import org.assertj.neo4j.api.beta.type.Relationships.DbRelationship;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author patouche - 13/02/2021
 */
class ShouldNodeHaveNoRelatedRelationshipsTests {

    @Nested
    class CreateIncomingTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final DbRelationship relationship1 = Drivers.relation().id(1).end(42).build();
            final DbRelationship relationship2 = Drivers.relation().id(2).end(42).build();
            final DbRelationship relationship3 = Drivers.relation().id(3).end(42).build();
            final List<DbRelationship> relationships = Randomize.listOf(relationship1, relationship2, relationship3);
            final DbNode actual = Drivers.node().id(42).build();

            // WHEN
            final ErrorMessageFactory error = ShouldNodeHaveNoRelatedRelationships.createIncoming(actual,
                    relationships);

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\n"
                    + "Expecting node:\n"
                    + "  <NODE{id=42}>\n"
                    + "to have no incoming relationships but found:\n"
                    + "  <[RELATIONSHIP{id=1, type='null', start=null, end=42, properties={}},\n"
                    + "    RELATIONSHIP{id=2, type='null', start=null, end=42, properties={}},\n"
                    + "    RELATIONSHIP{id=3, type='null', start=null, end=42, properties={}}]>"
            );
        }

    }

    @Nested
    class CreateOutgoingTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final DbRelationship relationship1 = Drivers.relation().id(1).start(42).build();
            final DbRelationship relationship2 = Drivers.relation().id(2).start(42).build();
            final DbRelationship relationship3 = Drivers.relation().id(3).start(42).build();
            final List<DbRelationship> relationships = Randomize.listOf(relationship1, relationship2, relationship3);
            final DbNode actual = Drivers.node().id(42).build();

            // WHEN
            final ErrorMessageFactory error = ShouldNodeHaveNoRelatedRelationships.createOutgoing(actual,
                    relationships);

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\n"
                    + "Expecting node:\n"
                    + "  <NODE{id=42}>\n"
                    + "to have no outgoing relationships but found:\n"
                    + "  <[RELATIONSHIP{id=1, type='null', start=42, end=null, properties={}},\n"
                    + "    RELATIONSHIP{id=2, type='null', start=42, end=null, properties={}},\n"
                    + "    RELATIONSHIP{id=3, type='null', start=42, end=null, properties={}}]>"
            );
        }

    }

    @Nested
    class IncomingElementsTests {

        @Test
        void should_generate_an_aggregate_error_message() {
            // GIVEN
            final DbRelationship relationship1 = Drivers.relation().id(1).end(22).build();
            final DbRelationship relationship2 = Drivers.relation().id(2).end(42).build();
            final DbRelationship relationship3 = Drivers.relation().id(3).end(69).build();
            final List<DbRelationship> relationships = Randomize.listOf(relationship1, relationship2, relationship3);
            final DbNode node1 = Drivers.node().id(22).build();
            final DbNode node2 = Drivers.node().id(29).build();
            final DbNode node3 = Drivers.node().id(35).build();
            final DbNode node4 = Drivers.node().id(42).build();
            final DbNode node5 = Drivers.node().id(56).build();
            final DbNode node6 = Drivers.node().id(69).build();
            final List<DbNode> actual = Randomize.listOf(node1, node2, node3, node4, node5, node6);

            // WHEN
            final ErrorMessageFactory error = ShouldNodeHaveNoRelatedRelationships
                    .incomingElements(actual, relationships)
                    .notSatisfies(Randomize.listOf(node1, node4, node6));

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\n"
                    + "Expecting nodes:\n"
                    + "  <[\"NODE{id=22}\",\n"
                    + "    \"NODE{id=29}\",\n"
                    + "    \"NODE{id=35}\",\n"
                    + "    \"NODE{id=42}\",\n"
                    + "    \"NODE{id=56}\",\n"
                    + "    \"NODE{id=69}\"]>\n"
                    + "to have no incoming relationships but found:\n"
                    + "  <[\"RELATIONSHIP{id=1}\", \"RELATIONSHIP{id=2}\", \"RELATIONSHIP{id=3}\"]>\n"
                    + "which are incoming relationships to nodes:\n"
                    + "\n"
                    + "  1) NODE{id=22}\n"
                    + "    - Incoming relationships:: [RELATIONSHIP{id=1, type='null', start=null, end=22, "
                    + "properties={}}]\n"
                    + "\n"
                    + "  2) NODE{id=42}\n"
                    + "    - Incoming relationships:: [RELATIONSHIP{id=2, type='null', start=null, end=42, "
                    + "properties={}}]\n"
                    + "\n"
                    + "  3) NODE{id=69}\n"
                    + "    - Incoming relationships:: [RELATIONSHIP{id=3, type='null', start=null, end=69, "
                    + "properties={}}]"
            );
        }
    }

    @Nested
    class OutgoingElementsTests {

        @Test
        void should_generate_an_aggregate_error_message() {
            // GIVEN
            final DbRelationship relationship1 = Drivers.relation().id(1).start(22).build();
            final DbRelationship relationship2 = Drivers.relation().id(2).start(42).build();
            final DbRelationship relationship3 = Drivers.relation().id(3).start(69).build();
            final List<DbRelationship> relationships = Randomize.listOf(relationship1, relationship2, relationship3);
            final DbNode node1 = Drivers.node().id(22).build();
            final DbNode node2 = Drivers.node().id(29).build();
            final DbNode node3 = Drivers.node().id(35).build();
            final DbNode node4 = Drivers.node().id(42).build();
            final DbNode node5 = Drivers.node().id(56).build();
            final DbNode node6 = Drivers.node().id(69).build();
            final List<DbNode> actual = Randomize.listOf(node1, node2, node3, node4, node5, node6);

            // WHEN
            final ErrorMessageFactory error = ShouldNodeHaveNoRelatedRelationships
                    .outgoingElements(actual, relationships)
                    .notSatisfies(Randomize.listOf(node1, node4, node6));

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\n"
                    + "Expecting nodes:\n"
                    + "  <[\"NODE{id=22}\",\n"
                    + "    \"NODE{id=29}\",\n"
                    + "    \"NODE{id=35}\",\n"
                    + "    \"NODE{id=42}\",\n"
                    + "    \"NODE{id=56}\",\n"
                    + "    \"NODE{id=69}\"]>\n"
                    + "to have no outgoing relationships but found:\n"
                    + "  <[\"RELATIONSHIP{id=1}\", \"RELATIONSHIP{id=2}\", \"RELATIONSHIP{id=3}\"]>\n"
                    + "which are outgoing relationships to nodes:\n"
                    + "\n"
                    + "  1) NODE{id=22}\n"
                    + "    - Outgoing relationships:: [RELATIONSHIP{id=1, type='null', start=22, end=null, "
                    + "properties={}}]\n"
                    + "\n"
                    + "  2) NODE{id=42}\n"
                    + "    - Outgoing relationships:: [RELATIONSHIP{id=2, type='null', start=42, end=null, "
                    + "properties={}}]\n"
                    + "\n"
                    + "  3) NODE{id=69}\n"
                    + "    - Outgoing relationships:: [RELATIONSHIP{id=3, type='null', start=69, end=null, "
                    + "properties={}}]"
            );
        }
    }

}