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
import org.assertj.neo4j.api.beta.type.Nodes;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author Patrick Allain - 14/11/2020
 */
public class ShouldHaveNodeLabelsTests {

    private static final List<String> LABELS = Randomize.listOf("LBL_1", "LBL_2", "LBL_3", "LBL_4");

    @Nested
    class CreateTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final Nodes.DbNode entity = Drivers.node().id(42).labels("LBL_4", "LBL_1").build();

            // WHEN
            final ErrorMessageFactory error = ShouldHaveNodeLabels.create(entity, LABELS);

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting node labels:\n"
                    + " <[\"LBL_1\", \"LBL_4\"]>\n"
                    + "to contain:\n"
                    + " <[\"LBL_1\", \"LBL_2\", \"LBL_3\", \"LBL_4\"]>\n"
                    + "but could not find the following labels:\n"
                    + " <[\"LBL_2\", \"LBL_3\"]>\n"
            );
        }

    }

    @Nested
    class ElementsTests {

        @Test
        void should_generate_an_aggregate_error_message() {
            // GIVEN
            final Nodes.DbNode node1 = Drivers.node().id(12).labels("LBL_1").build();
            final Nodes.DbNode node2 = Drivers.node().id(18).labels("LBL_2").build();
            final Nodes.DbNode node3 = Drivers.node().id(42).labels("LBL_3").build();
            final Nodes.DbNode node4 = Drivers.node().id(51).labels("LBL_4", "LBL_1").build();
            final Nodes.DbNode node5 = Drivers.node().id(69).labels("LBL_4", "LBL_2").build();
            final Nodes.DbNode node6 = Drivers.node().id(95).labels("LBL_1", "LBL_2", "LBL_3", "LBL_4").build();

            final List<Nodes.DbNode> nodes = Randomize.listOf(node1, node2, node3, node4, node5, node6);

            // WHEN
            final ErrorMessageFactory error = ShouldHaveNodeLabels
                    .elements(nodes, LABELS)
                    .notSatisfies(Randomize.listOf(node1, node2, node3, node4, node5));

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting nodes:\n"
                    + "  <[NODE{id=12L, labels=[\"LBL_1\"], properties={}},\n"
                    + "    NODE{id=18L, labels=[\"LBL_2\"], properties={}},\n"
                    + "    NODE{id=42L, labels=[\"LBL_3\"], properties={}},\n"
                    + "    NODE{id=51L, labels=[\"LBL_1\", \"LBL_4\"], properties={}},\n"
                    + "    NODE{id=69L, labels=[\"LBL_2\", \"LBL_4\"], properties={}},\n"
                    + "    NODE{id=95L, labels=[\"LBL_1\", \"LBL_2\", \"LBL_3\", \"LBL_4\"], properties={}}]>\n"
                    + "to have the labels:\n"
                    + "  <[\"LBL_1\", \"LBL_2\", \"LBL_3\", \"LBL_4\"]>\n"
                    + "but some labels are missing for nodes:\n"
                    + "\n"
                    + "  1) NODE{id=12L, labels=[\"LBL_1\"], properties={}}\n"
                    + "    - Actual labels: [\"LBL_1\"]\n"
                    + "    - Missing labels: [\"LBL_2\", \"LBL_3\", \"LBL_4\"]\n"
                    + "\n"
                    + "  2) NODE{id=18L, labels=[\"LBL_2\"], properties={}}\n"
                    + "    - Actual labels: [\"LBL_2\"]\n"
                    + "    - Missing labels: [\"LBL_1\", \"LBL_3\", \"LBL_4\"]\n"
                    + "\n"
                    + "  3) NODE{id=42L, labels=[\"LBL_3\"], properties={}}\n"
                    + "    - Actual labels: [\"LBL_3\"]\n"
                    + "    - Missing labels: [\"LBL_1\", \"LBL_2\", \"LBL_4\"]\n"
                    + "\n"
                    + "  4) NODE{id=51L, labels=[\"LBL_1\", \"LBL_4\"], properties={}}\n"
                    + "    - Actual labels: [\"LBL_1\", \"LBL_4\"]\n"
                    + "    - Missing labels: [\"LBL_2\", \"LBL_3\"]\n"
                    + "\n"
                    + "  5) NODE{id=69L, labels=[\"LBL_2\", \"LBL_4\"], properties={}}\n"
                    + "    - Actual labels: [\"LBL_2\", \"LBL_4\"]\n"
                    + "    - Missing labels: [\"LBL_1\", \"LBL_3\"]\n"
            );
        }
    }

}
