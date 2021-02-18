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

import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.assertj.neo4j.api.beta.type.Models;
import org.assertj.neo4j.api.beta.type.loader.DataLoader;
import org.assertj.neo4j.api.beta.type.loader.LoaderFactory;
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Patrick Allain - 24/11/2020
 */
class AbstractRelationshipsAssertTests {

    private static class BaseRelationshipsTests {

        protected final Relationships dataLoader;

        private final List<DbRelationship> relationships;

        protected DriverRelationshipsAssert assertions;

        protected final Driver driver = Mockito.mock(Driver.class);

        protected BaseRelationshipsTests(final DbRelationship.DbRelationshipBuilder... builders) {
            this.dataLoader = Mockito.mock(Relationships.class);
            this.relationships = IntStream.range(0, builders.length)
                    .mapToObj(idx -> builders[idx].id(idx + 1).build())
                    .collect(Collectors.toList());
        }

        @BeforeEach
        void setUp() {
            testCase(this.relationships);
        }

        protected void testCase(final List<DbRelationship> nodes) {
            when(this.dataLoader.load()).thenReturn(nodes);
            this.assertions = new DriverRelationshipsAssert(dataLoader);
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
    class UsingNoEntityIdComparisonTests extends BaseRelationshipsTests {
        UsingNoEntityIdComparisonTests() {
            super(
                    Models.relation("KNOWS_1").id(22),
                    Models.relation("KNOWS_2").id(56)
            );
        }

        @Test
        void should_return_a_list_of_nodes_without_ids() {
            // WHEN
            final DriverRelationshipsAssert result = assertions.usingNoEntityIdComparison();

            // THEN
            verify(dataLoader).load();
            assertThat(result.getActual())
                    .extracting(DbEntity::getId)
                    .doesNotContainNull();
            Assertions.assertDoesNotThrow(() -> result
                    .contains(Models.relation("KNOWS_1").build())
                    .contains(Models.relation("KNOWS_2").id(29).build())
            );
        }

    }

    @Nested
    @DisplayName("haveType")
    class HaveTypeTests extends BaseRelationshipsTests {

        HaveTypeTests() {
            super(
                    Models.relation("KNOWS"),
                    Models.relation("KNOWS"),
                    Models.relation("KNOWS"),
                    Models.relation("KNOWS")
            );
        }

        @Test
        void should_fail() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveType("OTHER_TYPE"));

            // THEN
            verify(dataLoader).load();
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting relationships:",
                            "to have type:",
                            "but found other types for some relationships:"
                    );
        }

        @Test
        void should_pass() {
            // WHEN
            final DriverRelationshipsAssert result = assertions.haveType("KNOWS");

            // THEN
            verify(dataLoader).load();
            assertThat(result).isSameAs(assertions);
        }

    }

    @Nested
    @DisplayName("haveAnyOfTypes")
    class HaveAnyOfTypesTests extends BaseRelationshipsTests {

        HaveAnyOfTypesTests() {
            super(
                    Models.relation("KNOWS_1"),
                    Models.relation("KNOWS_2"),
                    Models.relation("KNOWS_1"),
                    Models.relation("KNOWS_3")
            );
        }

        @Test
        void should_fail() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveAnyOfTypes("KNOWS_1", "KNOWS_2"));

            // THEN
            verify(dataLoader).load();
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting relationships:",
                            "to have type in:",
                            "but found other types for some relationships:"
                    );
        }

        @Test
        void should_pass() {
            // WHEN
            final DriverRelationshipsAssert result = assertions.haveAnyOfTypes("KNOWS_1", "KNOWS_2", "KNOWS_3");

            // THEN
            verify(dataLoader).load();
            assertThat(result).isSameAs(assertions);
        }

    }

}
