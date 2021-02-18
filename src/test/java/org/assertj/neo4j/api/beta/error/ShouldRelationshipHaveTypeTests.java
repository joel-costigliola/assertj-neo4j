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
import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.assertj.neo4j.api.beta.type.Models;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 25/11/2020
 */
class ShouldRelationshipHaveTypeTests {

    @Nested
    @DisplayName("#create(relationship, type)")
    class CreateSingleTypeTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final String type = "TYPE";
            final DbRelationship relationship = Models.relation("BAD_TYPE").id(2).build();

            // WHEN
            final ShouldRelationshipHaveType error = ShouldRelationshipHaveType.create(relationship, type);

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting relationship to have type:\n"
                    + "  <\"TYPE\">\n"
                    + "but actual type is:\n"
                    + "  <\"BAD_TYPE\">\n"
            );

        }
    }

    @Nested
    @DisplayName("#elements(relationships, type)")
    class ElementsSingleTypeTests {

        @Test
        void should_generate_an_aggregate_error_message() {
            // GIVEN
            final String type = "TYPE";
            final DbRelationship relationship1 = Models.relation("TYPE").id(1).build();
            final DbRelationship relationship2 = Models.relation("OTHER_TYPE_1").id(2).build();
            final DbRelationship relationship3 = Models.relation("TYPE").id(3).build();
            final DbRelationship relationship4 = Models.relation("OTHER_TYPE_2").id(4).build();
            final DbRelationship relationship5 = Models.relation("OTHER_TYPE_3").id(5).build();
            final DbRelationship relationship6 = Models.relation("TYPE").id(6).build();
            final List<DbRelationship> relationships = Randomize
                    .listOf(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6);

            // WHEN
            final ErrorMessageFactory error = ShouldRelationshipHaveType
                    .elements(relationships, type)
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

    @Nested
    @DisplayName("#create(relationship, types)")
    class CreateMultipleTypesTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final List<String> types = Randomize.listOf("TYPE_1", "TYPE_2", "TYPE_3");
            final DbRelationship relationship = Models.relation("BAD_TYPE").id(2).build();

            // WHEN
            final ShouldRelationshipHaveType error = ShouldRelationshipHaveType.create(relationship, types);

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting relationship to have type in:\n"
                    + "  <[\"TYPE_1\", \"TYPE_2\", \"TYPE_3\"]>\n"
                    + "but actual type is:\n"
                    + "  <\"BAD_TYPE\">\n"
            );

        }
    }

    @Nested
    @DisplayName("#elements(relationships, types)")
    class ElementsMultipleTypesTests {

        @Test
        void should_generate_an_aggregate_error_message() {
            // GIVEN
            final List<String> types = Randomize.listOf("TYPE_1", "TYPE_2", "TYPE_3");
            final DbRelationship relationship1 = Models.relation("TYPE_1").id(1).build();
            final DbRelationship relationship2 = Models.relation("OTHER_TYPE_1").id(2).build();
            final DbRelationship relationship3 = Models.relation("TYPE_2").id(3).build();
            final DbRelationship relationship4 = Models.relation("OTHER_TYPE_2").id(4).build();
            final DbRelationship relationship5 = Models.relation("OTHER_TYPE_3").id(5).build();
            final DbRelationship relationship6 = Models.relation("TYPE_3").id(6).build();
            final List<DbRelationship> relationships = Randomize
                    .listOf(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6);

            // WHEN
            final ErrorMessageFactory error = ShouldRelationshipHaveType
                    .elements(relationships, types)
                    .notSatisfies(Randomize.listOf(relationship2, relationship4, relationship5));

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\n"
                    + "Expecting relationships:\n"
                    + "  <[RELATIONSHIP{id=1L, type='TYPE_1', start=null, end=null, properties={}},\n"
                    + "    RELATIONSHIP{id=2L, type='OTHER_TYPE_1', start=null, end=null, properties={}},\n"
                    + "    RELATIONSHIP{id=3L, type='TYPE_2', start=null, end=null, properties={}},\n"
                    + "    RELATIONSHIP{id=4L, type='OTHER_TYPE_2', start=null, end=null, properties={}},\n"
                    + "    RELATIONSHIP{id=5L, type='OTHER_TYPE_3', start=null, end=null, properties={}},\n"
                    + "    RELATIONSHIP{id=6L, type='TYPE_3', start=null, end=null, properties={}}]>\n"
                    + "to have type in:\n"
                    + "  <[\"TYPE_1\", \"TYPE_2\", \"TYPE_3\"]>\n"
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
