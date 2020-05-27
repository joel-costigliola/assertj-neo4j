package org.assertj.neo4j.api;

import org.junit.ClassRule;
import org.junit.Test;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.testcontainers.containers.Neo4jContainer;

/**
 * @author patouche - 5/26/20.
 */

public class IntegrationTest {

    @ClassRule
    public static Neo4jContainer NEO4J_CONTAINER = new Neo4jContainer<>("neo4j:3.4.18")
            .withAdminPassword(null);

    private static Driver dataset(Neo4jContainer container) {
        final Driver driver = GraphDatabase.driver(container.getBoltUrl());
        return driver;
    }

    @Test
    public void version_3_4() throws Exception {

        // GIVEN
        final Driver driver = GraphDatabase.driver(NEO4J_CONTAINER.getBoltUrl());

        // WHEN
        // THEN

        DriverAssertions.assertThat(driver)
                .withDataset(
                        "CREATE (:Language {name: \"Java\" })",
                        "CREATE (:Language {name: \"Golang\" })",
                        "CREATE (:Language {name: \"Kotlin\" })",
                        "CREATE (:Language {name: \"Python\" })",
                        "CREATE (:Language {name: \"JavaScript\" })",
                        "CREATE (:Language {name: \"Rust\" })",
                        "CREATE (:Language {name: \"Elixir\" })",
                        "CREATE (:Language {name: \"C\" })",
                        "CREATE (:Language {name: \"C++\" })",
                        "CREATE (:Language {name: \"Lisp\" })"
                )
                .when(() -> {
                    // Should invoke test method
                    try (Session session = driver.session()) {
                        session.run("CREATE (:Language {name: \"Scala\" })");
                    }
                })
                .executing("MATCH (n1) RETURN n1", result -> result
                        .hasSize(11))
                .executing("MATCH (n2) RETURN n2", result -> result
                        .filteredOnKeys("name")
                        .hasSize(11))
                .executing("MATCH (n:Language) RETURN n", result -> result
                        .filteredOnKeyValue("name", "Scala")
                        .single());

    }
}
