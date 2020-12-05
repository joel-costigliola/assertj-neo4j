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
package org.assertj.neo4j.api.beta.type;

import org.assertj.neo4j.api.beta.testing.Mocks;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionConfig;
import org.neo4j.driver.Values;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author patouche - 11/11/2020
 */
@ExtendWith(MockitoExtension.class)
class NodesTests {

    @Mock
    private Driver driver;

    @Mock
    private Session session;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(session, driver);
    }

    @Test
    void load_whenNoLabels() {
        // GIVEN
        final Nodes nodes = new Nodes(driver);
        final Result result = Mocks.result()
                .record(Mocks.record()
                        .key("key")
                        .node(Mocks.node().labels("Sample").properties("prop-0", Values.value(true)).build())
                        .build()
                )
                .record(Mocks.record()
                        .key("key")
                        .node(Mocks.node().id(1).labels("Sample").properties("prop-1", Values.value(false)).build())
                        .build()
                )
                .build();

        doReturn(session).when(driver).session();
        doReturn(result).when(session).run(anyString(), any(TransactionConfig.class));

        // WHEN
        final List<Nodes.DbNode> dbNodes = nodes.load();

        // THEN
        assertThat(dbNodes)
                .hasSize(2)
                .contains(
                        Drivers.node().id(0).label("Sample").property("prop-0", true).build(),
                        Drivers.node().id(1).label("Sample").property("prop-1", false).build()
                );

        verify(driver).session();
        verify(session).run(eq("MATCH (n ) RETURN n"), any(TransactionConfig.class));
        verify(session).close();
    }

    @Test
    void load_whenMultiLabels() {
        // GIVEN
        final Nodes nodes = new Nodes(driver, "Lbl1", "Lbl2");
        final Result result = Mocks.result()
                .record(Mocks.record()
                        .key("key")
                        .node(Mocks.node().id(0).labels("Lbl1").labels("Lbl2").properties("prop-0", Values.value(true)).build())
                        .build()
                )
                .record(Mocks.record()
                        .key("key")
                        .node(Mocks.node().id(1).labels("Lbl1").labels("Lbl2").properties("prop-1",
                                Values.value(false)).build())
                        .build()
                )
                .build();

        doReturn(session).when(driver).session();
        doReturn(result).when(session).run(anyString(), any(TransactionConfig.class));

        // WHEN
        final List<Nodes.DbNode> dbNodes = nodes.load();

        // THEN
        assertThat(dbNodes)
                .hasSize(2)
                .contains(
                        Drivers.node().id(0).label("Lbl1").label( "Lbl2").property("prop-0", true).build(),
                        Drivers.node().id(1).label("Lbl1").label( "Lbl2").property("prop-1", false).build()
                );

        verify(driver).session();
        verify(session).run(eq("MATCH (n :Lbl1:Lbl2) RETURN n"), any(TransactionConfig.class));
        verify(session).close();
    }

}
