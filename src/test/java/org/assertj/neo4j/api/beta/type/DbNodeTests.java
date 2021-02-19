package org.assertj.neo4j.api.beta.type;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author patouche - 19/02/2021
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
