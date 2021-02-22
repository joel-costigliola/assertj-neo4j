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
import org.assertj.neo4j.api.beta.type.DbValue;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 21/02/2021
 */
class ShouldValueBeOfTypeTests {

    @Nested
    class CreateTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final DbValue actual = DbValue.fromObject("str");

            // WHEN
            final ErrorMessageFactory error = ShouldValueBeOfType.create(actual, ValueType.FLOAT);

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting value:\n"
                    + "  <STRING{\"str\"}>\n"
                    + "to have a value type:\n"
                    + "  <FLOAT>\n"
                    + "but actual value type is:\n"
                    + "  <STRING>"
            );
        }
    }

    @Nested
    class ElementsTests {

        @Test
        void should_generate_an_aggregate_error_message() {
            // GIVEN
            final DbValue val1 = DbValue.fromObject("str");
            final DbValue val2 = DbValue.fromObject(true);
            final DbValue val3 = DbValue.fromObject(3.14);
            final List<DbValue> actual = Randomize.listOf(val1, val2, val3);

            // WHEN
            final ErrorMessageFactory error = ShouldValueBeOfType
                    .elements(actual, ValueType.FLOAT)
                    .notSatisfies(Randomize.listOf(val1, val2));

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting values:\n"
                    + "  <[FLOAT{3.14}, STRING{\"str\"}, BOOLEAN{true}]>\n"
                    + "to have a value type:\n"
                    + "  <FLOAT>\n"
                    + "but some values have a different value type:\n"
                    + "\n"
                    + "  1) STRING{\"str\"}\n"
                    + "    - Actual value type: STRING\n"
                    + "\n"
                    + "  2) BOOLEAN{true}\n"
                    + "    - Actual value type: BOOLEAN\n"
            );
        }

    }

}
