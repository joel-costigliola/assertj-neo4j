package org.assertj.neo4j.api.beta.error;

import org.assertj.core.api.Assertions;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Values;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author patouche - 31/01/2021
 */
class ElementsShouldHavePropertyInstanceOfTests {
    @Test
    void single_entities_error() {
        // GIVEN
        final ValueType expectedType = ValueType.STRING;
        final List<Nodes.DbNode> actual = Arrays.asList(
                Drivers.node().id(1).property("prop-key", "value-1").build(),
                Drivers.node().id(2).property("prop-key", LocalDateTime.now()).build(),
                Drivers.node().id(3).property("prop-key", "value-3").build(),
                Drivers.node().id(4).property("prop-key", "value-4").build()
        );

        // WHEN
        final ErrorMessageFactory result = ElementsShouldHavePropertyInstanceOf
                .create(RecordType.NODE, actual, "toto", String.class);

        // THEN
        Assertions.assertThat(result.create()).isEqualToNormalizingNewlines(
                "todo"
        );
    }

}
