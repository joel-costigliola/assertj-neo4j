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
 * Copyright 2013-2019 the original author or authors.
 */
package org.assertj.neo4j.api;

import org.junit.Test;
import org.neo4j.graphdb.Node;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

/**
 * Tests for <code>{@link org.assertj.neo4j.api.Assertions#assertThat(org.neo4j.graphdb.Node)}</code>
 * 
 * @author Florent Biville
 */
public class Assertions_assertThat_with_Node_Test {

  @Test
  public void should_create_Assert() {
    NodeAssert nodeAssert = assertThat(mock(Node.class));
    assertNotNull(nodeAssert);
  }

  @Test
  public void should_pass_actual() {
    Node node = mock(Node.class);
    NodeAssert nodeAssert = assertThat(node);
    assertSame(node, nodeAssert.getActual());
  }
}
