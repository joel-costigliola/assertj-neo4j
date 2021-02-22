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
package org.assertj.neo4j.api.beta.integrations;

import org.assertj.neo4j.api.beta.DriverAssertions;
import org.assertj.neo4j.api.beta.testing.Dataset;
import org.assertj.neo4j.api.beta.testing.IntegrationTests;
import org.assertj.neo4j.api.beta.testing.TestTags;
import org.assertj.neo4j.api.beta.type.ObjectType;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Query;
import org.neo4j.driver.Session;

/**
 * @author Patrick Allain - 26/01/2021
 */
@Tag(TestTags.INTEGRATION)
class SampleResultIntegrationTests {

    private static final Query SAMPLE_QUERY =
            new Query("MATCH (repo:Repo)-[w:WRITTEN]->() RETURN  repo, w AS written, repo.name");

    @Nested
    @DisplayName("Should succeed")
    class ShouldSucceedTests extends IntegrationTests.DatasetTests {

        ShouldSucceedTests() {
            super(Dataset.GITHUB_LANGUAGE);
        }

        @Test
        void sample_multi_columns_result() {
            //@formatter:off
            try (final Session session = driver.session()) {
                session.readTransaction(tx -> DriverAssertions.assertThat(tx.run(SAMPLE_QUERY)))
                        .hasRecordSize(13)
                        .hasColumnSize(3)
                        .hasColumns("repo", "written", "repo.name")
                        .haveType("repo", ObjectType.NODE)
                        .haveValueType("repo.name", ValueType.STRING)
                        .haveValueInstanceOf("repo.name", String.class)
                        .asListOf("repo.name", String.class)
                            .contains("assertj-core", "assertj-core", "neo4j", "junit5", "cli", "ktor", "kubernetes", "flask", "click")
                            .toRootAssert()
                        .asRelationshipAssert("written")
                            .hasSize(13)
                            .extractingProperty("percent", Double.class)
                                .contains(0.3, 99.7, 0.2, 76.8, 2.1, 97.5, 0.2, 99.8, 0.2, 2.8, 90.3, 99.9, 100.0)
                                .toParentAssert()
                            .toParentAssert()
                        .hasRecordSize(13)
                        .hasColumnSize(3);
            }
            //@formatter:on
        }

        @Test
        void sample_limit_1_of_single_column() {
            try (final Session session = driver.session()) {
                session.readTransaction(tx -> DriverAssertions
                        .assertThat(tx.run("MATCH (n:Repo) RETURN n.name LIMIT 1"))
                        .asListOf(String.class)
                        .hasSize(1)
                );
            }
        }

        @Test
        void hasColumnSize() {
            // WHEN && THEN
            try (final Session session = driver.session()) {
                session.readTransaction(tx -> DriverAssertions.assertThat(tx.run(SAMPLE_QUERY))
                        .hasColumnSize(3)
                );
            }
        }

        @Test
        void hasRecordSize() {
            // WHEN && THEN
            try (final Session session = driver.session()) {
                session.readTransaction(tx -> DriverAssertions.assertThat(tx.run(SAMPLE_QUERY))
                        .hasRecordSize(13)
                );
            }
        }

        @Test
        void hasColumns() {
            // WHEN && THEN
            try (final Session session = driver.session()) {
                session.readTransaction(tx -> DriverAssertions.assertThat(tx.run(SAMPLE_QUERY))
                        // .hasSize(3)
                        .hasColumns("repo", "written", "repo.name")
                );
            }
        }

        @Test
        void haveType() {
            // WHEN && THEN
            try (final Session session = driver.session()) {
                session.readTransaction(tx -> DriverAssertions.assertThat(tx.run(SAMPLE_QUERY))
                        // .hasSize(3)
                        .haveType("repo", ObjectType.NODE)
                        .haveType("written", ObjectType.RELATIONSHIP)
                        .haveType("repo.name", ObjectType.VALUE)
                );
            }
        }

        @Test
        void haveValueType() {
            // WHEN && THEN
            try (final Session session = driver.session()) {
                session.readTransaction(tx -> DriverAssertions.assertThat(tx.run(SAMPLE_QUERY))
                        // .hasSize(3)
                        .haveValueType("repo.name", ValueType.STRING)
                );
            }
        }

    }

}
