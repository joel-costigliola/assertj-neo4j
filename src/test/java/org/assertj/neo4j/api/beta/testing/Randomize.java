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
package org.assertj.neo4j.api.beta.testing;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Patrick Allain - 07/02/2021
 */
public class Randomize {

    public static final SecureRandom SECURE_RANDOM = new SecureRandom(new SecureRandom().generateSeed(128));

    public static <T> Comparator<T> comparator() {
        return (o1, o2) -> SECURE_RANDOM.nextInt(3) - 1;
    }

    public static <T> List<T> listOf(T... items) {
        return Arrays.stream(items).sorted(Randomize.comparator()).collect(Collectors.toList());
    }
}
