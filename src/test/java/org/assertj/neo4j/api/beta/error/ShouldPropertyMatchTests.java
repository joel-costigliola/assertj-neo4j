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
import org.assertj.neo4j.api.beta.type.DbNode;
import org.assertj.neo4j.api.beta.type.Models;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 10/02/2021
 */
class ShouldPropertyMatchTests {

    @Nested
    class CreateTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final DbNode actual = Models.node().id(2).property("key", Samples.LOCAL_DATE_TIME).build();

            // WHEN
            final ErrorMessageFactory error = ShouldPropertyMatch.create(actual, "key");

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting node to have property:\n"
                    + "  <\"key\">\n"
                    + "matching the provided condition for its value:\n"
                    + "  <2020-02-03T04:05:06.000000007 (java.time.LocalDateTime)>\n"
                    + "but this value of type LOCAL_DATE_TIME did not.\n"
            );
        }
    }

    @Nested
    class ElementsTests {

        @Test
        void should_generate_an_aggregate_error_message() {
            // GIVEN
            final DbNode node1 = Models.node().id(1).property("key", "value-1").build();
            final DbNode node2 = Models.node().id(2).property("key", Samples.LOCAL_DATE_TIME).build();
            final DbNode node3 = Models.node().id(3).property("key", 1).build();
            final List<DbNode> actual = Randomize.listOf(node1, node2, node3);

            // WHEN
            final ErrorMessageFactory error = ShouldPropertyMatch
                    .elements(actual, "key")
                    .notSatisfies(Randomize.listOf(node1, node2));

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting nodes:\n"
                    + "  <[NODE{id=1L, labels=[], properties={key=STRING{\"value-1\"}}},\n"
                    + "    NODE{id=2L, labels=[], properties={key=LOCAL_DATE_TIME{2020-02-03T04:05:06.000000007}}},\n"
                    + "    NODE{id=3L, labels=[], properties={key=INTEGER{1}}}]>\n"
                    + "to have a for the property \"key\" matching the condition but nodes:\n"
                    + "  <[NODE{id=1L, labels=[], properties={key=STRING{\"value-1\"}}},\n"
                    + "    NODE{id=2L, labels=[], properties={key=LOCAL_DATE_TIME{2020-02-03T04:05:06.000000007}}}]>\n"
                    + "did not:\n"
                    + "\n"
                    + "  1) NODE{id=1L, labels=[], properties={key=STRING{\"value-1\"}}}\n"
                    + "    - Actual property value: \"value-1\"\n"
                    + "    - Actual property type: STRING\n"
                    + "\n"
                    + "  2) NODE{id=2L, labels=[], properties={key=LOCAL_DATE_TIME{2020-02-03T04:05:06.000000007}}}\n"
                    + "    - Actual property value: 2020-02-03T04:05:06.000000007 (java.time.LocalDateTime)\n"
                    + "    - Actual property type: LOCAL_DATE_TIME\n"
            );
        }

    }
}
