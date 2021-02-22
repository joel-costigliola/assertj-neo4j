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
package org.assertj.neo4j.api.beta.type;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 19/02/2021
 */
class DbNodeTests {

    @Nested
    class ComparableTests {

        @Test
        void should_return_a_negative_value() {
            // GIVEN
            final DbNode node1 = Models.node().id(11).build();
            final DbNode node2 = Models.node().id(16).build();

            // WHEN
            final int result = node1.compareTo(node2);

            // THEN
            assertThat(result).isLessThan(0);
        }

        @Test
        void should_return_a_positive_value() {
            // GIVEN
            final DbNode node1 = Models.node().id(42).build();
            final DbNode node2 = Models.node().id(16).build();

            // WHEN
            final int result = node1.compareTo(node2);

            // THEN
            assertThat(result).isGreaterThan(0);
        }

    }
}
