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
package org.assertj.neo4j.api.beta;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * @author patouche - 22/02/2021
 */
class AbstractDbListAssertTests {

    //@formatter:off
    private static class ConcreteDbListAssert<E> extends AbstractDbListAssert<ConcreteDbListAssert<E>,
                                                                           List<E>,
                                                                           E,
                                                                           ConcreteDbListAssert<E>,
                                                                           ConcreteDbListAssert<E>,
                                                                           ConcreteDbListAssert<E>> {
        //@formatter:on

        protected ConcreteDbListAssert(final List<E> actual) {
            this(actual, null);
        }

        protected ConcreteDbListAssert(final List<E> actual, final ConcreteDbListAssert<E> parentAssert) {
            super(actual, ConcreteDbListAssert.class, ConcreteDbListAssert::new, parentAssert, rootAssert(parentAssert));
        }

        @Override
        public ConcreteDbListAssert<E> toRootAssert() {
            return rootAssert().orElse(this);
        }
    }

    private static class BaseDbListTests<E> {

        protected ConcreteDbListAssert<E> assertions;

        @SafeVarargs
        BaseDbListTests(E... elements) {
            testCase(elements);
        }

        protected void testCase(E... elements) {
            this.assertions = new ConcreteDbListAssert<E>(Arrays.asList(elements));
        }

    }

    @Nested
    @DisplayName("isEmpty")
    class IsEmptyTests extends BaseDbListTests<String> {

        @Test
        void should_fail() {
            // GIVEN
            testCase("val-1", "val-2");

            // WHEN
            final Throwable result = catchThrowable(() -> assertions.isEmpty());

            // THEN
            assertThat(result)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContaining("Expecting empty but was:<[\"val-1\", \"val-2\"]>");
        }

        @Test
        void should_pass() {
            // WHEN
            final ConcreteDbListAssert<String> result = assertions.isEmpty();

            // THEN
            assertThat(result).isSameAs(assertions);
        }
    }

    @Nested
    @DisplayName("isNotEmpty")
    class IsNotEmptyTests extends BaseDbListTests<String> {

        @Test
        void should_fail() {
            // WHEN
            final Throwable result = catchThrowable(() -> assertions.isNotEmpty());

            // THEN
            assertThat(result)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContaining("Expecting actual not to be empty");
        }

        @Test
        void should_pass() {
            // GIVEN
            testCase("val-1", "val-2");

            // WHEN
            final ConcreteDbListAssert<String> result = assertions.isNotEmpty();

            // THEN
            assertThat(result).isSameAs(assertions);
        }
    }

    @Nested
    @DisplayName("contains")
    class ContainsTests extends BaseDbListTests<String> {

        ContainsTests() {
            super("str-1", "str-2", "str-3");
        }

        @Test
        void should_fail() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.contains("str-1", "other", "toto"));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContaining("toto");
        }

        @Test
        void should_pass() {
            // WHEN
            final ConcreteDbListAssert<String> result = assertions.contains("str-1", "str-2", "str-3");

            // THEN
            assertThat(result).isSameAs(assertions);
        }

    }

    @Nested
    @DisplayName("hasSize")
    class HasSizeTests extends BaseDbListTests<String> {

        HasSizeTests() {
            super("str-1", "str-2", "str-3");
        }

        @Test
        void should_fail() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.hasSize(4));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContaining("Expected size:<4> but was:<3> in:");
        }

        @Test
        void should_pass() {
            // WHEN
            final ConcreteDbListAssert<String> result = assertions.hasSize(3);
            // THEN
            assertThat(result).isSameAs(assertions);
        }

    }

    @Nested
    @DisplayName("anyMatch")
    class AnyMatchTests extends BaseDbListTests<Integer> {

        AnyMatchTests() {
            super(IntStream.rangeClosed(0, 10).boxed().toArray(Integer[]::new));
        }

        @Test
        void should_fail() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.anyMatch(i -> i > 20));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContaining("Expecting any elements of:");
        }

        @Test
        void should_pass() {
            // WHEN
            final ConcreteDbListAssert<Integer> result = assertions.anyMatch(i -> i < 5);

            // THEN
            assertThat(result).isSameAs(assertions);
        }

    }

    @Nested
    @DisplayName("allMatch")
    class AllMatchTests extends BaseDbListTests<Integer> {

        AllMatchTests() {
            super(IntStream.rangeClosed(0, 10).boxed().toArray(Integer[]::new));
        }

        @Test
        void should_fail() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.allMatch(i -> i < 5));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContaining("Expecting all elements of:");
        }

        @Test
        void should_pass() {
            // WHEN
            final ConcreteDbListAssert<Integer> result = assertions.allMatch(i -> i < 20);

            // THEN
            assertThat(result).isSameAs(assertions);
        }

    }

}
