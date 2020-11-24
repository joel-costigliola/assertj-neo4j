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
import org.assertj.neo4j.api.beta.testing.AbstractIntegrationTests;
import org.assertj.neo4j.api.beta.testing.Dataset;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Result;
import org.neo4j.driver.Values;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * https://insights.stackoverflow.com/survey/2020#correlated-technologies
 *
 * @author patouche - 5/26/20.
 */
@Testcontainers
class SampleBasicIntegrationTests extends AbstractIntegrationTests {

    public SampleBasicIntegrationTests() {
        super(Dataset.GITHUB_LANGUAGE);
    }

    @Test
    public void basic_insert() {

        // FAKE WHEN : Sample of function execution
        final Result result = driver.session().run("CREATE (:Language {name: \"Scala\" })");

        // THEN
        DriverAssertions.assertThat(result)
                .asSingleRecord()
                .hasSize(0);
    }

    @Test
    @Disabled("TODO : Cannot retrieve the node result assert => Check how to do that")
    public void basic_insert_write() {

        // FAKE WHEN : Sample of function execution
        final Result result = driver.session().writeTransaction(tx -> tx.run("CREATE (:Language {name: \"Scala\" })"));

        // THEN
        DriverAssertions.assertThat(result)
                .asSingleRecord()
                .hasSize(0);
    }

    @Test
    public void read_query_nodes() {

        // FAKE WHEN : Sample of function execution
        final Result result = driver.session().run("MATCH (n :Language) RETURN n");

        // THEN
        DriverAssertions.assertThat(result)
                .asSingleRecord()
                .hasSize(6)
                .haveLabel("Language")
                .haveProperty("name");
    }

    @Test
    public void read_query_relationships() {

        // FAKE WHEN : Sample of function execution
        final Result result = driver.session()
                .run("MATCH (p :Person)-[k:KNOWS]->(l :Language {name: $language}) RETURN k",
                     Values.parameters("language", "Kotlin")
                );

        // THEN
        DriverAssertions.assertThat(result)
                .haveRecords()
                .haveRecords(2)
                .haveTypeRecords(RecordType.RELATIONSHIP)
                .asSingleRecord()
                .hasSize(2)
                .haveLabel("Language")
                .haveProperty("name")
                .onNode(1, n -> {

                });
    }

    @Test
    public void read_query_with_relationship() {
        
        // FAKE WHEN : Sample of function execution
        final Result result = driver.session()
                .run("MATCH (p :Person)-[:KNOWS]->(l :Language {name: $language}) RETURN p,l",
                     Values.parameters("language", "Kotlin")
                );

        // THEN
        DriverAssertions.assertThat(result)
                .asSingleRecord()
                .hasSize(2)
                .haveLabel("Language")
                .haveProperty("name")
                .onNode(1, n -> {

                });

    }

}
