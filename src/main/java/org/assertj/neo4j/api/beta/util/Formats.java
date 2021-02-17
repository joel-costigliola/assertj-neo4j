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

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author patouche - 16/02/2021
 */
public class Formats {

    public static String number(final Long number) {
        return number == null ? "null" : String.format("%dL", number);
    }

    public static String string(final String str) {
        return String.format("\"%s\"", str);
    }

    public static String strings(final Iterable<String> iterable) {
        final List<String> values = IterableUtil.toCollection(iterable).stream()
                .map(Formats::string)
                .collect(Collectors.toList());
        return String.valueOf(values);
    }

    public static String title(final String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
