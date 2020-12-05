package org.assertj.neo4j.api.beta.util;

import org.assertj.core.api.Assertions;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Relationships.DbRelationship;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author patouche - 25/11/2020
 */
class RelationshipTypesTests {

    private static final String SAMPLE_TYPE = "TYPE";

    private static final DbRelationship SAMPLE_TYPE_RELATIONSHIP = Drivers.relation(SAMPLE_TYPE).build();

    private static final DbRelationship SAMPLE_OTHER_RELATIONSHIP = Drivers.relation("OTHER").build();

    @Nested
    class IsTests {

        @Test
        void should_fail_with_NullPointerException() {
            // WHEN
            final Throwable throwable = Assertions.catchThrowable(() -> RelationshipTypes.is(null,
                                                                                             SAMPLE_TYPE_RELATIONSHIP));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Type should not be null or empty");
        }

        @Test
        void should_fail_with_IllegalArgumentException() {
            // WHEN
            final Throwable throwable = Assertions.catchThrowable(() -> RelationshipTypes.is("",
                                                                                             SAMPLE_TYPE_RELATIONSHIP));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Type should not be null or empty");
        }

        @Test
        void should_return_true() {
            // WHEN
            final boolean result = RelationshipTypes.is(SAMPLE_TYPE, SAMPLE_TYPE_RELATIONSHIP);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_false() {
            // WHEN
            final boolean result = RelationshipTypes.is(SAMPLE_TYPE, SAMPLE_OTHER_RELATIONSHIP);

            // THEN
            assertThat(result).isFalse();
        }
    }

    @Nested
    class IsNotTests {

        @Test
        void should_return_true() {
            // WHEN
            final boolean result = RelationshipTypes.isNot(SAMPLE_TYPE, SAMPLE_OTHER_RELATIONSHIP);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_false() {
            // WHEN
            final boolean result = RelationshipTypes.isNot(SAMPLE_TYPE, SAMPLE_TYPE_RELATIONSHIP);

            // THEN
            assertThat(result).isFalse();
        }
    }

    @Nested
    class AreTests {

        @Test
        void should_return_true() {
            // GIVEN
            final List<DbRelationship> relationships = Arrays.asList(
                    Drivers.relation("TYPE").build(),
                    Drivers.relation("TYPE").build(),
                    Drivers.relation("TYPE").build(),
                    Drivers.relation("TYPE").build()
            );

            // WHEN
            final boolean result = RelationshipTypes.are("TYPE", relationships);

            // THEN
            assertThat(result).isTrue();
        }

        @Test
        void should_return_false() {
            // GIVEN
            final List<DbRelationship> relationships = Arrays.asList(
                    Drivers.relation("TYPE").build(),
                    Drivers.relation("TYPE").build(),
                    Drivers.relation("OTHER_TYPE").build(),
                    Drivers.relation("TYPE").build()
            );

            // WHEN
            final boolean result = RelationshipTypes.are("TYPE", relationships);

            // THEN
            assertThat(result).isFalse();
        }
    }

    @Nested
    class OthersTests {

        @Test
        void should_return_all_relationships_having_an_other_type() {
            // GIVEN
            final List<DbRelationship> relationships = Arrays.asList(
                    Drivers.relation("TYPE").id(1).build(),
                    Drivers.relation("OTHER").id(2).build(),
                    Drivers.relation("TYPE").id(3).build(),
                    Drivers.relation("OTHER_2").id(4).build()
            );

            // WHEN
            final List<DbRelationship> result = RelationshipTypes.others("TYPE", relationships);

            // THEN
            assertThat(result)
                    .hasSize(2)
                    .contains(
                            Drivers.relation("OTHER").id(2).build(),
                            Drivers.relation("OTHER_2").id(4).build()
                    );
        }
    }

}
