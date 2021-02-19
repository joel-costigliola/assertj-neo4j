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
import org.neo4j.driver.Values;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 31/01/2021
 */
class ShouldHavePropertyValueTests {

    @Nested
    class CreateTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final DbNode actual = Models.node().id(1).property("key", "value").build();

            // WHEN
            final ErrorMessageFactory error = ShouldHavePropertyValue.create(actual, "key", "other-value");

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting node to have a property \"key\" with value:\n"
                    + "  <\"other-value\">\n"
                    + "but current value of this property is:\n"
                    + "  <\"value\">\n"
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
            final DbNode node4 = Models.node().id(4).property("key", Values.point(1, 22.29, 56.35).asObject()).build();
            final DbNode node5 = Models.node().id(5).property("key", Samples.ZONED_DATE_TIME).build();
            final DbNode node6 = Models.node().id(6).property("key", true).build();
            final List<DbNode> actual = Randomize.listOf(node1, node2, node3, node4, node5, node6);

            // WHEN
            final ErrorMessageFactory result = ShouldHavePropertyValue
                    .elements(actual, "key", true)
                    .notSatisfies(Randomize.listOf(node1, node2, node3, node4, node5));

            // THEN
            assertThat(result.create()).isEqualToNormalizingNewlines(
                    "\nExpecting nodes:\n"
                    + "  <[NODE{id=1L, labels=[], properties={key=STRING{\"value-1\"}}},\n"
                    + "    NODE{id=2L, labels=[], properties={key=LOCAL_DATE_TIME{2020-02-03T04:05:06.000000007}}},\n"
                    + "    NODE{id=3L, labels=[], properties={key=INTEGER{1}}},\n"
                    + "    NODE{id=4L, labels=[], properties={key=POINT{Point{srid=1, x=22.29, y=56.35}}}},\n"
                    + "    NODE{id=5L, labels=[], properties={key=DATE_TIME{2020-02-03T04:05:06"
                    + ".000000007+11:00[Australia/Sydney]}}},\n"
                    + "    NODE{id=6L, labels=[], properties={key=BOOLEAN{true}}}]>\n"
                    + "to have a property named \"key\" with value:\n"
                    + "  <true>\n"
                    + "but some nodes have a different value for this property:\n"
                    + "\n"
                    + "  1) NODE{id=1L, labels=[], properties={key=STRING{\"value-1\"}}}\n"
                    + "    - Actual value: \"value-1\"\n"
                    + "    - Actual type: STRING\n"
                    + "\n"
                    + "  2) NODE{id=2L, labels=[], properties={key=LOCAL_DATE_TIME{2020-02-03T04:05:06.000000007}}}\n"
                    + "    - Actual value: 2020-02-03T04:05:06.000000007 (java.time.LocalDateTime)\n"
                    + "    - Actual type: LOCAL_DATE_TIME\n"
                    + "\n"
                    + "  3) NODE{id=3L, labels=[], properties={key=INTEGER{1}}}\n"
                    + "    - Actual value: 1L\n"
                    + "    - Actual type: INTEGER\n"
                    + "\n"
                    + "  4) NODE{id=4L, labels=[], properties={key=POINT{Point{srid=1, x=22.29, y=56.35}}}}\n"
                    + "    - Actual value: Point{srid=1, x=22.29, y=56.35}\n"
                    + "    - Actual type: POINT\n"
                    + "\n"
                    + "  5) NODE{id=5L, labels=[], properties={key=DATE_TIME{2020-02-03T04:05:06"
                    + ".000000007+11:00[Australia/Sydney]}}}\n"
                    + "    - Actual value: 2020-02-03T04:05:06.000000007+11:00[Australia/Sydney] (java.time"
                    + ".ZonedDateTime)\n"
                    + "    - Actual type: DATE_TIME\n"
            );
        }
    }
}
