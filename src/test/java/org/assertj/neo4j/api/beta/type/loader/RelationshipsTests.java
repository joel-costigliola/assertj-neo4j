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
package org.assertj.neo4j.api.beta.type.loader;

import org.assertj.neo4j.api.beta.testing.Builders;
import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.assertj.neo4j.api.beta.type.Models;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Query;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionConfig;
import org.neo4j.driver.Values;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Patrick Allain - 24/11/2020
 */
@ExtendWith(MockitoExtension.class)
class RelationshipsTests {

    @Mock
    private Driver driver;

    @Mock
    private Session session;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(session, driver);
    }

    @Test
    void load() {
        // GIVEN
        final Relationships relationships = Relationships.of(driver, "KNOWS");
        final List<Record> result = Arrays.asList(
                Builders.record()
                        .relation(
                                "key",
                                Builders.relation("SAMPLE").id(1).properties("prop-0", Values.value(true)).build()
                        )
                        .build(),
                Builders.record()
                        .relation(
                                "key",
                                Builders.relation("SAMPLE").id(2).properties("prop-1", Values.value(false)).build()
                        )
                        .build()
        );

        doReturn(session).when(driver).session();
        doReturn(result).when(session).readTransaction(any(), any(TransactionConfig.class));

        // WHEN
        final List<DbRelationship> dbRelationships = relationships.load();

        // THEN
        assertThat(dbRelationships)
                .hasSize(2)
                .contains(
                        Models.relation("SAMPLE").id(1).property("prop-0", true).build(),
                        Models.relation("SAMPLE").id(2).property("prop-1", false).build()
                );
        assertThat(relationships.query()).isEqualTo(new Query("MATCH ()-[r :KNOWS]->() RETURN r"));

        verify(driver).session();
        verify(session).readTransaction(any(), any(TransactionConfig.class));
        verify(session).close();
    }

}