package org.assertj.neo4j.api.beta.error;

import org.assertj.core.api.Assertions;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.util.Entities;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author patouhe - 26/11/2020
 */
class ElementsShouldHavePropertyKeysTests {

    @Test
    void create_single_node() {
        // GIVEN
        final List<String> expectedKeys = Arrays.asList("k-1", "k-2", "k-3");
        final List<Nodes.DbNode> nodes = Arrays.asList(
                Drivers.node().id(1).property("k-1", "v-1").property("k-2", "v-2").property("k-3", "v-3").build(),
                Drivers.node().id(2).property("k-1", "v-1").property("k-2", "v-2").build(),
                Drivers.node().id(3).property("k-1", "v-1").property("k-2", "v-2").property("k-3", "v-3").build()
        );
        final List<Missing<Nodes.DbNode, String>> others = Entities.missingPropertyKeys(nodes, expectedKeys);

        // WHEN
        final ElementsShouldHavePropertyKeys<?> error = ElementsShouldHavePropertyKeys.create(
                RecordType.NODE, nodes, expectedKeys, others
        );

        // THEN
        Assertions.assertThat(error.create()).isEqualToNormalizingNewlines(
                "\nExpecting nodes:\n"
                + "  <[\"NODE{id=1}\", \"NODE{id=2}\", \"NODE{id=3}\"]> to have all the following property keys:\n"
                + "  <[\"k-1\", \"k-2\", \"k-3\"]>\n"
                + "but some property keys were missing on:\n"
                + "\n"
                + "  - NODE{id=2} have missing property keys: [k-3]\n"
                + "    Actual: <[k-1, k-2]>\n"
                + "    Expected: <[k-1, k-2, k-3]>"
        );

    }

    @Test
    void create_multiple_nodes() {
        // GIVEN
        final List<String> expectedKeys = Arrays.asList("k-1", "k-2", "k-3");
        final List<Nodes.DbNode> nodes = Arrays.asList(
                Drivers.node().id(1).property("k-3", "v-3").property("k-2", "v-2").build(),
                Drivers.node().id(2).property("k-3", "v-3").property("k-1", "v-1").build(),
                Drivers.node().id(3).property("k-2", "v-2").property("k-1", "v-1").build(),
                Drivers.node().id(4).property("k-3", "v-3").build()
        );
        final List<Missing<Nodes.DbNode, String>> others = Entities.missingPropertyKeys(nodes, expectedKeys);

        // WHEN
        final ElementsShouldHavePropertyKeys<?> error = ElementsShouldHavePropertyKeys.create(
                RecordType.NODE, nodes, expectedKeys, others
        );

        // THEN
        Assertions.assertThat(error.create()).isEqualToNormalizingNewlines(
                "\nExpecting nodes:\n"
                + "  <[\"NODE{id=1}\", \"NODE{id=2}\", \"NODE{id=3}\", \"NODE{id=4}\"]> to have all the following property keys:\n"
                + "  <[\"k-1\", \"k-2\", \"k-3\"]>\n"
                + "but some property keys were missing on:\n"
                + "\n"
                + "  - NODE{id=1} have missing property keys: [k-1]\n"
                + "    Actual: <[k-2, k-3]>\n"
                + "    Expected: <[k-1, k-2, k-3]>\n"
                + "\n"
                + "  - NODE{id=2} have missing property keys: [k-2]\n"
                + "    Actual: <[k-1, k-3]>\n"
                + "    Expected: <[k-1, k-2, k-3]>\n"
                + "\n"
                + "  - NODE{id=3} have missing property keys: [k-3]\n"
                + "    Actual: <[k-1, k-2]>\n"
                + "    Expected: <[k-1, k-2, k-3]>\n"
                + "\n"
                + "  - NODE{id=4} have missing property keys: [k-1, k-2]\n"
                + "    Actual: <[k-3]>\n"
                + "    Expected: <[k-1, k-2, k-3]>"
        );

    }
}
