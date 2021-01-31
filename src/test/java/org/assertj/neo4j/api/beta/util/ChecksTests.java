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
 * @author patouche - 28/01/2021
 */
class ChecksTests {

    @Nested
    @DisplayName("notNullOrEmpty(Object[],String)")
    class NotNullOrEmptyOnArrays {

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
    class NotNullOrEmptyOnIterable {

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



}
