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
import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.assertj.neo4j.api.beta.type.Models;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.neo4j.api.beta.util.RelationshipComparisonStrategy.builder;

/**
 * @author patouche - 17/02/2021
 */
class RelationshipComparisonStrategyTests {

    public static final DbRelationship SAMPLE_RELATIONSHIP = Models.relation()
            .id(22)
            .type("TYPE")
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
            final DbRelationship other = Builders.rebuild(SAMPLE_RELATIONSHIP).build();
            final RelationshipComparisonStrategy strategy = builder().build();

            // WHEN
            final boolean result = strategy.areEqual(SAMPLE_RELATIONSHIP, other);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_true_when_ignore_ids() {
            // GIVEN
            final DbRelationship other = Builders.rebuild(SAMPLE_RELATIONSHIP).id(42).build();
            final RelationshipComparisonStrategy strategy = builder().ignoreId(true).build();

            // WHEN
            final boolean result = strategy.areEqual(SAMPLE_RELATIONSHIP, other);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_false_when_ignore_ids() {
            // GIVEN
            final DbRelationship other = Builders.rebuild(SAMPLE_RELATIONSHIP).id(42).type("OTHER_TYPE").build();
            final RelationshipComparisonStrategy strategy = builder().ignoreId(true).build();

            // WHEN
            final boolean result = strategy.areEqual(SAMPLE_RELATIONSHIP, other);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_false_when_ignore_properties() {
            // GIVEN
            final DbRelationship other = Builders.rebuild(SAMPLE_RELATIONSHIP)
                    .property("prop-3", 21.14)
                    .property("prop-4", Samples.LOCAL_DATE.plusDays(1))
                    .build();
            final RelationshipComparisonStrategy strategy =
                    builder().ignoreProperties("prop-3").build();

            // WHEN
            final boolean result = strategy.areEqual(SAMPLE_RELATIONSHIP, other);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true_when_ignore_properties() {
            // GIVEN
            final DbRelationship other = Builders.rebuild(SAMPLE_RELATIONSHIP)
                    .property("prop-3", 21.14)
                    .property("prop-4", Samples.LOCAL_DATE.plusDays(1))
                    .build();
            final RelationshipComparisonStrategy strategy = builder().ignoreProperties("prop-3", "prop-4").build();

            // WHEN
            final boolean result = strategy.areEqual(SAMPLE_RELATIONSHIP, other);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_false_when_ignore_type() {
            // GIVEN
            final DbRelationship other = Builders.rebuild(SAMPLE_RELATIONSHIP).id(42).type("OTHER_TYPE").build();
            final RelationshipComparisonStrategy strategy = builder().ignoreType(true).build();

            // WHEN
            final boolean result = strategy.areEqual(SAMPLE_RELATIONSHIP, other);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true_when_ignore_type() {
            // GIVEN
            final DbRelationship other = Builders.rebuild(SAMPLE_RELATIONSHIP).type("OTHER_TYPE").build();
            final RelationshipComparisonStrategy strategy = builder().ignoreType(true).build();

            // WHEN
            final boolean result = strategy.areEqual(SAMPLE_RELATIONSHIP, other);

            // THEN
            assertThat(result).isTrue();
        }

    }
}
