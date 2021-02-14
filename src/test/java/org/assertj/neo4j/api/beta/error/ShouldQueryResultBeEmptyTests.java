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

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.neo4j.api.beta.testing.Randomize;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.Nodes.DbNode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Query;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author patouche - 13/02/2021
 */
class ShouldQueryResultBeEmptyTests {

    @Nested
    class CreateTests {

        @Test
        void should_generate_an_aggregate_error_message() {
            // GIVEN
            final Query query = new Query("MATCH (n) RETURN n");
            final DbNode node1 = Drivers.node().id(22).build();
            final DbNode node2 = Drivers.node().id(29).build();
            final DbNode node3 = Drivers.node().id(35).build();
            final DbNode node4 = Drivers.node().id(56).build();
            final List<DbNode> actual = Randomize.listOf(node1, node2, node3, node4);

            // WHEN
            final ErrorMessageFactory error = ShouldQueryResultBeEmpty.create(actual, query);

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\n"
                    + "Expecting query:\n"
                    + "  <Query{text='MATCH (n) RETURN n', parameters={}}>\n"
                    + "to return an empty list of nodes got:\n"
                    + "  <[\"NODE{id=22}\", \"NODE{id=29}\", \"NODE{id=35}\", \"NODE{id=56}\"]>"
            );
        }
    }
}
