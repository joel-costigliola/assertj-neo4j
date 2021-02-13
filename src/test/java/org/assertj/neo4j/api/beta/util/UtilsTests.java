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
package org.assertj.neo4j.api.beta.util;

import org.assertj.core.api.Assertions;
import org.assertj.neo4j.api.beta.testing.Randomize;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 05/02/2021
 */
class UtilsTests {

    @Nested
    class MissingTests {

        @Test
        void should_return_an_empty_list() {
            // GIVEN
            final Iterable<String> searched = Randomize.listOf("str-3", "str-5", "str-7", "str-9");
            final Iterable<String> items = IntStream.rangeClosed(1, 10)
                    .mapToObj(i -> "str-" + i)
                    .sorted(Randomize.comparator())
                    .collect(Collectors.toList());

            // WHEN
            final List<String> result = Utils.missing(searched, items);

            // THEN
            assertThat(result).isEmpty();
        }

        @Test
        void should_return_the_missing_items() {
            // GIVEN
            final Iterable<String> searched = Randomize.listOf("str-3", "str-15", "str-17", "str-29");
            final Iterable<String> items = IntStream.rangeClosed(1, 10)
                    .mapToObj(i -> "str-" + i)
                    .sorted(Randomize.comparator())
                    .collect(Collectors.toList());

            // WHEN
            final List<String> result = Utils.missing(searched, items);

            // THEN
            assertThat(result).containsExactly("str-15", "str-17", "str-29");
        }
    }







}
