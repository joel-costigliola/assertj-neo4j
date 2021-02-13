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
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.type.Relationships;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
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
    public static <T> List<T> listOf(final T[] array) {
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

    public static List<String> sorted(final Iterable<String> items) {
        return Streams.stream(items).sorted().collect(Collectors.toList());
    }

    public static String title(final String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private Utils() {
    }
}
