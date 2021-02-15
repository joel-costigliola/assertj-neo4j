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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Query;
import org.neo4j.driver.Session;

/**
 * @author Patrick Allain - 26/01/2021
 */
class SampleResultIntegrationTests extends IntegrationTests.DatasetTests {

    protected SampleResultIntegrationTests() {
        super(Dataset.GITHUB_LANGUAGE);
    }

    @Test
    void sample_multi_columns_result() {

        try (final Session session = driver.session()) {
            session.readTransaction(tx -> DriverAssertions
                    .assertThat(tx.run("MATCH (n:Repo)-[w:WRITTEN]->() RETURN n, w, n.name"))
                    // .hasSize(3)
                    .hasColumnNumber(2)
                    .isNode("n")
                    .asListOf(String.class)
            );
        }
    }

    @Test
    void toto3() {
        try (final Session session = driver.session()) {
            session.readTransaction(tx -> DriverAssertions.assertThat(tx.run("MATCH (n:Repo) RETURN n.name LIMIT 1"))
                    .contains()
            );
        }
    }

    @Nested
    class ShouldSuccessTests extends IntegrationTests.DatasetTests {

        ShouldSuccessTests() {
            super(Dataset.GITHUB_LANGUAGE);
        }

        @Test
        void hasColumnNumber() {
            // GIVEN
            final Query query = new Query("MATCH (n:Repo)-[w:WRITTEN]->() RETURN n, w, n.name");

            // WHEN && THEN
            try (final Session session = driver.session()) {
                session.readTransaction(tx -> DriverAssertions.assertThat(tx.run(query))
                        // .hasSize(3)
                        .hasColumnNumber(2)
                        .isNode("n")
                        .asListOf(String.class)
                );
            }
        }

        @Test
        void hasColumns() {
            // GIVEN
            // WHEN
            // THEN
        }

        @Test
        void isNode() {
            // GIVEN
            // WHEN
            // THEN
        }
    }
}
