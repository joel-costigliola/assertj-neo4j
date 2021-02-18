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
package org.assertj.neo4j.api.beta.testing;

import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Logging;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static java.lang.ClassLoader.getSystemResourceAsStream;
import static java.util.Objects.requireNonNull;

/**
 * For org.assertj.neo4j.api.beta.testing purpose...
 *
 * @author Patrick Allain
 */
public final class Neo4JDataSet {

    private final Driver driver;

    private Neo4JDataSet(final Driver driver) {
        this.driver = requireNonNull(driver);
    }

    public static Neo4JDataSet fromDriver(final Driver driver) {
        return new Neo4JDataSet(driver);
    }

    public static Neo4JDataSet fromUrl(final String url) {
        return new Neo4JDataSet(GraphDatabase.driver(url, Config.builder()
                .withLogging(Logging.slf4j())
                .build())
        );
    }

    private Result runSingle(final String query) {
        final Session session = this.driver.session();
        return session.writeTransaction(tx -> tx.run(query));
    }

    private Result runScript(final String script) {
        final InputStream in = requireNonNull(getSystemResourceAsStream(script), "Cannot find script : " + script);
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            final String query = reader.lines().collect(Collectors.joining("\n"));
            return this.runSingle(query);
        } catch (final IOException e) {
            throw new RuntimeException("Oups", e);
        }
    }

    public Neo4JDataSet execScript(final String script) {
        this.runScript(script);
        return this;
    }

    public Neo4JDataSet clean() {
        runSingle("MATCH (n) DETACH DELETE n");
        // runSingle(""); // Drop constraint
        // runSingle(""); // Drop index
        return this;
    }

    public Driver getDriver() {
        return driver;
    }

}
