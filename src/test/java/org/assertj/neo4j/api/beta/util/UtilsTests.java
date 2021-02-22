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

import org.assertj.neo4j.api.beta.testing.Builders;
import org.assertj.neo4j.api.beta.testing.Randomize;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.types.Entity;
import org.neo4j.driver.types.Node;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

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

    private static class FakeIterable<T> implements Iterable<T> {

        private final Collection<T> collection;

        public FakeIterable(Collection<T> collection) {
            this.collection = collection;
        }

        @Override
        public Iterator<T> iterator() {
            return collection.iterator();
        }

        @Override
        public void forEach(Consumer<? super T> action) {
            collection.forEach(action);
        }

        @Override
        public Spliterator<T> spliterator() {
            return collection.spliterator();
        }
    }

    @Nested
    class ListOfTests {

        @Test
        void should_return_current_instance() {
            // GIVEN
            final List<Integer> iterable = Arrays.asList(1, 2, 3);

            // WHEN
            final List<Integer> result = Utils.listOf(iterable);

            // THEN
            assertThat(result).isSameAs(iterable);
        }

        @Test
        void should_transform_into_list() {
            // GIVEN
            final Iterable<Integer> iterable = new FakeIterable<>(Arrays.asList(1, 2, 3));

            // WHEN
            final List<Integer> result = Utils.listOf(iterable);

            // THEN
            assertThat(result)
                    .isNotSameAs(iterable)
                    .contains(1, 2, 3);
        }
    }

    @Nested
    class LazyDeepEqualsTests {

        @Test
        void should_be_always_true() {
            // GIVEN
            final Node node1 = Builders.node().id(1).property("prop", 1).build();
            final Node node2 = Builders.node().id(2).property("prop", 2).build();

            // WHEN
            final BiPredicate<Node, Node> result = Utils.lazyDeepEquals(true, Entity::id);

            // THEN
            assertThat(result.test(node1, node2)).isTrue();
        }

        @Test
        void should_test_equality() {
            // GIVEN
            final Node node1 = Builders.node().id(1).property("prop", 1).build();
            final Node node2 = Builders.node().id(2).property("prop", 2).build();

            // WHEN
            final BiPredicate<Node, Node> result = Utils.lazyDeepEquals(false, Entity::id);

            // THEN
            assertThat(result.test(node1, node2)).isFalse();
        }
    }

    @Nested
    class CombineTests {

        @Test
        void should_return_false() {
            // GIVEN
            final BiPredicate<Integer, Integer> predicate1 = (i, j) -> i <= j;
            final BiPredicate<Integer, Integer> predicate2 = (i, j) -> i >= j;

            // WHEN
            final BiPredicate<Integer, Integer> result = Utils.combine(predicate1, predicate2);

            // THEN
            assertThat(result.test(10, 11)).isFalse();
        }

        @Test
        void should_return_true() {
            // GIVEN
            final BiPredicate<Integer, Integer> predicate1 = (i, j) -> i <= j;
            final BiPredicate<Integer, Integer> predicate2 = (i, j) -> i >= j;

            // WHEN
            final BiPredicate<Integer, Integer> result = Utils.combine(predicate1, predicate2);

            // THEN
            assertThat(result.test(10, 10)).isTrue();
        }
    }

    @Nested
    class ComparatorsTests {

        @Test
        void should_create_an_comparator_from_comparable_elements() {
            // GIVEN
            final Node node1 = Builders.node().id(1).labels("lbl-1", "lbl-2").build();
            final Node node2 = Builders.node().id(2).property("prop", 1).build();
            final Node node3 = Builders.node().id(3).property("prop", 2).build();
            final Node node4 = Builders.node().id(4).property("prop", 3).build();
            final Node node5 = Builders.node().id(5).property("prop", 4).property("prop-2", "str-1").build();
            final Node node6 = Builders.node().id(6).property("prop", 4).property("prop-2", "str-2").build();
            final Node node7 = Builders.node().id(7).property("prop", 4).property("prop-2", "str-3").build();
            final List<Node> nodes = Randomize.listOf(node1, node2, node3, node4, node5, node6, node7);

            // WHEN
            final Comparator<Node> comparator = Utils.comparators(
                    Node::labels, // NOT COMPARABLE
                    (n) -> n.get("prop").asInt(0),
                    (n) -> n,     // NOT COMPARABLE
                    (n) -> n.get("prop-2").asString("")
            );

            // THEN
            assertThat(nodes.stream().sorted(comparator).collect(Collectors.toList()))
                    .containsExactly(node1, node2, node3, node4, node5, node6, node7);
        }
    }

    @Nested
    class FirstTests {

        @Test
        void should_throw_an_exception_when_the_iterable_is_empty() {
            // GIVEN
            final Iterable<String> iterable = Collections.emptyList();

            // WHEN
            final Throwable throwable = catchThrowable(() -> Utils.first(iterable));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot retrieve first element of an empty iterable.");
        }

        @Test
        void should_return_the_first_element_in_an_iterable() {
            // GIVEN
            final Iterable<String> iterable = Arrays.asList("str-1", "str-2", "str-3");

            // WHEN
            final String result = Utils.first(iterable);

            // THEN
            assertThat(result).isEqualTo("str-1");
        }
    }

}
