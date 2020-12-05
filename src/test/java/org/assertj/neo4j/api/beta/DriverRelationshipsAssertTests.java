package org.assertj.neo4j.api.beta;

import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Relationships;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * @author pallain - 24/11/2020
 */
class DriverRelationshipsAssertTests {

    @Nested
    @DisplayName("ignoringIds")
    class IgnoringIdsTests {

        @Test
        void should_return_a_list_of_nodes_without_ids() {
            // GIVEN
            final List<Relationships.DbRelationship> relationships = Arrays.asList(
                    Drivers.relation("KNOWS").id(22).build(),
                    Drivers.relation("KNOWS").id(56).build()
            );
            final DriverRelationshipsAssert relationshipsAssert = new DriverRelationshipsAssert(relationships, null,
                                                                                                null);

            // WHEN
            final DriverRelationshipsAssert result = relationshipsAssert.ignoringIds();

            // THEN
            assertThat(result.getActual())
                    .extracting(DbEntity::getId)
                    .containsOnlyNulls();
        }

    }

    @Nested
    @DisplayName("ignoringIds")
    class HaveTypeTests {

        @Test
        void should_pass() {
            // GIVEN
            final List<Relationships.DbRelationship> relationships = Arrays.asList(
                    Drivers.relation("KNOWS").id(22).build(),
                    Drivers.relation("KNOWS").id(56).build()
            );
            final DriverRelationshipsAssert relationshipsAssert = new DriverRelationshipsAssert(relationships, null,
                                                                                                null);

            // WHEN
            final DriverRelationshipsAssert result = relationshipsAssert.haveType("KNOWS");

            // THEN
            assertThat(result).isSameAs(relationshipsAssert);
        }

        @Test
        void should_fail() {
            // GIVEN
            final List<Relationships.DbRelationship> relationships = Arrays.asList(
                    Drivers.relation("KNOWS").id(22).build(),
                    Drivers.relation("KNOWS").id(56).build()
            );
            final DriverRelationshipsAssert relationshipsAssert = new DriverRelationshipsAssert(
                    relationships, null, null
            );

            // WHEN
            final Throwable throwable = catchThrowable(() -> relationshipsAssert.haveType("OTHER_TYPE"));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll("Expecting relationships:", "but some relationships have an other type:");
        }

    }

}
