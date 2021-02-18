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
import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 14/02/2021
 */
class EntityComparisonStrategyTests {

    @Nested
    class CompositeTests {

        @Test
        void should_return_true_when_node_without_id_comparison() {
            // GIVEN
            final DbNode obj1 = Builders.rebuild(Samples.NODE).id(22).build();
            final DbNode obj2 = Builders.rebuild(Samples.NODE).id(29).build();
            final NodeComparisonStrategy s1 = NodeComparisonStrategy.builder().ignoreId(true).build();
            final RelationshipComparisonStrategy s2 = RelationshipComparisonStrategy.builder().ignoreId(false).build();

            // WHEN
            final boolean result = EntityComparisonStrategy.composite(s1, s2).areEqual(obj1, obj2);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_true_when_relationship_without_id_comparison() {
            // GIVEN
            final DbRelationship obj1 = Builders.rebuild(Samples.RELATIONSHIP).id(22).build();
            final DbRelationship obj2 = Builders.rebuild(Samples.RELATIONSHIP).id(29).build();
            final NodeComparisonStrategy s1 = NodeComparisonStrategy.builder().ignoreId(false).build();
            final RelationshipComparisonStrategy s2 = RelationshipComparisonStrategy.builder().ignoreId(true).build();

            // WHEN
            final boolean result = EntityComparisonStrategy.composite(s1, s2).areEqual(obj1, obj2);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_false_when_node_without_id_comparison() {
            // GIVEN
            final DbNode obj1 = Builders.rebuild(Samples.NODE).id(22).labels("OTHER").build();
            final DbNode obj2 = Builders.rebuild(Samples.NODE).id(29).build();
            final NodeComparisonStrategy s1 = NodeComparisonStrategy.builder().ignoreId(false).build();
            final RelationshipComparisonStrategy s2 = RelationshipComparisonStrategy.builder().ignoreId(true).build();

            // WHEN
            final boolean result = EntityComparisonStrategy.composite(s1, s2).areEqual(obj1, obj2);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true_when_fallback_to_standard_strategy() {
            // GIVEN
            final String obj1 = "TEST";
            final String obj2 = "TEST";
            final NodeComparisonStrategy s1 = NodeComparisonStrategy.builder().ignoreId(false).build();
            final RelationshipComparisonStrategy s2 = RelationshipComparisonStrategy.builder().ignoreId(true).build();

            // WHEN
            final boolean result = EntityComparisonStrategy.composite(s1, s2).areEqual(obj1, obj2);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_false_when_fallback_to_standard_strategy() {
            // GIVEN
            final String obj1 = "TEST-1";
            final String obj2 = "TEST-2";
            final NodeComparisonStrategy s1 = NodeComparisonStrategy.builder().ignoreId(false).build();
            final RelationshipComparisonStrategy s2 = RelationshipComparisonStrategy.builder().ignoreId(true).build();

            // WHEN
            final boolean result = EntityComparisonStrategy.composite(s1, s2).areEqual(obj1, obj2);

            // THEN
            assertThat(result).isFalse();
        }
    }

}
