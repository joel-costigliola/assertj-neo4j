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

import org.assertj.core.util.Arrays;
import org.assertj.core.util.IterableUtil;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Streams;

import java.util.List;
import java.util.Objects;

/**
 * Extend the {@link org.assertj.core.util.Preconditions} which is a final class.
 *
 * @author Patrick Allain - 28/01/2021
 */
public final class Checks {

    /**
     * Check that an array is not null or empty.
     *
     * @param array   the array to check
     * @param message the exception message
     * @param <T>     the type of elements
     * @return a list containing all elements
     */
    public static <T> List<T> notNullOrEmpty(final T[] array, final String message) {
        if (Arrays.isNullOrEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
        return java.util.Arrays.asList(array);
    }

    /**
     * Check that an iterable is not null or empty.
     *
     * @param iterable the iterable to check
     * @param message  the exception message
     * @param <T>      the type of elements
     * @return a list containing all elements
     */
    public static <T> List<T> notNullOrEmpty(final Iterable<T> iterable, final String message) {
        if (IterableUtil.isNullOrEmpty(iterable)) {
            throw new IllegalArgumentException(message);
        }
        return Utils.listOf(iterable);
    }

    /**
     * Check that an array is not null or empty and check that it doesn't contains any null elements.
     *
     * @param array   the array to check
     * @param message the exception message
     * @param <T>     the type of elements
     * @return a list of all elements
     */
    public static <T> List<T> nonNullElementsIn(final T[] array, final String message) {
        return nonNullElementsIn(notNullOrEmpty(array, message), message);
    }

    /**
     * Check that an array is not null or empty and check that it doesn't contains any null elements.
     *
     * @param iterable the iterable to check
     * @param message  the exception message
     * @param <T>      the type of elements
     * @return a list of all elements
     */
    public static <T> List<T> nonNullElementsIn(final Iterable<T> iterable, final String message) {
        final List<T> list = notNullOrEmpty(iterable, message);
        if (list.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(message);
        }
        return list;
    }

    public static <T> T first(final Iterable<T> items, final String message) {
        return Streams.stream(items).findFirst().orElseThrow(() -> new IllegalArgumentException(message));
    }
}
