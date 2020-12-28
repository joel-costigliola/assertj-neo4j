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

class ElementsShouldHavePropertyWithTypeTests {

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
        final ErrorMessageFactory result = ElementsShouldHavePropertyWithType
                .create(RecordType.NODE, actual, "prop-key", expectedType);

        // THEN
        Assertions.assertThat(result.create()).isEqualToNormalizingNewlines(
                "\nExpecting nodes:\n"
                + "  [\"NODE{id=1}\", \"NODE{id=2}\", \"NODE{id=3}\", \"NODE{id=4}\"]\n"
                + "to have property \"prop-key\" with type:\n"
                + "  STRING\n"
                + "but some nodes have for the property \"prop-key\" another type:\n"
                + "\n"
                + "  - NODE{id=2} have property \"prop-key\" of type:\n"
                + "      Actual  : LOCAL_DATE_TIME\n"
                + "      Expected: STRING"
        );
    }

    @Test
    void multiple_entities_error() {
        // GIVEN
        final ValueType expectedType = ValueType.STRING;
        final List<Nodes.DbNode> actual = Arrays.asList(
                Drivers.node().id(1).property("prop-key", "value-1").build(),
                Drivers.node().id(2).property("prop-key", LocalDateTime.now()).build(),
                Drivers.node().id(3).property("prop-key", 1).build(),
                Drivers.node().id(4).property("prop-key", Values.point(1, 22.29, 56.35).asObject()).build(),
                Drivers.node().id(4).property("prop-key", ZonedDateTime.now()).build(),
                Drivers.node().id(4).property("prop-key", true).build()
        );
        final List<Nodes.DbNode> items = actual.stream()
                .filter(e -> e.getPropertyType("prop-key") != expectedType)
                .collect(Collectors.toList());

        // WHEN
        final ErrorMessageFactory result = ElementsShouldHavePropertyWithType
                .create(RecordType.NODE, actual, "prop-key", expectedType);

        // THEN
        Assertions.assertThat(result.create()).isEqualToNormalizingNewlines(
                "\nExpecting nodes:\n"
                + "  [\"NODE{id=1}\",\n"
                + "    \"NODE{id=2}\",\n"
                + "    \"NODE{id=3}\",\n"
                + "    \"NODE{id=4}\",\n"
                + "    \"NODE{id=4}\",\n"
                + "    \"NODE{id=4}\"]\n"
                + "to have property \"prop-key\" with type:\n"
                + "  STRING\n"
                + "but some nodes have for the property \"prop-key\" another type:\n"
                + "\n"
                + "  - NODE{id=2} have property \"prop-key\" of type:\n"
                + "      Actual  : LOCAL_DATE_TIME\n"
                + "      Expected: STRING\n"
                + "\n"
                + "  - NODE{id=3} have property \"prop-key\" of type:\n"
                + "      Actual  : INTEGER\n"
                + "      Expected: STRING\n"
                + "\n"
                + "  - NODE{id=4} have property \"prop-key\" of type:\n"
                + "      Actual  : POINT\n"
                + "      Expected: STRING\n"
                + "\n"
                + "  - NODE{id=4} have property \"prop-key\" of type:\n"
                + "      Actual  : DATE_TIME\n"
                + "      Expected: STRING\n"
                + "\n"
                + "  - NODE{id=4} have property \"prop-key\" of type:\n"
                + "      Actual  : BOOLEAN\n"
                + "      Expected: STRING"
        );
    }
}
