package org.assertj.neo4j.api.beta.util;

import org.assertj.neo4j.api.beta.testing.Builders;
import org.assertj.neo4j.api.beta.testing.Samples;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.type.Nodes.DbNode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author patouche - 15/02/2021
 */
class NodeComparisonStrategyTests {

    public static final DbNode SAMPLE_NODE = Drivers.node().id(42)
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
            final DbNode node1 = Builders.rebuild(SAMPLE_NODE).build();
            final DbNode node2 = Builders.rebuild(SAMPLE_NODE).build();
            final NodeComparisonStrategy strategy = NodeComparisonStrategy.builder().build();

            // WHEN
            final boolean result = strategy.areEqual(node1, node2);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_true_when_ignore_ids() {
            // GIVEN
            final DbNode node1 = Builders.rebuild(SAMPLE_NODE).id(42).build();
            final DbNode node2 = Builders.rebuild(SAMPLE_NODE).id(69).build();
            final NodeComparisonStrategy strategy = NodeComparisonStrategy.builder().ignoreId(true).build();

            // WHEN
            final boolean result = strategy.areEqual(node1, node2);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_false_when_ignore_ids() {
            // GIVEN
            final DbNode node1 = Builders.rebuild(SAMPLE_NODE).id(42).build();
            final DbNode node2 = Builders.rebuild(SAMPLE_NODE).id(69).label("LBL_4").build();
            final NodeComparisonStrategy strategy = NodeComparisonStrategy.builder().ignoreId(true).build();

            // WHEN
            final boolean result = strategy.areEqual(node1, node2);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_false_when_ignore_properties() {
            // GIVEN
            final DbNode node1 = Builders.rebuild(SAMPLE_NODE).build();
            final DbNode node2 = Builders.rebuild(SAMPLE_NODE)
                    .property("prop-3", 21.14)
                    .property("prop-4", Samples.LOCAL_DATE.plusDays(1))
                    .build();
            final NodeComparisonStrategy strategy =
                    NodeComparisonStrategy.builder().ignoredProperties("prop-3").build();

            // WHEN
            final boolean result = strategy.areEqual(node1, node2);

            // THEN
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true_when_ignore_properties() {
            // GIVEN
            final DbNode node1 = Builders.rebuild(SAMPLE_NODE).build();
            final DbNode node2 = Builders.rebuild(SAMPLE_NODE)
                    .property("prop-3", 21.14)
                    .property("prop-4", Samples.LOCAL_DATE.plusDays(1))
                    .build();
            final NodeComparisonStrategy strategy = NodeComparisonStrategy.builder()
                    .ignoredProperties("prop-3", "prop-4")
                    .build();

            // WHEN
            final boolean result = strategy.areEqual(node1, node2);

            // THEN
            assertThat(result).isTrue();
        }

    }

}
