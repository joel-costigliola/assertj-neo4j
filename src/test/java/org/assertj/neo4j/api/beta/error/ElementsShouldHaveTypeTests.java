package org.assertj.neo4j.api.beta.error;

import org.assertj.core.api.Assertions;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Relationships;
import org.assertj.neo4j.api.beta.util.RelationshipTypes;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author patouche - 25/11/2020
 */
class ElementsShouldHaveTypeTests {

    @Test
    void create_single_relationships() {
        // GIVEN
        final String expectedType = "TYPE";
        final List<Relationships.DbRelationship> relationships = Arrays.asList(
                Drivers.relation("TYPE").id(1).build(),
                Drivers.relation("OTHER_TYPE").id(2).build(),
                Drivers.relation("TYPE").id(3).build()
        );
        final List<Relationships.DbRelationship> others = RelationshipTypes.others(expectedType, relationships);

        // WHEN
        final ElementsShouldHaveType error = ElementsShouldHaveType.create(relationships, expectedType, others);

        // THEN
        Assertions.assertThat(error.create()).isEqualToNormalizingNewlines(
                "\nExpecting relationships:\n"
                + "  <[\"RELATIONSHIP{id=1}\", \"RELATIONSHIP{id=2}\", \"RELATIONSHIP{id=3}\"]> to be of type:\n"
                + "  <\"TYPE\">\n"
                + "but some relationships have an other type:\n"
                + "\n"
                + "  - RELATIONSHIP{id=2} doesn't have the expected type:\n"
                + "    Actual: <OTHER_TYPE>\n"
                + "    Expected: <TYPE>"
        );

    }

    @Test
    void create_multiple_relationships() {
        // GIVEN
        final String expectedType = "TYPE";
        final List<Relationships.DbRelationship> relationships = Arrays.asList(
                Drivers.relation("TYPE").id(1).build(),
                Drivers.relation("OTHER_TYPE_2").id(2).build(),
                Drivers.relation("TYPE").id(3).build(),
                Drivers.relation("OTHER_TYPE_4").id(4).build(),
                Drivers.relation("OTHER_TYPE_5").id(5).build(),
                Drivers.relation("TYPE").id(6).build()
        );
        final List<Relationships.DbRelationship> others = RelationshipTypes.others(expectedType, relationships);

        // WHEN
        final ElementsShouldHaveType error = ElementsShouldHaveType.create(relationships, expectedType, others);

        // THEN
        Assertions.assertThat(error.create()).isEqualToNormalizingNewlines(
                "\nExpecting relationships:\n"
                + "  <[\"RELATIONSHIP{id=1}\",\n"
                + "    \"RELATIONSHIP{id=2}\",\n"
                + "    \"RELATIONSHIP{id=3}\",\n"
                + "    \"RELATIONSHIP{id=4}\",\n"
                + "    \"RELATIONSHIP{id=5}\",\n"
                + "    \"RELATIONSHIP{id=6}\"]> to be of type:\n"
                + "  <\"TYPE\">\n"
                + "but some relationships have an other type:\n"
                + "\n"
                + "  - RELATIONSHIP{id=2} doesn't have the expected type:\n"
                + "    Actual: <OTHER_TYPE_2>\n"
                + "    Expected: <TYPE>\n"
                + "\n"
                + "  - RELATIONSHIP{id=4} doesn't have the expected type:\n"
                + "    Actual: <OTHER_TYPE_4>\n"
                + "    Expected: <TYPE>\n"
                + "\n"
                + "  - RELATIONSHIP{id=5} doesn't have the expected type:\n"
                + "    Actual: <OTHER_TYPE_5>\n"
                + "    Expected: <TYPE>"
        );

    }
}
