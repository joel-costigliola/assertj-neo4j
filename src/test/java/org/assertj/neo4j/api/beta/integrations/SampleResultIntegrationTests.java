package org.assertj.neo4j.api.beta.integrations;

import org.assertj.core.util.Maps;
import org.assertj.neo4j.api.beta.DriverAssertions;
import org.assertj.neo4j.api.beta.testing.Dataset;
import org.assertj.neo4j.api.beta.testing.IntegrationTests;
import org.assertj.neo4j.api.beta.type.Drivers;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Records;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionConfig;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;

/**
 * @author patouche - 26/01/2021
 */
class SampleResultIntegrationTests extends IntegrationTests.DatasetTests {

    protected SampleResultIntegrationTests() {
        super(Dataset.GITHUB_LANGUAGE);
    }



    @Test
    void toto2() {

        try (final Session session = driver.session()) {
            session.readTransaction(tx -> DriverAssertions.assertThat(tx.run("MATCH (n:Repo)<-[p:TYPE]-() RETURN n, p, n.name"))
                    .hasSize(3)
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
}
