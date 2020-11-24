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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author pallain - 14/11/2020
 */
public class ItemsTests {

    public static List<String> STRINGS = IntStream.range(0, 10).mapToObj(i -> "val-" + i).collect(Collectors.toList());

    @Nested
    class MissingTests {

        @Test
        void shouldReturnNoValues_sameItems() {
            // GIVEN
            final List<String> items = STRINGS;
            final List<String> searchedItems = STRINGS;

            // WHEN
            final List<String> result = Items.missing(items, searchedItems);

            // THEN
            Assertions.assertThat(result).isNotNull().isEmpty();
        }

        @Test
        void shouldReturnNoValues_allSearchItemsArePresent() {
            // GIVEN
            final List<String> items = STRINGS;
            final List<String> searchedItems = STRINGS.stream().skip(2).collect(Collectors.toList());

            // WHEN
            final List<String> result = Items.missing(items, searchedItems);

            // THEN
            Assertions.assertThat(result).isNotNull().isEmpty();
        }

        @Test
        void shouldReturnOnlyTheMissingValues() {
            // GIVEN
            final List<String> items = STRINGS.stream().skip(2).collect(Collectors.toList());
            final List<String> searchedItems = STRINGS;

            // WHEN
            final List<String> result = Items.missing(items, searchedItems);

            // THEN
            Assertions.assertThat(result)
                    .hasSize(2)
                    .contains("val-0", "val-1");
        }

    }

}
