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

import org.assertj.core.api.Assertions;
import org.assertj.neo4j.api.beta.type.DataLoader;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.assertj.neo4j.api.beta.util.Predicates;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * @author patouche - 12/11/2020
 */
class AbstractEntitiesAssertTests {

    private static class ConcreteEntitiesAssert
            extends AbstractEntitiesAssert<ConcreteEntitiesAssert, Nodes.DbNode, ConcreteEntitiesAssert,
            ConcreteEntitiesAssert> {

        protected ConcreteEntitiesAssert(List<Nodes.DbNode> entities) {
            this(entities, null, false, null);
        }

        protected ConcreteEntitiesAssert(List<Nodes.DbNode> entities, DataLoader<Nodes.DbNode> loader,
                                         boolean ignoringIds, ConcreteEntitiesAssert parent) {
            super(
                    RecordType.NODE,
                    ConcreteEntitiesAssert.class,
                    loader,
                    entities,
                    ignoringIds,
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

        protected ConcreteEntitiesAssert assertions;

        protected BaseTests(Nodes.DbNodeBuilder... builders) {
            this.assertions = new ConcreteEntitiesAssert(
                    IntStream.range(0, builders.length)
                            .mapToObj(idx -> builders[idx].id(idx + 1).build())
                            .collect(Collectors.toList())
            );
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
    @DisplayName("haveListPropertyOfType")
    class HaveListPropertyOfTypeTest extends BaseTests {

        HaveListPropertyOfTypeTest() {
            super(
                    Drivers.node()
                            .property("prop", Arrays.asList(1, 2))
                            .property("mixed", 1),
                    Drivers.node().property("prop", Arrays.asList(1, 2, 3))
                            .property("mixed", "val"),
                    Drivers.node().property("prop", Arrays.asList(1, 2, 3, 4))
                            .property("mixed", Arrays.asList(1, 2))
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
                            "to have all the following property keys:",
                            "but some property keys were missing on:"
                    );
        }

        @Test
        void should_fail_when_property_is_not_all_list_value() {
            // WHEN
            final Throwable throwable = catchThrowable(
                    () -> assertions.haveListPropertyOfType("mixed", ValueType.STRING)
            );

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "to have property \"mixed\" with type:",
                            "but some nodes have for the property \"mixed\" another type:"
                    );
        }

        @Test
        void should_pass() {
            // WHEN
            final ConcreteEntitiesAssert result = assertions.haveListPropertyOfType("prop", ValueType.STRING);

            // FIXME: BAD ERROR MESSAGE

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
                            .property("inc", "val-1"),
                    Drivers.node()
                            .property("prop", "val")
                            .property("inc", "val-1"),
                    Drivers.node()
                            .property("prop", "val")
                            .property("inc", "val-1")
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
                            "to have all the following property keys:",
                            "but some property keys were missing on:"
                    );
        }

        @Test
        void should_fail_when_property_dont_have_the_expected_value() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveProperty("inc", "val-0"));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The property keys to look for should not be null or empty");
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
                    .hasMessageContaining("TODO");
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
                            "to have all the following property keys:",
                            "but some property keys were missing on:"
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
                            "to have all the following property keys:",
                            "but some property keys were missing on:"
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
                    .hasMessageContainingAll("Expecting nodes:", "but some property keys were missing on:");
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
    @DisplayName("havePropertyValueMatching")
    class HavePropertyValueMatchingTests extends BaseTests {

        HavePropertyValueMatchingTests() {
            super(Drivers.node().property("prop", "val-1"), Drivers.node().property("prop", "val-2"),
                    Drivers.node().property("prop", "val-3"));
        }

        @Test
        void should_fail_when_property_is_missing() {
            // WHEN
            final Throwable throwable = catchThrowable(
                    () -> assertions.havePropertyValueMatching("prop-mixed", (o) -> true)
            );

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "toto",
                            ""
                    );
        }

        @Test
        void should_fail_when_not_matching() {
            // WHEN
            final Throwable throwable = catchThrowable(
                    () -> assertions.havePropertyValueMatching("prop-mixed", (o) -> false)
            );

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "toto",
                            ""
                    );
        }

        @Test
        void should_pass() {
            // WHEN
            final ConcreteEntitiesAssert result = assertions.havePropertyValueMatching("key", (o) -> true);

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
                            "to have all the following property keys:",
                            "but some property keys were missing on:"
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
                            "to have property \"mixed\" with type:",
                            "but some nodes have for the property \"mixed\" another type:"
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
