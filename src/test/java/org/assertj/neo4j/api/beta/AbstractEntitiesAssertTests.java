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
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * @author patouche - 12/11/2020
 */
public class AbstractEntitiesAssertTests {

    private final List<Nodes.DbNode> SAMPLE_NODES = Arrays.asList(
            Drivers.node().id(1).property("prop", "val-1").build(),
            Drivers.node().id(2).property("prop", "val-2").property("other-prop", 1).build(),
            Drivers.node().id(3).property("prop", "val-3").property("prop-1", LocalDateTime.now()).build(),
            Drivers.node().id(4).property("prop", "val-4").property("other-prop", 1).property("prop-1", LocalDateTime.now()).build(),
            Drivers.node().id(5).property("prop", "val-5").property("prop-1", "v-5").build()
    );

    static class FakeEntitiesAssert extends AbstractEntitiesAssert<FakeEntitiesAssert, Nodes, Nodes.DbNode> {

        protected FakeEntitiesAssert(List<Nodes.DbNode> entities) {
            this(entities, null);
        }

        protected FakeEntitiesAssert(List<Nodes.DbNode> entities, FakeEntitiesAssert parent) {
            super(RecordType.NODE, null, entities, FakeEntitiesAssert.class, FakeEntitiesAssert::new, parent);
        }

    }

    @Nested
    @DisplayName("havePropertyKeys")
    class HavePropertyKeysTests {

        @Test
        void should_fail_when_no_labels_provided() {
            // GIVEN
            final FakeEntitiesAssert fakeAssert = new FakeEntitiesAssert(SAMPLE_NODES);

            // WHEN
            final Throwable throwable = catchThrowable(fakeAssert::havePropertyKeys);

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The property keys to look for should not be null or empty");
        }

        @Test
        void should_fail_when_iterable_is_empty() {
            // GIVEN
            final FakeEntitiesAssert fakeAssert = new FakeEntitiesAssert(SAMPLE_NODES);

            // WHEN
            final Throwable throwable = catchThrowable(() -> fakeAssert.havePropertyKeys(Collections.emptyList()));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The iterable of property keys to look for should not be empty");
        }

        @Test
        void should_fail_when_nodes_have_missing_values() {
            // GIVEN
            final FakeEntitiesAssert fakeAssert = new FakeEntitiesAssert(SAMPLE_NODES);

            // WHEN
            final Throwable throwable = catchThrowable(() -> fakeAssert.havePropertyKeys("other-prop"));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll("Expecting nodes:", "but some property keys were missing on:");
        }

        @Test
        void should_pass() {
            // GIVEN
            final FakeEntitiesAssert fakeAssert = new FakeEntitiesAssert(SAMPLE_NODES);

            // WHEN
            final FakeEntitiesAssert result = fakeAssert.havePropertyKeys("prop");

            // THEN
            assertThat(result).isSameAs(fakeAssert);
        }

    }

    @Nested
    @DisplayName("filteredOn")
    class FilteredOnTests {

        @Test
        void should_filter_entities_and_create_a_new_assertions() {
            // GIVEN
            FakeEntitiesAssert fakeAssert = new FakeEntitiesAssert(SAMPLE_NODES);

            // WHEN
            FakeEntitiesAssert result = fakeAssert
                    .filteredOn(n -> Objects.equals(n.getProperties().get("other-prop"), "val"));

            // THEN
            assertThat(result).isNotSameAs(fakeAssert);
            assertThat(result.getActual()).hasSize(2);

        }

    }

    @Nested
    @DisplayName("havePropertyType")
    class HavePropertyTypeTests {

        @Test
        void should_fail_when_property_is_missing() {
            Assertions.fail("TODO");
        }

        @Test
        void should_fail_when_property_dont_have_the_right_type() {
            Assertions.fail("TODO");
        }

    }

    @Nested
    @DisplayName("haveProperty")
    class HavePropertyTests {
        @Test
        void should_fail_when_property_is_missing() {
            Assertions.fail("TODO");
        }

    }

}
