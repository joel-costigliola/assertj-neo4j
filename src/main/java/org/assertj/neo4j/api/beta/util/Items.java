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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author pallain - 14/11/2020
 */
public class Items {

    /**
     * List all the missing values of {@code items} that are in {@code searchedItems}.
     * <p/>
     * For example:
     * <pre><code class='java'>
     * // When using a String type.
     * List&lt;String&gt; items = Arrays.asList("item-1", "item-2", "item-3", "item-4");
     * List&lt;String&gt; searchedItems = Arrays.asList("item-1", "item-4", "other-item");
     *
     * // Output list will contains "item-2" and "item-3"
     * List&lt;String&gt; results = Lists.missing(items, searchedItems);
     * </code></pre>
     *
     * @param items         the items
     * @param searchedItems the searched items
     * @param <T>           the type of items in both list
     * @return the missing values that appears in {@code searchedItems} but not in {@code items}
     */
    public static <T> List<T> missing(final Iterable<T> items, final Iterable<T> searchedItems) {
        return Streams.stream(searchedItems)
                .filter(si -> Streams.stream(items).noneMatch(i -> Objects.equals(si, i)))
                .collect(Collectors.toList());
    }

}
