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

import org.assertj.neo4j.api.beta.testing.Builders;
import org.assertj.neo4j.api.beta.testing.Samples;
import org.assertj.neo4j.api.beta.type.DbNode;
import org.assertj.neo4j.api.beta.type.Models;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.neo4j.api.beta.util.NodeComparisonStrategy.builder;

/**
 * @author Patrick Allain - 15/02/2021
 */
class NodeComparisonStrategyTests {

    private static final DbNode SAMPLE_NODE = Models.node()
            .id(22)
            .labels("LBL_1", "LBL_2", "LBL_3")
            .property("prop-1", "val-1")
            .property("prop-2", 2)
            .property("prop-3", 3.14)
            .property("prop-4", Samples.LOCAL_DATE)
            .build();

    @Nested
    class AreEqualTests {

        @Test
        void should_return_true() {
            // GIVEN
            final DbNode other = Builders.rebuild(SAMPLE_NODE).build();
            final NodeComparisonStrategy strategy = builder().build();

            // WHEN
            final boolean result = strategy.areEqual(SAMPLE_NODE, other);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_true_when_ignore_ids() {
            // GIVEN
            final DbNode other = Builders.rebuild(SAMPLE_NODE).id(42).build();
            final NodeComparisonStrategy strategy = builder().ignoreId(true).build();

            // WHEN
            final boolean result = strategy.areEqual(SAMPLE_NODE, other);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_false_when_ignore_ids() {
            // GIVEN
            final DbNode other = Builders.rebuild(SAMPLE_NODE).id(42).label("LBL_4").build();
            final NodeComparisonStrategy strategy = builder().ignoreId(true).build();

            // WHEN
            final boolean result = strategy.areEqual(SAMPLE_NODE, other);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_false_when_ignore_properties() {
            // GIVEN
            final DbNode other = Builders.rebuild(SAMPLE_NODE)
                    .property("prop-3", 21.14)
                    .property("prop-4", Samples.LOCAL_DATE.plusDays(1))
                    .build();
            final NodeComparisonStrategy strategy = builder().ignoreProperties("prop-3").build();

            // WHEN
            final boolean result = strategy.areEqual(SAMPLE_NODE, other);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true_when_ignore_properties() {
            // GIVEN
            final DbNode other = Builders.rebuild(SAMPLE_NODE)
                    .property("prop-3", 21.14)
                    .property("prop-4", Samples.LOCAL_DATE.plusDays(1))
                    .build();
            final NodeComparisonStrategy strategy = builder().ignoreProperties("prop-3", "prop-4").build();

            // WHEN
            final boolean result = strategy.areEqual(SAMPLE_NODE, other);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_false_when_ignore_labels() {
            // GIVEN
            final DbNode other = Builders.rebuild(SAMPLE_NODE).labels("IGN_LBL", "NO_IGN_LBL").build();
            final NodeComparisonStrategy strategy = builder().ignoreLabels("IGN_LBL").build();

            // WHEN
            final boolean result = strategy.areEqual(SAMPLE_NODE, other);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true_when_ignore_labels() {
            // GIVEN
            final DbNode other = Builders.rebuild(SAMPLE_NODE).labels("IGN_LBL").build();
            final NodeComparisonStrategy strategy = builder().ignoreLabels("IGN_LBL").build();

            // WHEN
            final boolean result = strategy.areEqual(SAMPLE_NODE, other);

            // THEN
            assertThat(result).isTrue();
        }

    }

}
