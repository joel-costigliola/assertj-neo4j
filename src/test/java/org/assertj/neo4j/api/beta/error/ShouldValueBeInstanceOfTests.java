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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 21/02/2021
 */
class ShouldValueBeInstanceOfTests {

    @Nested
    class CreateTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final DbValue actual = DbValue.fromObject("str");

            // WHEN
            final ErrorMessageFactory error = ShouldValueBeInstanceOf.create(actual, Boolean.class);

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting value:\n"
                    + "  <STRING{\"str\"}>\n"
                    + "to have value instance of:\n"
                    + "  <java.lang.Boolean>\n"
                    + "but actual value class is:\n"
                    + "  <java.lang.String>\n"
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
            final ErrorMessageFactory error = ShouldValueBeInstanceOf
                    .elements(actual, Double.class)
                    .notSatisfies(Randomize.listOf(val1, val2));

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting values:\n"
                    + "  <[FLOAT{3.14}, STRING{\"str\"}, BOOLEAN{true}]>\n"
                    + "to have values instance of:\n"
                    + "  <java.lang.Double>\n"
                    + "but some values have value which are not instance of this class:\n"
                    + "\n"
                    + "  1) STRING{\"str\"}\n"
                    + "    - Actual value class: java.lang.String\n"
                    + "\n"
                    + "  2) BOOLEAN{true}\n"
                    + "    - Actual value class: java.lang.Boolean\n"
            );
        }

    }
}
