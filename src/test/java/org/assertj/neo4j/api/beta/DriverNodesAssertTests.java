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
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * @author pallain - 12/11/2020
 */
public class DriverNodesAssertTests {

    @Nested
    class IgnoringIdsTests {

        @Test
        void should_return_a_list_of_nodes_without_ids() {
            // GIVEN
            final List<Nodes.DbNode> nodes = Arrays.asList(
                    Drivers.node().id(22).label("lbl-1").build(),
                    Drivers.node().id(56).label("lbl-2").build()
            );
            final DriverNodesAssert nodesAssert = new DriverNodesAssert(nodes, null, null);

            // WHEN
            final DriverNodesAssert result = nodesAssert.ignoringIds();

            // THEN
            assertThat(result.getActual())
                    .extracting(DbEntity::getId)
                    .containsOnlyNulls();
        }
    }

    @Nested
    @DisplayName("haveLabels")
    class HaveLabelsTests {

        @Test
        void should_fail_when_no_labels_provided() {
            // GIVEN
            final DriverNodesAssert nodesAssert = new DriverNodesAssert(Collections.emptyList(), null, null);

            // WHEN
            final Throwable throwable = catchThrowable(nodesAssert::haveLabels);

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The labels to look for should not be null or empty");
        }

        @Test
        void should_fail_when_iterable_is_empty() {
            // GIVEN
            final DriverNodesAssert nodesAssert = new DriverNodesAssert(Collections.emptyList(), null, null);

            // WHEN
            final Throwable throwable = catchThrowable(() -> nodesAssert.haveLabels(Collections.emptyList()));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The iterable of values to look for should not be empty");
        }

        @Test
        void should_pass_when_single_value() {

            // GIVEN
            final List<Nodes.DbNode> nodes = Arrays.asList(
                    Drivers.node().label("Test").build(),
                    Drivers.node().label("Test").build(),
                    Drivers.node().label("Test").build(),
                    Drivers.node().label("Test").build(),
                    Drivers.node().label("Test").build()
            );
            final DriverNodesAssert nodesAssert = new DriverNodesAssert(nodes, null, null);

            // WHEN
            final DriverNodesAssert result = nodesAssert.haveLabels("Test");

            // THEN
            assertThat(result).isSameAs(nodesAssert);
        }

        @Test
        void should_fail_when_single_value() {
            // GIVEN
            final List<Nodes.DbNode> nodes = Arrays.asList(
                    Drivers.node().label("Test").build(),
                    Drivers.node().label("Test").build(),
                    Drivers.node().label("OTHER-LABEL").build(),
                    Drivers.node().label("Test").build(),
                    Drivers.node().label("Test").build()
            );
            final DriverNodesAssert nodesAssert = new DriverNodesAssert(nodes, null, null);

            // WHEN
            final Throwable throwable = catchThrowable(() -> nodesAssert.haveLabels("Test"));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
            .hasMessageContainingAll("Expecting nodes:", "to have all the following labels:");
        }
    }

    @Nested
    class HavePropertyKeysTests {

        private final List<Nodes.DbNode> samplePropNodes = Arrays.asList(
                Drivers.node().id(1).property("prop", "value-1").build(),
                Drivers.node().id(2).property("prop", "value-2").build(),
                Drivers.node().id(3).property("prop", "value-3").build(),
                Drivers.node().id(4).property("prop", "value-4").build(),
                Drivers.node().id(5).property("prop", "value-5").build()
        );

        @Test
        void should_fail_when_no_labels_provided() {
            // GIVEN
            final DriverNodesAssert nodesAssert = new DriverNodesAssert(samplePropNodes, null, null);

            // WHEN
            final Throwable throwable = catchThrowable(nodesAssert::havePropertyKeys);

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The property keys to look for should not be null or empty");
        }

        @Test
        void should_fail_when_iterable_is_empty() {
            // GIVEN
            final DriverNodesAssert nodesAssert = new DriverNodesAssert(samplePropNodes, null, null);

            // WHEN
            final Throwable throwable = catchThrowable(() -> nodesAssert.havePropertyKeys(Collections.emptyList()));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The iterable of property keys to look for should not be empty");
        }

        @Test
        void should_fail_when_nodes_have_missing_values() {
            // GIVEN
            final List<Nodes.DbNode> nodes = Arrays.asList(
                    Drivers.node().id(1).property("prop", "value-1").build(),
                    Drivers.node().id(2).property("other-prop", "value-2").build(),
                    Drivers.node().id(3).property("prop", "value-3").build(),
                    Drivers.node().id(4).property("other-prop", "value-4").build(),
                    Drivers.node().id(5).property("prop", "value-5").build()
            );
            final DriverNodesAssert nodesAssert = new DriverNodesAssert(nodes, null, null);

            // WHEN
            final Throwable throwable = catchThrowable(() -> nodesAssert.havePropertyKeys("prop"));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll("Expecting nodes:", "but some property keys were missing on:");
        }

        @Test
        void should_pass() {
            // GIVEN
            final DriverNodesAssert nodesAssert = new DriverNodesAssert(samplePropNodes, null, null);

            // WHEN
            final DriverNodesAssert result = nodesAssert.havePropertyKeys("prop");

            // THEN
            assertThat(result).isSameAs(nodesAssert);
        }


    }

}
