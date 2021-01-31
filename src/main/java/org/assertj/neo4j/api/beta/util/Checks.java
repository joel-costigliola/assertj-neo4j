package org.assertj.neo4j.api.beta.util;

import org.assertj.core.util.Arrays;
import org.assertj.core.util.IterableUtil;
import org.assertj.core.util.Lists;

import java.util.List;

/**
 * @author patouche - 28/01/2021
 */
public final class Checks {

    public static <T> List<T> notNullOrEmpty(final T[] items, final String message) {
        if (Arrays.isNullOrEmpty(items)) {
            throw new IllegalArgumentException(message);
        }
        return java.util.Arrays.asList(items);
    }

    public static <T> List<T> notNullOrEmpty(final Iterable<T> items, final String message) {
        if (IterableUtil.isNullOrEmpty(items)) {
            throw new IllegalArgumentException(message);
        }
        return Lists.newArrayList(items);
    }
}
