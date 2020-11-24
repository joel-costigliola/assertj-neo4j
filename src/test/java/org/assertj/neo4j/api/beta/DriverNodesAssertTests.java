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
import org.assertj.neo4j.api.beta.testing.Mocks;
import org.assertj.neo4j.api.beta.testing.NodeBuilder;
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
                    Nodes.node().id(22).label("lbl-1").build(),
                    Nodes.node().id(56).label("lbl-2").build()
            );
            DriverNodesAssert driverNodesAssert = new DriverNodesAssert(nodes, null);

            // WHEN
            final DriverNodesAssert result = driverNodesAssert.ignoringIds();

            // THEN
            Assertions.fail("TODO");
        }
    }

    @Nested
    @DisplayName("haveLabels")
    class HaveLabelsTests {

        @Test
        void should_fail_whenNoLabelsProvided() {
            // GIVEN
            final DriverNodesAssert nodesAssert = new DriverNodesAssert(Collections.emptyList(), null);

            // WHEN
            final Throwable throwable = catchThrowable(nodesAssert::haveLabels);

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The array of values to look for should not be empty");
        }

        @Test
        void should_fail_whenIterablesIsEmpty() {
            // GIVEN
            final DriverNodesAssert nodesAssert = new DriverNodesAssert(Collections.emptyList(), null);

            // WHEN
            final Throwable throwable = catchThrowable(() -> nodesAssert.haveLabels(Collections.emptyList()));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The iterable of values to look for should not be empty");
        }

        @Test
        void should_pass_whenSingleValue() {

            // GIVEN
            final List<Nodes.DbNode> nodes = Arrays.asList(
                    Nodes.node().label("Test").build(),
                    Nodes.node().label("Test").build(),
                    Nodes.node().label("Test").build(),
                    Nodes.node().label("Test").build(),
                    Nodes.node().label("Test").build()
            );
            final DriverNodesAssert nodesAssert = new DriverNodesAssert(nodes, null);

            // WHEN
            final DriverNodesAssert result = nodesAssert.haveLabels("Test");

            // THEN
            assertThat(result).isSameAs(nodesAssert);
        }

        @Test
        void should_fail_whenSingleValue() {
            // GIVEN
            final List<Nodes.DbNode> nodes = Arrays.asList(
                    Nodes.node().label("Test").build(),
                    Nodes.node().label("Test").build(),
                    Nodes.node().label("OTHER-LABEL").build(),
                    Nodes.node().label("Test").build(),
                    Nodes.node().label("Test").build()
            );
            final DriverNodesAssert nodesAssert = new DriverNodesAssert(nodes, null);

            // WHEN
            final Throwable throwable = catchThrowable(() -> nodesAssert.haveLabels("Test"));

            // THEN
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        }
    }

}
