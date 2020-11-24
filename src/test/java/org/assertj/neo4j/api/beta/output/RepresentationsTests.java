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
package org.assertj.neo4j.api.beta.output;

import org.assertj.core.api.Assertions;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author pallain - 14/11/2020
 */
public class RepresentationsTests {

    public static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Test
    void id_shouldReturnTheTypeWithItsId() {
        // GIVEN
        final Nodes.DbNode entity = Nodes.node().id(42).label("TEST").property("prop", "value").build();

        // WHEN
        final String representation = Representations.id(entity);

        // THEN
        Assertions.assertThat(representation).isEqualTo("NODE{id=42}");
    }

    @Test
    void ids_shouldReturnAListOfSortedIds() {
        // GIVEN
        final List<Nodes.DbNode> entities = IntStream.range(0, 10)
                .mapToObj(i -> Nodes.node().id(i).label("TEST").property("prop", "value").build())
                .sorted((o1, o2) -> 1 - SECURE_RANDOM.nextInt(2))
                .collect(Collectors.toList());

        // WHEN
        final List<String> representation = Representations.ids(entities);

        // THEN
        Assertions.assertThat(representation).containsExactly(
                "NODE{id=0}",
                "NODE{id=1}",
                "NODE{id=2}",
                "NODE{id=3}",
                "NODE{id=4}",
                "NODE{id=5}",
                "NODE{id=6}",
                "NODE{id=7}",
                "NODE{id=8}",
                "NODE{id=9}"
        );
    }
}
