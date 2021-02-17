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
import org.assertj.neo4j.api.beta.type.Relationships;
import org.assertj.neo4j.api.beta.type.Relationships.DbRelationship;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 25/11/2020
 */
class ShouldRelationshipHaveTypeTests {

    @Nested
    class CreateTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final String expectedType = "TYPE";
            final DbRelationship relationship = Drivers.relation("BAD_TYPE").id(2).build();

            // WHEN
            final ShouldRelationshipHaveType error = ShouldRelationshipHaveType.create(relationship, expectedType);

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting relationship to have type:\n"
                    + " <\"TYPE\">\n"
                    + "but actual type is:\n"
                    + " <\"BAD_TYPE\">\n"
            );

        }
    }

    @Nested
    class ElementsTests {

        @Test
        void should_generate_an_aggregate_error_message() {
            // GIVEN
            final String expectedType = "TYPE";
            final DbRelationship relationship1 = Drivers.relation("TYPE").id(1).build();
            final DbRelationship relationship2 = Drivers.relation("OTHER_TYPE_1").id(2).build();
            final DbRelationship relationship3 = Drivers.relation("TYPE").id(3).build();
            final DbRelationship relationship4 = Drivers.relation("OTHER_TYPE_2").id(4).build();
            final DbRelationship relationship5 = Drivers.relation("OTHER_TYPE_3").id(5).build();
            final DbRelationship relationship6 = Drivers.relation("TYPE").id(6).build();
            final List<DbRelationship> relationships = Randomize
                    .listOf(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6);

            // WHEN
            final ErrorMessageFactory error = ShouldRelationshipHaveType
                    .elements(relationships, expectedType)
                    .notSatisfies(Randomize.listOf(relationship2, relationship4, relationship5));

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\n"
                    + "Expecting relationships:\n"
                    + "  <[RELATIONSHIP{id=1L, type='TYPE', start=null, end=null, properties={}},\n"
                    + "    RELATIONSHIP{id=2L, type='OTHER_TYPE_1', start=null, end=null, properties={}},\n"
                    + "    RELATIONSHIP{id=3L, type='TYPE', start=null, end=null, properties={}},\n"
                    + "    RELATIONSHIP{id=4L, type='OTHER_TYPE_2', start=null, end=null, properties={}},\n"
                    + "    RELATIONSHIP{id=5L, type='OTHER_TYPE_3', start=null, end=null, properties={}},\n"
                    + "    RELATIONSHIP{id=6L, type='TYPE', start=null, end=null, properties={}}]>\n"
                    + "to have type:\n"
                    + "  <\"TYPE\">\n"
                    + "but found other types for some relationships:\n"
                    + "\n"
                    + "  1) RELATIONSHIP{id=2L, type='OTHER_TYPE_1', start=null, end=null, properties={}}\n"
                    + "    - Actual type: \"OTHER_TYPE_1\"\n"
                    + "\n"
                    + "  2) RELATIONSHIP{id=4L, type='OTHER_TYPE_2', start=null, end=null, properties={}}\n"
                    + "    - Actual type: \"OTHER_TYPE_2\"\n"
                    + "\n"
                    + "  3) RELATIONSHIP{id=5L, type='OTHER_TYPE_3', start=null, end=null, properties={}}\n"
                    + "    - Actual type: \"OTHER_TYPE_3\"\n"
            );

        }
    }
}
