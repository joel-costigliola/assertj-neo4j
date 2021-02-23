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
import org.assertj.neo4j.api.beta.type.Entities;
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

import java.util.Arrays;
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

    private static final List<DbNode.DbNodeBuilder> LABELLED_NODE_BUILDERS = Arrays.asList(
            Entities.node().id(1).labels("lbl-1", "lbl-2"),
            Entities.node().id(2).labels("lbl-2", "lbl-3", "other"),
            Entities.node().id(3).labels("lbl-1", "lbl-3", "other"),
            Entities.node().id(4).labels("lbl-2", "lbl-4"),
            Entities.node().id(5).labels("other"),
            Entities.node().id(6)
    );

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
            verify(dataLoader).load();
            verifyNoMoreInteractions(this.dataLoader);
        }
    }

    @Nested
    @DisplayName("usingNoEntityIdComparison")
    class UsingNoEntityIdComparisonTests extends BaseNodesTests {

        UsingNoEntityIdComparisonTests() {
            super(
                    Entities.node().id(22).labels("lbl", "lbl-1"),
                    Entities.node().id(56).labels("lbl", "lbl-2")
            );
        }

        @Test
        void should_return_a_list_of_nodes_without_ids() {
            // WHEN
            final DriverNodesAssert result = assertions.usingNoEntityIdComparison();

            // THEN
            assertThat(result.getActual())
                    .extracting(DbEntity::getId)
                    .doesNotContainNull();
            Assertions.assertDoesNotThrow(() -> result
                    .withFullRepresentation()
                    .contains(Entities.node().labels("lbl", "lbl-1").build())
                    .contains(Entities.node().id(35).labels("lbl", "lbl-2").build())
            );
        }
    }

    @Nested
    @DisplayName("filteredOnLabels")
    class FilteredOnLabelsTests extends BaseNodesTests {

        public FilteredOnLabelsTests() {
            super(LABELLED_NODE_BUILDERS.toArray(new DbNode.DbNodeBuilder[0]));
        }

        @Test
        void should_filter_nodes_having_at_least_one_labels_matching_the_predicate() {
            // WHEN
            final DriverNodesAssert result = this.assertions.filteredOnLabels("lbl-1");

            // THEN
            assertThat(result.getActual())
                    .hasSize(3)
                    .extracting(DbEntity::getId).contains(1L, 3L, 6L);
        }

        @Test
        void should_filter_nodes_having_at_least_one_labels_matching_the_predicate_without_no_labels() {
            // WHEN
            final DriverNodesAssert result = this.assertions.filteredOnLabels(true, "lbl-1");

            // THEN
            assertThat(result.getActual())
                    .hasSize(2)
                    .extracting(DbEntity::getId).contains(1L, 3L);
        }
    }

    @Nested
    @DisplayName("filteredOnNonEmptyLabels")
    class FilteredOnNonEmptyLabelsTests extends BaseNodesTests {

        public FilteredOnNonEmptyLabelsTests() {
            super(LABELLED_NODE_BUILDERS.toArray(new DbNode.DbNodeBuilder[0]));
        }

        @Test
        void should_filter_nodes_having_no_labels() {
            // WHEN
            final DriverNodesAssert result = this.assertions.filteredOnNonEmptyLabels();

            // THEN
            assertThat(result.getActual())
                    .hasSize(5)
                    .extracting(DbEntity::getId).contains(1L, 2L, 3L, 4L, 5L);
        }

    }

    @Nested
    @DisplayName("filteredOnLabelMatchingAny")
    class FilteredOnLabelMatchingAnyTests extends BaseNodesTests {

        public FilteredOnLabelMatchingAnyTests() {
            super(LABELLED_NODE_BUILDERS.toArray(new DbNode.DbNodeBuilder[0]));
        }

        @Test
        void should_filter_nodes_having_at_least_one_labels_matching_the_predicate() {
            // WHEN
            final DriverNodesAssert result = this.assertions.filteredOnLabelMatchingAny(l -> l.startsWith("lbl-"));

            // THEN
            assertThat(result.getActual())
                    .hasSize(5)
                    .extracting(DbEntity::getId).containsExactlyInAnyOrder(1L, 2L, 3L, 4L, 6L);
        }

        @Test
        void should_filter_nodes_having_at_least_one_labels_matching_the_predicate_without_no_labels() {
            // WHEN
            final DriverNodesAssert result = this.assertions
                    .filteredOnLabelMatchingAny(l -> l.startsWith("lbl-"), true);

            // THEN
            assertThat(result.getActual())
                    .hasSize(4)
                    .extracting(DbEntity::getId).containsExactlyInAnyOrder(1L, 2L, 3L, 4L);
        }
    }

    @Nested
    @DisplayName("filteredOnLabelMatchingAll")
    class FilteredOnLabelMatchingAllTests extends BaseNodesTests {

        public FilteredOnLabelMatchingAllTests() {
            super(LABELLED_NODE_BUILDERS.toArray(new DbNode.DbNodeBuilder[0]));
        }

        @Test
        void should_filter_nodes_having_at_least_one_labels_matching_the_predicate() {
            // WHEN
            final DriverNodesAssert result = this.assertions
                    .filteredOnLabelMatchingAll(l -> l.startsWith("lbl-"));

            // THEN
            assertThat(result.getActual())
                    .hasSize(3)
                    .extracting(DbEntity::getId).containsExactlyInAnyOrder(1L, 4L, 6L);
        }

        @Test
        void should_filter_nodes_having_at_least_one_labels_matching_the_predicate_without_no_labels() {
            // WHEN
            final DriverNodesAssert result = this.assertions
                    .filteredOnLabelMatchingAll(l -> l.startsWith("lbl-"), true);

            // THEN
            assertThat(result.getActual())
                    .hasSize(2)
                    .extracting(DbEntity::getId).containsExactlyInAnyOrder(1L, 4L);
        }
    }

    @Nested
    @DisplayName("haveLabels")
    class HaveLabelsTests extends BaseNodesTests {

        HaveLabelsTests() {
            super(
                    Entities.node().labels("all", "missing"),
                    Entities.node().labels("all", "missing"),
                    Entities.node().labels("all", "OTHER-LABEL"),
                    Entities.node().labels("all", "missing"),
                    Entities.node().labels("all", "missing")
            );
        }

        @Test
        void should_fail_when_no_labels_provided() {
            // WHEN
            final Throwable throwable = catchThrowable(assertions::haveLabels);

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The labels to look for should not be null or empty");
        }

        @Test
        void should_fail_when_iterable_is_empty() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveLabels(Collections.emptyList()));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The iterable of values to look for should not be empty");
        }

        @Test
        void should_fail_when_single_value() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveLabels("missing"));

            // THEN
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
            assertThat(result).isSameAs(assertions);
        }

    }

    @Nested
    @DisplayName("incomingRelationships")
    class IncomingRelationshipsTests extends BaseNodesTests {

        IncomingRelationshipsTests() {
            super(
                    Entities.node().id(1).labels("LBL_1"),
                    Entities.node().id(2).labels("LBL_2"),
                    Entities.node().id(3).labels("LBL_3")
            );
        }

        @Test
        void should_pass() {
            // GIVEN
            final DbRelationship relationship1 = Entities.relationship("DISCOVER").id(11).start(1).end(4).build();
            final DbRelationship relationship2 = Entities.relationship("KNOWS").id(12).start(5).end(1).build();
            final DbRelationship relationship3 = Entities.relationship("KNOWS").id(23).start(6).end(3).build();
            final Relationships relationships = Loaders.relationships(relationship1, relationship2, relationship3);
            when(dataLoader.chain(any())).thenReturn(relationships);

            // WHEN
            final ChildrenDriverRelationshipsAssert<DriverNodesAssert, DriverNodesAssert> result =
                    assertions.incomingRelationships("KNOWS", "DISCOVER");

            // THEN
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
                    Entities.node().id(1).labels("LBL_1"),
                    Entities.node().id(2).labels("LBL_2"),
                    Entities.node().id(3).labels("LBL_3")
            );
        }

        @Test
        void should_pass() {
            // GIVEN
            final DbRelationship relationship1 = Entities.relationship("DISCOVER").id(11).start(4).end(1).build();
            final DbRelationship relationship2 = Entities.relationship("KNOWS").id(12).start(1).end(5).build();
            final DbRelationship relationship3 = Entities.relationship("KNOWS").id(23).start(3).end(6).build();
            final Relationships relationships = Loaders.relationships(relationship1, relationship2, relationship3);
            when(dataLoader.chain(any())).thenReturn(relationships);

            // WHEN
            final ChildrenDriverRelationshipsAssert<DriverNodesAssert, DriverNodesAssert> result =
                    assertions.outgoingRelationships("KNOWS", "DISCOVER");

            // THEN
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
                    Entities.node().id(1).labels("LBL_1"),
                    Entities.node().id(2).labels("LBL_2"),
                    Entities.node().id(3).labels("LBL_3")
            );
        }

        @Test
        void should_fail() {
            // GIVEN
            final DbRelationship relationship1 = Entities.relationship("DISCOVER").id(11).start(1).end(4).build();
            final DbRelationship relationship2 = Entities.relationship("KNOWS").id(12).start(5).end(1).build();
            final DbRelationship relationship3 = Entities.relationship("KNOWS").id(13).start(6).end(3).build();
            final Relationships relationships = Loaders.relationships(relationship1, relationship2, relationship3);
            when(dataLoader.chain(any())).thenReturn(relationships);

            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveNoIncomingRelationships());

            // THEN
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
            final DbRelationship relationship1 = Entities.relationship("DISCOVER").id(11).start(1).end(4).build();
            final DbRelationship relationship2 = Entities.relationship("KNOWS").id(12).start(2).end(5).build();
            final DbRelationship relationship3 = Entities.relationship("KNOWS").id(13).start(3).end(6).build();
            final Relationships relationships = Loaders.relationships(relationship1, relationship2, relationship3);
            when(dataLoader.chain(any())).thenReturn(relationships);

            // WHEN

            final DriverNodesAssert result = assertions.haveNoIncomingRelationships("KNOWS", "DISCOVER");

            // THEN
            verify(dataLoader).chain(argQuery("MATCH ()-[r :DISCOVER|KNOWS]->() RETURN r"));

            assertThat(result).isSameAs(assertions);
        }

    }

    @Nested
    @DisplayName("haveNoOutgoingRelationships")
    class HaveNoOutgoingRelationshipsTests extends BaseNodesTests {

        HaveNoOutgoingRelationshipsTests() {
            super(
                    Entities.node().id(1).labels("LBL_1"),
                    Entities.node().id(2).labels("LBL_2"),
                    Entities.node().id(3).labels("LBL_3")
            );
        }

        @Test
        void should_fail() {
            // GIVEN
            final DbRelationship relationship1 = Entities.relationship("DISCOVER").id(11).start(1).end(4).build();
            final DbRelationship relationship2 = Entities.relationship("DISCOVER").id(12).start(2).end(5).build();
            final DbRelationship relationship3 = Entities.relationship("KNOWS").id(13).start(6).end(3).build();
            final Relationships relationships = Loaders.relationships(relationship1, relationship2, relationship3);
            when(dataLoader.chain(any())).thenReturn(relationships);

            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveNoOutgoingRelationships());

            // THEN
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
            final DbRelationship relationship1 = Entities.relationship("DISCOVER").id(11).start(4).end(1).build();
            final DbRelationship relationship2 = Entities.relationship("KNOWS").id(12).start(5).end(2).build();
            final DbRelationship relationship3 = Entities.relationship("KNOWS").id(13).start(6).end(3).build();
            final Relationships relationships = Loaders.relationships(relationship1, relationship2, relationship3);
            when(dataLoader.chain(any())).thenReturn(relationships);

            // WHEN

            final DriverNodesAssert result = assertions.haveNoOutgoingRelationships("KNOWS", "DISCOVER");

            // THEN
            verify(dataLoader).chain(argQuery("MATCH ()-[r :DISCOVER|KNOWS]->() RETURN r"));

            assertThat(result).isSameAs(assertions);
        }

    }
}

