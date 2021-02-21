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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author patouche - 20/02/2021
 */
@Disabled("ASSERTJ IMPROVEMENTS")
class AssertJTests {

    @Test
    void iterable_bad_name() {
        final Set<String> actual = Stream.of("toto", "tata").collect(Collectors.toSet());
        Assertions.assertThat(actual).contains("tutu");
    }
}