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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * @author Patrick Allain - 28/01/2021
 */
class ChecksTests {

    @Nested
    @DisplayName("notNullOrEmpty(Object[],String)")
    class NotNullOrEmptyOnArraysTests {

        @Test
        void should_failed_when_null() {
            // GIVEN
            final Object[] items = null;

            // WHEN
            final Throwable throwable = catchThrowable(() -> Checks.notNullOrEmpty(items, "error message"));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("error message");
        }

        @Test
        void should_failed_when_empty() {
            // GIVEN
            final Object[] items = new Object[]{};

            // WHEN
            final Throwable throwable = catchThrowable(() -> Checks.notNullOrEmpty(items, "error message"));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("error message");
        }

        @Test
        void should_success_when_not_empty() {
            // GIVEN
            final String[] items = new String[]{"item-1", "item-2"};

            // WHEN
            final List<String> result = Checks.notNullOrEmpty(items, "error message");

            // THEN
            assertThat(result)
                    .hasSize(2)
                    .contains(items);
        }
    }

    @Nested
    @DisplayName("notNullOrEmpty(Iterable,String)")
    class NotNullOrEmptyOnIterableTests {

        @Test
        void should_failed_when_null() {
            // GIVEN
            final Iterable<Object> items = null;

            // WHEN
            final Throwable throwable = catchThrowable(() -> Checks.notNullOrEmpty(items, "error message"));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("error message");
        }

        @Test
        void should_failed_when_empty() {
            // GIVEN
            final Iterable<Object> items = Collections.emptyList();

            // WHEN
            final Throwable throwable = catchThrowable(() -> Checks.notNullOrEmpty(items, "error message"));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("error message");
        }

        @Test
        void should_success_when_not_empty() {
            // GIVEN
            final Iterable<String> items = Arrays.asList("item-1", "item-2");

            // WHEN
            final List<String> result = Checks.notNullOrEmpty(items, "error message");

            // THEN
            assertThat(result)
                    .hasSize(2)
                    .containsAll(items);
        }
    }

    @Nested
    @DisplayName("nonNullElementsIn(Iterable)")
    class NonNullElementsInOnIterableTests {

        @Test
        void should_fail_when_contains_null() {
            // GIVEN
            final Iterable<String> iterable = Arrays.asList("str-1",null, "str-3");

            // WHEN
            final Throwable throwable = catchThrowable(() -> Checks.nonNullElementsIn(iterable, "message"));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("message");
        }

        @Test
        void should_pass() {
            // GIVEN
            final Iterable<String> iterable = Arrays.asList("str-1","str-2", "str-3");

            // WHEN
            final List<String> result = Checks.nonNullElementsIn(iterable, "message");

            // THEN
            assertThat(result)
                    .hasSize(3)
                    .containsExactly("str-1", "str-2", "str-3");
        }
    }

}
