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

import org.assertj.neo4j.api.beta.type.DataLoader;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.assertj.neo4j.api.beta.util.Predicates;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.neo4j.driver.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Patrick Allain - 12/11/2020
 */
class AbstractEntitiesAssertTests {

    //@formatter:off
    private static class ConcreteEntitiesAssert extends AbstractEntitiesAssert<ConcreteEntitiesAssert, Nodes.DbNode, ConcreteEntitiesAssert,
                                                                               ConcreteEntitiesAssert,
                                                                               ConcreteEntitiesAssert> {
    //@formatter:on

        protected ConcreteEntitiesAssert(List<Nodes.DbNode> entities, DataLoader<Nodes.DbNode> loader) {
            this(entities, loader, null);
        }

        protected ConcreteEntitiesAssert(List<Nodes.DbNode> entities, DataLoader<Nodes.DbNode> loader,
                                         ConcreteEntitiesAssert parent) {
            super(
                    RecordType.NODE,
                    ConcreteEntitiesAssert.class,
                    loader,
                    entities,
                    ConcreteEntitiesAssert::new,
                    parent,
                    Navigable.rootAssert(parent)
            );
        }

        @Override
        public ConcreteEntitiesAssert toRootAssert() {
            return rootAssert().orElse(this);
        }
    }

    private static class BaseTests {

        protected final Nodes dataLoader;

        protected ConcreteEntitiesAssert assertions;

        protected BaseTests(Nodes.DbNodeBuilder... builders) {
            this.dataLoader = Mockito.mock(Nodes.class);
            testCase(builders);
        }

        protected void testCase(Nodes.DbNodeBuilder... builders) {
            this.assertions = new ConcreteEntitiesAssert(
                    IntStream.range(0, builders.length)
                            .mapToObj(idx -> builders[idx].id(idx + 1).build())
                            .collect(Collectors.toList()),
                    dataLoader
            );
        }

        @AfterEach
        void tearDown() {
            Mockito.verifyNoMoreInteractions(dataLoader);
        }
    }

    @Nested
    @DisplayName("filteredOn")
    class FilteredOnTests extends BaseTests {

        FilteredOnTests() {
            super(
                    Drivers.node(),
                    Drivers.node().property("prop", "v-2"),
                    Drivers.node().property("prop", "v-3")
            );
        }

        @Test
        void should_filter_entities_and_create_a_new_assertions() {
            // WHEN
            ConcreteEntitiesAssert result = assertions
                    .filteredOn(n -> Objects.equals(n.getPropertyValue("prop"), "v-2"));

            // THEN
            assertThat(result).isNotSameAs(assertions);
            assertThat(result.getActual())
                    .hasSize(1)
                    .extracting(DbEntity::getId)
                    .contains(2L);
        }

        @Test
        void should_be_navigable() {
            // WHEN
            ConcreteEntitiesAssert result = assertions
                    .filteredOn(n -> Objects.equals(n.getPropertyValue("prop"), "v-2"));

            // THEN
            assertThat(result.toParentAssert()).as("parent").isSameAs(assertions);
            assertThat(result.toRootAssert()).as("root").isSameAs(assertions);
        }

    }

    @Nested
    @DisplayName("filteredOnPropertyExists")
    class FilteredOnPropertyExistsTests extends BaseTests {

        FilteredOnPropertyExistsTests() {
            super(Drivers.node(), Drivers.node().property("prop", "val-2"), Drivers.node().property("prop", "val-3"));
        }

        @Test
        void should_filter_entities_that_contains_the_property() {
            // WHEN
            ConcreteEntitiesAssert result = assertions.filteredOnPropertyExists("prop");

            // THEN
            assertThat(result).isNotSameAs(assertions);
            assertThat(result.getActual())
                    .hasSize(2)
                    .extracting(DbEntity::getId)
                    .contains(2L, 3L);
        }

        @Test
        void should_be_navigable() {
            // WHEN
            ConcreteEntitiesAssert result = assertions.filteredOnPropertyExists("prop");

            // THEN
            assertThat(result.toParentAssert()).as("parent").isSameAs(assertions);
            assertThat(result.toRootAssert()).as("root").isSameAs(assertions);
        }

    }

    @Nested
    @DisplayName("filteredOnPropertyExists")
    class FilteredOnPropertyValueTests extends BaseTests {

        FilteredOnPropertyValueTests() {
            super(
                    Drivers.node(),
                    Drivers.node().property("prop", "other-val-1"),
                    Drivers.node().property("prop", "val"),
                    Drivers.node().property("prop", "other-val-2")
            );
        }

        @Test
        void should_filter_entities_having_the_property() {
            // WHEN
            ConcreteEntitiesAssert result = assertions.filteredOnPropertyValue("prop", "val");

            // THEN
            assertThat(result).isNotSameAs(assertions);
            assertThat(result.getActual())
                    .hasSize(1)
                    .extracting(DbEntity::getId)
                    .contains(3L);
        }

        @Test
        void should_be_navigable() {
            // WHEN
            ConcreteEntitiesAssert result = assertions.filteredOnPropertyExists("prop");

            // THEN
            assertThat(result.toParentAssert()).as("parent").isSameAs(assertions);
            assertThat(result.toRootAssert()).as("root").isSameAs(assertions);
        }

    }

    @Nested
    @DisplayName("isEmpty")
    class IsEmptyTests extends BaseTests {

        @Test
        void should_failed() {
            // GIVEN
            testCase(Drivers.node().id(1));
            when(dataLoader.query()).thenReturn(new Query("MATCH (n) RETURN n"));

            // WHEN
            final Throwable result = catchThrowable(() -> assertions.isEmpty());

            // THEN
            verify(dataLoader).query();
            assertThat(result)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting query:",
                            "to return an empty list of nodes got:"
                    );
        }

        @Test
        void should_pass() {
            // WHEN
            final ConcreteEntitiesAssert result = assertions.isEmpty();

            // THEN
            assertThat(result).isSameAs(assertions);
        }
    }

    @Nested
    @DisplayName("isNotEmpty")
    class IsNotEmptyTests extends BaseTests {

        @Test
        void should_fail() {
            // WHEN
            final Throwable result = catchThrowable(() -> assertions.isNotEmpty());

            // THEN
            verify(dataLoader).query();
            assertThat(result)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting query:",
                            "to return a non empty result list of nodes but got no result."
                    );
        }

        @Test
        void should_pass() {
            // GIVEN
            testCase(Drivers.node().id(1));
            when(dataLoader.query()).thenReturn(new Query("MATCH (n) RETURN n"));

            // WHEN
            final ConcreteEntitiesAssert result = assertions.isNotEmpty();

            // THEN
            assertThat(result).isSameAs(assertions);
        }
    }

    @Nested
    @DisplayName("haveListPropertyOfType")
    class HaveListPropertyOfTypeTest extends BaseTests {

        HaveListPropertyOfTypeTest() {
            super(
                    Drivers.node()
                            .property("prop", Arrays.asList(1, 2))
                            .property("mixed", 1)
                            .property("mixed-list", Arrays.asList(1.1, 1.2)),
                    Drivers.node()
                            .property("prop", Arrays.asList(1, 2, 3))
                            .property("mixed", "val")
                            .property("mixed-list", Arrays.asList(true, false, true)),
                    Drivers.node()
                            .property("prop", Arrays.asList(1, 2, 3, 4))
                            .property("mixed", Arrays.asList(1, 2))
                            .property("mixed-list", Arrays.asList(1, 2, 3, 4))
                            .property("missing", Arrays.asList(1, 2))
            );
        }

        @Test
        void should_fail_when_property_is_missing() {
            // WHEN
            final Throwable throwable = catchThrowable(
                    () -> assertions.haveListPropertyOfType("missing", ValueType.STRING)
            );

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have properties with keys:",
                            "but some nodes don't have this properties:"
                    );
        }

        @Test
        void should_fail_when_property_are_not_all_list_value() {
            // WHEN
            final Throwable throwable = catchThrowable(
                    () -> assertions.haveListPropertyOfType("mixed", ValueType.STRING)
            );

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have a property \"mixed\" with a value type:",
                            "but some nodes have a different property value type:"
                    );
        }

        @Test
        void should_fail_when_property_are_not_all_of_expected_type() {
            // WHEN
            final Throwable throwable = catchThrowable(
                    () -> assertions.haveListPropertyOfType("mixed-list", ValueType.STRING)
            );

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have a composite property list named \"mixed-list\" containing only type:",
                            "but some nodes have a composite list containing others type:"
                    );
        }

        @Test
        void should_pass() {
            // WHEN
            final ConcreteEntitiesAssert result = assertions.haveListPropertyOfType("prop", ValueType.INTEGER);

            // THEN
            assertThat(result).isSameAs(assertions);
        }

    }

    @Nested
    @DisplayName("haveProperty")
    class HavePropertyTests extends BaseTests {

        HavePropertyTests() {
            super(
                    Drivers.node()
                            .property("prop", "val")
                            .property("prop-1", "val-1"),
                    Drivers.node()
                            .property("prop", "val")
                            .property("prop-1", "val-1"),
                    Drivers.node()
                            .property("prop", "val")
                            .property("prop-1", "val-1")
                            .property("missing", "val")
            );
        }

        @Test
        void should_fail_when_property_is_missing() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveProperty("missing", "val"));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have properties with keys:",
                            "but some nodes don't have this properties:"
                    );
        }

        @Test
        void should_fail_when_property_dont_have_the_expected_value() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveProperty("prop-1", "val-0"));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have a property named \"prop-1\" with value:",
                            "but some nodes have a different value for this property:"
                    );
        }

        @Test
        void should_pass() {
            // WHEN
            final ConcreteEntitiesAssert result = assertions.haveProperty("prop", "val");

            // THEN
            assertThat(result).isSameAs(assertions);
        }

    }

    @Nested
    @DisplayName("havePropertySize")
    class HavePropertySizeTests extends BaseTests {

        HavePropertySizeTests() {
            super(
                    Drivers.node().property("p-1", "v-1.1").property("p-2", "v-2.1"),
                    Drivers.node().property("p-1", "v-1.1").property("p-2", "v-2.1"),
                    Drivers.node().property("p-1", "v-1.1").property("p-2", "v-2.1").property("p-3", "v-3.1")
            );
        }

        @Test
        void should_fail() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.havePropertySize(2));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have a property size:",
                            "but some nodes have another property size:"
                    );
        }

        @Test
        void should_pass() {
            // GIVEN
            final ConcreteEntitiesAssert newAssertions = assertions.filteredOn(Predicates.propertySize(2));

            // WHEN
            final ConcreteEntitiesAssert result = newAssertions.havePropertySize(2);

            // THEN
            assertThat(result).isSameAs(newAssertions);
        }

    }

    @Nested
    @DisplayName("havePropertyInstanceOf")
    class HavePropertyInstanceOfTests extends BaseTests {

        HavePropertyInstanceOfTests() {
            super(
                    Drivers.node()
                            .property("prop", LocalDateTime.now().plusDays(1))
                            .property("mixed", "val"),
                    Drivers.node()
                            .property("prop", LocalDateTime.now().plusDays(2))
                            .property("mixed", 1.5),
                    Drivers.node()
                            .property("prop", LocalDateTime.now().plusDays(3))
                            .property("mixed", LocalDate.now())
                            .property("missing", "val")
            );
        }

        @Test
        void should_fail_when_property_is_missing() {
            // WHEN
            final Throwable throwable = catchThrowable(
                    () -> assertions.havePropertyInstanceOf("missing", LocalDateTime.class)
            );

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have properties with keys:",
                            "but some nodes don't have this properties:"
                    );
        }

        @Test
        void should_fail_when_property_dont_have_the_right_type() {
            // WHEN
            final Throwable throwable = catchThrowable(
                    () -> assertions.havePropertyInstanceOf("mixed", LocalDateTime.class)
            );

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have property value \"mixed\" instance of:",
                            "but some nodes have a property value which is not an instance of the expected class:"
                    );
        }

        @Test
        void should_pass() {
            // WHEN
            final ConcreteEntitiesAssert result = assertions.havePropertyInstanceOf("prop", LocalDateTime.class);

            // THEN
            assertThat(result).isSameAs(assertions);
        }
    }

    @Nested
    @DisplayName("havePropertyKeys")
    class HavePropertyKeysTests extends BaseTests {

        HavePropertyKeysTests() {
            super(Drivers.node().property("prop", "val-1"), Drivers.node().property("prop", "val-2"),
                    Drivers.node().property("prop", "val-3"));
        }

        @Test
        void should_fail_when_no_keys_provided() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.havePropertyKeys());

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The property keys should not be null or empty");
        }

        @Test
        void should_fail_when_nodes_have_missing_values() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.havePropertyKeys("other-prop"));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have properties with keys:",
                            "but some nodes don't have this properties:"
                    );
        }

        @Test
        void should_pass() {
            // WHEN
            final ConcreteEntitiesAssert result = assertions.havePropertyKeys("prop");

            // THEN
            assertThat(result).isSameAs(assertions);
        }

    }

    @Nested
    @DisplayName("havePropertyValueMatching(String, Predicate)")
    class HavePropertyValueMatchingTests extends BaseTests {

        HavePropertyValueMatchingTests() {
            super(
                    Drivers.node().property("prop", "val").property("prop-inc", "val-1"),
                    Drivers.node().property("prop", "val").property("prop-inc", "val-2"),
                    Drivers.node().property("prop", "val").property("prop-inc", "val-3").property("missing", true)
            );
        }

        @Test
        void should_fail_when_property_is_missing() {
            // WHEN
            final Throwable throwable = catchThrowable(
                    () -> assertions.havePropertyValueMatching("missing", (o) -> true)
            );

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have properties with keys:",
                            "but some nodes don't have this properties:"
                    );
        }

        @Test
        void should_fail_when_not_matching() {
            // WHEN
            final Throwable throwable = catchThrowable(
                    () -> assertions.havePropertyValueMatching("prop-inc", (o) -> Objects.equals("val", o))
            );

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have a for the property \"prop-inc\" matching the condition but nodes:",
                            "did not:"
                    );
        }

        @Test
        void should_pass() {
            // WHEN
            final ConcreteEntitiesAssert result = assertions
                    .havePropertyValueMatching("prop", (o) -> Objects.equals("val", o));

            // THEN
            assertThat(result).isSameAs(assertions);
        }
    }

    @Nested
    @DisplayName("havePropertyValueMatching(String, Class, Predicate)")
    class HavePropertyValueMatchingTypedTests extends BaseTests {

        HavePropertyValueMatchingTypedTests() {
            super(
                    Drivers.node().property("prop", 1).property("prop-mixed", true),
                    Drivers.node().property("prop", 2).property("prop-mixed", 3.14),
                    Drivers.node().property("prop", 3).property("prop-mixed", "val").property("missing", true)
            );
        }

        @Test
        void should_fail_when_property_is_missing() {
            // WHEN
            final Throwable throwable = catchThrowable(
                    () -> assertions.havePropertyValueMatching("missing", Boolean.class, (o) -> true)
            );

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have properties with keys:",
                            "but some nodes don't have this properties:"
                    );
        }

        @Test
        void should_fail_when_property_value_dont_have_the_correct_type() {
            // WHEN
            final Throwable throwable = catchThrowable(
                    () -> assertions.havePropertyValueMatching("prop-mixed", Boolean.class, (o) -> true)
            );

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have property value \"prop-mixed\" instance of:",
                            "but some nodes have a property value which is not an instance of the expected class:"
                    );
        }

        @Test
        void should_fail_when_not_matching() {
            // WHEN
            final Throwable throwable = catchThrowable(
                    () -> assertions.havePropertyValueMatching("prop", Long.class, (i) -> i < 0)
            );

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have a for the property \"prop\" matching the condition but nodes:",
                            "did not:"
                    );
        }

        @Test
        void should_pass() {
            // WHEN
            final ConcreteEntitiesAssert result = assertions
                    .havePropertyValueMatching("prop", Long.class, (i) -> i < 10);

            // THEN
            assertThat(result).isSameAs(assertions);
        }
    }

    @Nested
    @DisplayName("havePropertyOfType")
    class HavePropertyOfTypeTests extends BaseTests {

        HavePropertyOfTypeTests() {
            super(
                    Drivers.node()
                            .property("prop", "val-1")
                            .property("mixed", "val"),
                    Drivers.node()
                            .property("prop", "val-2")
                            .property("mixed", 1.5),
                    Drivers.node()
                            .property("prop", "val-3")
                            .property("mixed", LocalDate.now())
                            .property("missing", "val")
            );
        }

        @Test
        void should_fail_when_property_is_missing() {
            // WHEN
            final Throwable throwable = catchThrowable(
                    () -> assertions.havePropertyOfType("missing", ValueType.STRING)
            );

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have properties with keys:",
                            "but some nodes don't have this properties:"
                    );
        }

        @Test
        void should_fail_when_property_dont_have_the_right_type() {
            // WHEN
            final Throwable throwable = catchThrowable(
                    () -> assertions.havePropertyOfType("mixed", ValueType.STRING)
            );

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting nodes:",
                            "to have a property \"mixed\" with a value type:",
                            "but some nodes have a different property value type:"
                    );
        }

        @Test
        void should_pass() {
            // WHEN
            final ConcreteEntitiesAssert result = assertions.havePropertyOfType("prop", ValueType.STRING);

            // THEN
            assertThat(result).isSameAs(assertions);
        }

    }

}
