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

import org.assertj.core.api.Assertions;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.util.Entities;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author patouche - 14/11/2020
 */
public class ElementsShouldHaveLabelsTests {

    @Test
    void create_singleNode() {

        // GIVEN
        final List<String> expectedLabels = Arrays.asList("LBL_1", "LBL_2", "LBL_3");
        final List<Nodes.DbNode> nodes = Arrays.asList(
                Nodes.node().id(1).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                Nodes.node().id(2).label("LBL_1").label("LBL_3").build(),
                Nodes.node().id(3).label("LBL_1").label("LBL_2").label("LBL_3").build()
        );
        final List<MissingNodeLabels> havingMissingNodeLabels = Entities.havingMissingLabels(nodes, expectedLabels);

        // WHEN
        final ElementsShouldHaveLabels error = ElementsShouldHaveLabels
                .create(expectedLabels, nodes, havingMissingNodeLabels);

        // THEN
        Assertions.assertThat(error.create()).isEqualToNormalizingNewlines(
                "\nExpecting:\n"
                + "  <[\"NODE{id=1}\", \"NODE{id=2}\", \"NODE{id=3}\"]> to have all the following labels:\n"
                + "  <[\"LBL_1\", \"LBL_2\", \"LBL_3\"]>\n"
                + "but some labels where missing on:\n"
                + "\n"
                + "  - NODE{id=2} have missing labels: [LBL_2]\n"
                + "    Actual: <[LBL_1, LBL_3]>\n"
                + "    Expected: <[LBL_1, LBL_2, LBL_3]>"
        );

    }

    @Test
    void create_multipleNodes() {
        // GIVEN
        final List<String> expectedLabels = Arrays.asList("LBL_1", "LBL_2", "LBL_3");
        final List<Nodes.DbNode> nodes = Arrays.asList(
                Nodes.node().id(1).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                Nodes.node().id(2).label("LBL_1").label("LBL_3").build(),
                Nodes.node().id(3).label("LBL_1").label("LBL_2").label("LBL_3").build(),
                Nodes.node().id(4).label("LBL_1").label("LBL_2").build(),
                Nodes.node().id(5).label("LBL_1").build()
        );
        final List<MissingNodeLabels> havingMissingNodeLabels = Entities.havingMissingLabels(nodes, expectedLabels);

        // WHEN
        final ElementsShouldHaveLabels error = ElementsShouldHaveLabels
                .create(expectedLabels, nodes, havingMissingNodeLabels);

        // THEN
        Assertions.assertThat(error.create()).isEqualToNormalizingNewlines(
                "\nExpecting:\n"
                + "  <[\"NODE{id=1}\", \"NODE{id=2}\", \"NODE{id=3}\", \"NODE{id=4}\", \"NODE{id=5}\"]> to "
                + "have all the following labels:\n"
                + "  <[\"LBL_1\", \"LBL_2\", \"LBL_3\"]>\n"
                + "but some labels where missing on:\n"
                + "\n"
                + "  - NODE{id=2} have missing labels: [LBL_2]\n"
                + "    Actual: <[LBL_1, LBL_3]>\n"
                + "    Expected: <[LBL_1, LBL_2, LBL_3]>\n"
                + "\n"
                + "  - NODE{id=4} have missing labels: [LBL_3]\n"
                + "    Actual: <[LBL_1, LBL_2]>\n"
                + "    Expected: <[LBL_1, LBL_2, LBL_3]>\n"
                + "\n"
                + "  - NODE{id=5} have missing labels: [LBL_2, LBL_3]\n"
                + "    Actual: <[LBL_1]>\n"
                + "    Expected: <[LBL_1, LBL_2, LBL_3]>"
        );

    }
}
