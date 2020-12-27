package org.assertj.neo4j.api.beta.error;

import org.assertj.core.api.Assertions;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

/**
 * @author patouche - 27/12/2020
 */
class ElementsShouldHavePropertyListOfTypeTests {

    @Test
    void create_single_node() {
        // GIVEN
        final List<Nodes.DbNode> nodes = asList(
                Drivers.node().id(1)
                        .property("key", emptyList())
                        .build(),
                Drivers.node().id(2)
                        .property("key", singletonList("v-2.1.1"))
                        .build(),
                Drivers.node().id(3)
                        .property("key", asList(3.11, 3.12))
                        .build()
        );

        // WHEN
        final ErrorMessageFactory error = ElementsShouldHavePropertyListOfType.create(
                RecordType.NODE, nodes, "key", ValueType.INTEGER
        );

        // THEN
        Assertions.assertThat(error.create()).isEqualToNormalizingNewlines(
                "TOTO"
        );

    }

    @Test
    void create_multiple_nodes() {
        // GIVEN
        final List<Nodes.DbNode> nodes = asList(
                Drivers.node().id(1)
                        .property("key", emptyList())
                        .build(),
                Drivers.node().id(2)
                        .property("key", singletonList("v-2.1.1"))
                        .build(),
                Drivers.node().id(3)
                        .property("key", asList(3.11, 3.12))
                        .build(),
                Drivers.node().id(4)
                        .property("key", asList(4.11, 4.12, 4.13))
                        .build()
        );

        // WHEN
        final ErrorMessageFactory error = ElementsShouldHavePropertyListOfType.create(
                RecordType.NODE, nodes, "key", ValueType.STRING
        );

        // THEN
        Assertions.assertThat(error.create()).isEqualToNormalizingNewlines(
                "TODO"
        );

    }
}
