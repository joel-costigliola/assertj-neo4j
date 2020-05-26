package org.assertj.neo4j.api;

import org.junit.ClassRule;
import org.junit.Test;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.testcontainers.containers.Neo4jContainer;

/**
 * @author patouche - 5/26/20.
 */

public class IntegrationTest {

    @ClassRule
    public static Neo4jContainer NEO4J_CONTAINER = new Neo4jContainer<>("neo4j:3.4.18")
            .withAdminPassword(null);

    @Test
    public void version_3_4() throws Exception {

        // GIVEN
        // WHEN
        // THEN
        final Driver driver = GraphDatabase.driver(NEO4J_CONTAINER.getBoltUrl());

        DriverAssertions.assertThat(driver)
                .executing("MATCH (n) RETURN", result -> {
                    result
                            .filteredOnKeys("key-1", "key-2")
                            .single();
                })
                .executing("MATCH (n) RETURN", result -> {
                    result
                            .filteredOnKeys("key-2", "key-3")
                            .single();
                });


    }
}
