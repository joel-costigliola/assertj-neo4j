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
package org.assertj.neo4j.api.beta;

import org.assertj.neo4j.api.beta.testing.Loaders;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.DbNode;
import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.assertj.neo4j.api.beta.type.Models;
import org.assertj.neo4j.api.beta.type.loader.DataLoader;
import org.assertj.neo4j.api.beta.type.loader.LoaderFactory;
import org.assertj.neo4j.api.beta.type.loader.Nodes;
import org.assertj.neo4j.api.beta.type.loader.Relationships;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Query;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Patrick Allain - 10/02/2021
 */
class AbstractNodesAssertTests {

    private static class BaseNodesTests {

        protected final Nodes dataLoader;

        private final List<DbNode> nodes;

        protected DriverNodesAssert assertions;

        protected final Driver driver = Mockito.mock(Driver.class);

        protected BaseNodesTests(final DbNode.DbNodeBuilder... builders) {
            this.dataLoader = Mockito.mock(Nodes.class);
            this.nodes = IntStream.range(0, builders.length)
                    .mapToObj(idx -> entity(idx, builders[idx]))
                    .collect(Collectors.toList());
        }

        private DbNode entity(final long idx, final DbNode.DbNodeBuilder builder) {
            final Long id = Optional.ofNullable(builder.build().getId()).orElseGet(() -> idx + 1);
            return builder.id(id).build();
        }

        @BeforeEach
        void setUp() {
            testCase(this.nodes);
        }

        protected void testCase(final List<DbNode> nodes) {
            when(this.dataLoader.load()).thenReturn(nodes);
            this.assertions = new DriverNodesAssert(dataLoader);
        }

        protected LoaderFactory<Object, DataLoader<Object>> argQuery(final String query) {
            return argThat(d -> Objects.equals(d.create(this.driver).query(), new Query(query)));
        }

        @AfterEach
        void tearDown() {
            verifyNoMoreInteractions(this.dataLoader);
        }
    }

    @Nested
    @DisplayName("usingNoEntityIdComparison")
    class UsingNoEntityIdComparisonTests extends BaseNodesTests {

        UsingNoEntityIdComparisonTests() {
            super(
                    Models.node().id(22).labels("lbl", "lbl-1"),
                    Models.node().id(56).labels("lbl", "lbl-2")
            );
        }

        @Test
        void should_return_a_list_of_nodes_without_ids() {
            // WHEN
            final DriverNodesAssert result = assertions.usingNoEntityIdComparison();

            // THEN
            verify(dataLoader).load();
            assertThat(result.getActual())
                    .extracting(DbEntity::getId)
                    .doesNotContainNull();
            Assertions.assertDoesNotThrow(() -> result
                    .withFullRepresentation()
                    .contains(Models.node().labels("lbl", "lbl-1").build())
                    .contains(Models.node().id(35).labels("lbl", "lbl-2").build())
            );
        }
    }

    @Nested
    @DisplayName("haveLabels")
    class HaveLabelsTests extends BaseNodesTests {

        HaveLabelsTests() {
            super(
                    Models.node().labels("all", "missing"),
                    Models.node().labels("all", "missing"),
                    Models.node().labels("all", "OTHER-LABEL"),
                    Models.node().labels("all", "missing"),
                    Models.node().labels("all", "missing")
            );
        }

        @Test
        void should_fail_when_no_labels_provided() {
            // WHEN
            final Throwable throwable = catchThrowable(assertions::haveLabels);

            // THEN
            verify(dataLoader).load();
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The labels to look for should not be null or empty");
        }

        @Test
        void should_fail_when_iterable_is_empty() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveLabels(Collections.emptyList()));

            // THEN
            verify(dataLoader).load();
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The iterable of values to look for should not be empty");
        }

        @Test
        void should_fail_when_single_value() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveLabels("missing"));

            // THEN
            verify(dataLoader).load();
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have the labels:",
                            "but some labels are missing for nodes:"
                    );
        }

        @Test
        void should_pass_when_single_value() {
            // WHEN
            final DriverNodesAssert result = assertions.haveLabels("all");

            // THEN
            verify(dataLoader).load();
            assertThat(result).isSameAs(assertions);
        }

    }

    @Nested
    @DisplayName("incomingRelationships")
    class IncomingRelationshipsTests extends BaseNodesTests {

        IncomingRelationshipsTests() {
            super(
                    Models.node().id(1).labels("LBL_1"),
                    Models.node().id(2).labels("LBL_2"),
                    Models.node().id(3).labels("LBL_3")
            );
        }

        @Test
        void should_pass() {
            // GIVEN
            final DbRelationship relationship1 = Models.relation("DISCOVER").id(11).start(1).end(4).build();
            final DbRelationship relationship2 = Models.relation("KNOWS").id(12).start(5).end(1).build();
            final DbRelationship relationship3 = Models.relation("KNOWS").id(23).start(6).end(3).build();
            final Relationships relationships = Loaders.relationships(relationship1, relationship2, relationship3);
            when(dataLoader.chain(any())).thenReturn(relationships);

            // WHEN
            final ChildrenDriverRelationshipsAssert<DriverNodesAssert, DriverNodesAssert> result =
                    assertions.incomingRelationships("KNOWS", "DISCOVER");

            // THEN
            verify(dataLoader).load();
            verify(dataLoader).chain(argQuery("MATCH ()-[r :DISCOVER|KNOWS]->() RETURN r"));

            assertThat(result.getActual())
                    .hasSize(2)
                    .contains(relationship2, relationship3);
        }

    }

    @Nested
    @DisplayName("outgoingRelationships")
    class OutgoingRelationshipsTests extends BaseNodesTests {

        OutgoingRelationshipsTests() {
            super(
                    Models.node().id(1).labels("LBL_1"),
                    Models.node().id(2).labels("LBL_2"),
                    Models.node().id(3).labels("LBL_3")
            );
        }

        @Test
        void should_pass() {
            // GIVEN
            final DbRelationship relationship1 = Models.relation("DISCOVER").id(11).start(4).end(1).build();
            final DbRelationship relationship2 = Models.relation("KNOWS").id(12).start(1).end(5).build();
            final DbRelationship relationship3 = Models.relation("KNOWS").id(23).start(3).end(6).build();
            final Relationships relationships = Loaders.relationships(relationship1, relationship2, relationship3);
            when(dataLoader.chain(any())).thenReturn(relationships);

            // WHEN
            final ChildrenDriverRelationshipsAssert<DriverNodesAssert, DriverNodesAssert> result =
                    assertions.outgoingRelationships("KNOWS", "DISCOVER");

            // THEN
            verify(dataLoader).load();
            verify(dataLoader).chain(argQuery("MATCH ()-[r :DISCOVER|KNOWS]->() RETURN r"));

            assertThat(result.getActual())
                    .hasSize(2)
                    .contains(relationship2, relationship3);
        }
    }

    @Nested
    @DisplayName("haveNoIncomingRelationships")
    class HaveNoIncomingRelationshipsTests extends BaseNodesTests {

        HaveNoIncomingRelationshipsTests() {
            super(
                    Models.node().id(1).labels("LBL_1"),
                    Models.node().id(2).labels("LBL_2"),
                    Models.node().id(3).labels("LBL_3")
            );
        }

        @Test
        void should_fail() {
            // GIVEN
            final DbRelationship relationship1 = Models.relation("DISCOVER").id(11).start(1).end(4).build();
            final DbRelationship relationship2 = Models.relation("KNOWS").id(12).start(5).end(1).build();
            final DbRelationship relationship3 = Models.relation("KNOWS").id(13).start(6).end(3).build();
            final Relationships relationships = Loaders.relationships(relationship1, relationship2, relationship3);
            when(dataLoader.chain(any())).thenReturn(relationships);

            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveNoIncomingRelationships());

            // THEN
            verify(dataLoader).load();
            verify(dataLoader).chain(argQuery("MATCH ()-[r]->() RETURN r"));

            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have no incoming relationships but found:",
                            "which are incoming relationships to nodes:"
                    );
        }

        @Test
        void should_pass() {
            // GIVEN
            final DbRelationship relationship1 = Models.relation("DISCOVER").id(11).start(1).end(4).build();
            final DbRelationship relationship2 = Models.relation("KNOWS").id(12).start(2).end(5).build();
            final DbRelationship relationship3 = Models.relation("KNOWS").id(13).start(3).end(6).build();
            final Relationships relationships = Loaders.relationships(relationship1, relationship2, relationship3);
            when(dataLoader.chain(any())).thenReturn(relationships);

            // WHEN

            final DriverNodesAssert result = assertions.haveNoIncomingRelationships("KNOWS", "DISCOVER");

            // THEN
            verify(dataLoader).load();
            verify(dataLoader).chain(argQuery("MATCH ()-[r :DISCOVER|KNOWS]->() RETURN r"));

            assertThat(result).isSameAs(assertions);
        }

    }

    @Nested
    @DisplayName("haveNoOutgoingRelationships")
    class HaveNoOutgoingRelationshipsTests extends BaseNodesTests {

        HaveNoOutgoingRelationshipsTests() {
            super(
                    Models.node().id(1).labels("LBL_1"),
                    Models.node().id(2).labels("LBL_2"),
                    Models.node().id(3).labels("LBL_3")
            );
        }

        @Test
        void should_fail() {
            // GIVEN
            final DbRelationship relationship1 = Models.relation("DISCOVER").id(11).start(1).end(4).build();
            final DbRelationship relationship2 = Models.relation("DISCOVER").id(12).start(2).end(5).build();
            final DbRelationship relationship3 = Models.relation("KNOWS").id(13).start(6).end(3).build();
            final Relationships relationships = Loaders.relationships(relationship1, relationship2, relationship3);
            when(dataLoader.chain(any())).thenReturn(relationships);

            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveNoOutgoingRelationships());

            // THEN
            verify(dataLoader).load();
            verify(dataLoader).chain(argQuery("MATCH ()-[r]->() RETURN r"));

            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have no outgoing relationships but found:",
                            "which are outgoing relationships to nodes:"
                    );
        }

        @Test
        void should_pass() {
            // GIVEN
            final DbRelationship relationship1 = Models.relation("DISCOVER").id(11).start(4).end(1).build();
            final DbRelationship relationship2 = Models.relation("KNOWS").id(12).start(5).end(2).build();
            final DbRelationship relationship3 = Models.relation("KNOWS").id(13).start(6).end(3).build();
            final Relationships relationships = Loaders.relationships(relationship1, relationship2, relationship3);
            when(dataLoader.chain(any())).thenReturn(relationships);

            // WHEN

            final DriverNodesAssert result = assertions.haveNoOutgoingRelationships("KNOWS", "DISCOVER");

            // THEN
            verify(dataLoader).load();
            verify(dataLoader).chain(argQuery("MATCH ()-[r :DISCOVER|KNOWS]->() RETURN r"));

            assertThat(result).isSameAs(assertions);
        }

    }
}

