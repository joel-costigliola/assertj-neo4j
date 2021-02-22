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

import org.assertj.core.util.IterableUtil;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Streams;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Patrick Allain - 03/02/2021
 */
public final class Utils {

    /**
     * Transform a iterable into a list.
     *
     * @param iterable the iterable to transform
     * @param <T>      the type of elements return by the iterator
     * @return a list of elements.
     */
    public static <T> List<T> listOf(final Iterable<T> iterable) {
        if (iterable instanceof List) {
            return (List<T>) iterable;
        }
        return Lists.newArrayList(iterable);
    }

    /**
     * Transform a array into a list.
     *
     * @param array the array to transform
     * @param <T>   the type of elements return by the iterator
     * @return a list of elements.
     */
    @SafeVarargs
    public static <T> List<T> listOf(final T... array) {
        return Arrays.asList(array);
    }

    /**
     * Return a list of {items} that don't belong to the {@code searched} list.
     *
     * @param searched the searched list
     * @param items    the items
     * @return a list of items
     */
    public static List<String> missing(final Iterable<String> searched, final Iterable<String> items) {
        final Collection<String> collectionItems = IterableUtil.toCollection(items);
        return Streams.stream(searched)
                .filter(s -> !collectionItems.contains(s))
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Sort an iterable of comparable.
     *
     * @param items the items
     * @param <T>   the type of element in the the iterator
     * @return a sorted list
     */
    public static <T extends Comparable<T>> List<T> sorted(final Iterable<? extends T> items) {
        return Streams.stream(items).sorted().collect(Collectors.toList());
    }

    public static <I, O> BiPredicate<I, I> lazyDeepEquals(final boolean skipped, final Function<I, O> fn) {
        if (skipped) {
            return (l, r) -> true;
        }
        return lazyDeepEquals(fn);
    }

    public static <I, O> BiPredicate<I, I> lazyDeepEquals(final Function<I, O> fn) {
        return (l, r) -> Objects.deepEquals(fn.apply(l), fn.apply(r));
    }

    @SafeVarargs
    protected static <E> BiPredicate<E, E> combine(final BiPredicate<E, E>... predicates) {
        return Arrays.stream(predicates).reduce((a, o) -> true, BiPredicate::and);
    }

    @SafeVarargs
    public static <I> Comparator<I> comparators(final Function<I, ?>... functions) {
        return (o1, o2) -> Arrays.stream(functions)
                .map(Utils::comparableResultFunction)
                .map(f -> Comparator.comparing(f, Comparator.nullsFirst(Comparator.naturalOrder())))
                .reduce((l, r) -> 0, Comparator::thenComparing)
                .compare(o1, o2);
    }

    @SuppressWarnings("unchecked")
    private static <I, U extends Comparable<U>> Function<I, U> comparableResultFunction(final Function<I, ?> function) {
        return (i) -> (U) Optional
                .ofNullable(function.apply(i))
                .filter(Comparable.class::isInstance)
                .orElse(null);
    }

    public static <T> T first(Iterable<T> iterable) {
        return Streams.stream(iterable)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot retrieve first element of an empty iterable."));
    }

    private Utils() {
    }

}
