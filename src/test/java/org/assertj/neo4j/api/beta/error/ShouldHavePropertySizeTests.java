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
import org.assertj.neo4j.api.beta.testing.Samples;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes.DbNode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 01/02/2021
 */
class ShouldHavePropertySizeTests {

    @Nested
    class CreateTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final DbNode actual = Drivers.node().id(42).property("k-2", 3.14).property("k-1", "v-1").build();

            // WHEN
            final ErrorMessageFactory error = ShouldHavePropertySize.create(actual, 3);

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting node to have property size:\n"
                    + " <3>\n"
                    + "but actual property size is:\n"
                    + " <2>\n"
                    + "containing the following property keys:\n"
                    + " <[\"k-1\", \"k-2\"]>\n"
            );
        }
    }

    @Nested
    class ElementsTests {

        @Test
        void should_generate_an_aggregate_error_message() {
            // GIVEN
            final DbNode node1 = Drivers.node().id(1).property("k-1", "v-1").build();
            final DbNode node2 = Drivers.node().id(2).property("k-1", "v-2").property("k-2", 3.14).build();
            final DbNode node3 = Drivers.node().id(3)
                    .property("k-1", "v-3")
                    .property("k-2", 6.12)
                    .property("k-3", false)
                    .property("k-4", "some-prop")
                    .build();
            final DbNode node4 = Drivers.node().id(4)
                    .property("k-1", "v-4")
                    .property("k-4", "some-other-prop")
                    .build();
            final DbNode node5 = Drivers.node().id(5)
                    .property("k-1", "v-5")
                    .property("k-2", 2.0)
                    .property("k-3", true)
                    .property("k-5", Samples.ZONED_DATE_TIME)
                    .build();
            final DbNode node6 = Drivers.node().id(6)
                    .property("k-1", "v-6")
                    .property("k-2", 8.5)
                    .property("k-3", true)
                    .build();

            final List<DbNode> actual = Randomize.listOf(node1, node2, node3, node4, node5, node6);

            // WHEN
            final ErrorMessageFactory error = ShouldHavePropertySize
                    .elements(actual, 3)
                    .notSatisfies(Randomize.listOf(node1, node2, node3, node4, node5));

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting nodes:\n"
                    + "  <[NODE{id=1L, labels=[], properties={k-1=STRING{\"v-1\"}}},\n"
                    + "    NODE{id=2L, labels=[], properties={k-2=FLOAT{3.14}, k-1=STRING{\"v-2\"}}},\n"
                    + "    NODE{id=3L, labels=[], properties={k-3=BOOLEAN{false}, k-2=FLOAT{6.12}, "
                    + "k-4=STRING{\"some-prop\"}, k-1=STRING{\"v-3\"}}},\n"
                    + "    NODE{id=4L, labels=[], properties={k-4=STRING{\"some-other-prop\"}, k-1=STRING{\"v-4\"}}},\n"
                    + "    NODE{id=5L, labels=[], properties={k-3=BOOLEAN{true}, k-2=FLOAT{2.0}, "
                    + "k-5=DATE_TIME{2020-02-03T04:05:06.000000007+11:00[Australia/Sydney]}, k-1=STRING{\"v-5\"}}},\n"
                    + "    NODE{id=6L, labels=[], properties={k-3=BOOLEAN{true}, k-2=FLOAT{8.5}, "
                    + "k-1=STRING{\"v-6\"}}}]>\n"
                    + "to have a property size:\n"
                    + "  <3>\n"
                    + "but some nodes have another property size:\n"
                    + "\n"
                    + "  1) NODE{id=1L, labels=[], properties={k-1=STRING{\"v-1\"}}}\n"
                    + "    - Actual property size: 1\n"
                    + "    - Actual property keys: [\"k-1\"]\n"
                    + "\n"
                    + "  2) NODE{id=2L, labels=[], properties={k-2=FLOAT{3.14}, k-1=STRING{\"v-2\"}}}\n"
                    + "    - Actual property size: 2\n"
                    + "    - Actual property keys: [\"k-1\", \"k-2\"]\n"
                    + "\n"
                    + "  3) NODE{id=3L, labels=[], properties={k-3=BOOLEAN{false}, k-2=FLOAT{6.12}, "
                    + "k-4=STRING{\"some-prop\"}, k-1=STRING{\"v-3\"}}}\n"
                    + "    - Actual property size: 4\n"
                    + "    - Actual property keys: [\"k-1\", \"k-2\", \"k-3\", \"k-4\"]\n"
                    + "\n"
                    + "  4) NODE{id=4L, labels=[], properties={k-4=STRING{\"some-other-prop\"}, k-1=STRING{\"v-4\"}}}\n"
                    + "    - Actual property size: 2\n"
                    + "    - Actual property keys: [\"k-1\", \"k-4\"]\n"
                    + "\n"
                    + "  5) NODE{id=5L, labels=[], properties={k-3=BOOLEAN{true}, k-2=FLOAT{2.0}, "
                    + "k-5=DATE_TIME{2020-02-03T04:05:06.000000007+11:00[Australia/Sydney]}, k-1=STRING{\"v-5\"}}}\n"
                    + "    - Actual property size: 4\n"
                    + "    - Actual property keys: [\"k-1\", \"k-2\", \"k-3\", \"k-5\"]\n"
            );
        }
    }

}
