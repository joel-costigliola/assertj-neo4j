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
package org.assertj.neo4j.api.relationship;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RelationshipAssert_startsWithNode_Test {

  private final Relationship relationship = mock(Relationship.class);
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void should_pass_if_relationship_starts_with_node() {
    Node node = mock(Node.class);
    given_relationship_starts_with_node(node);

    assertThat(relationship).startsWithNode(node);
  }

  @Test
  public void should_fail_if_relationship_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((Relationship) null).startsWithNode(mock(Node.class));
  }

  @Test
  public void should_fail_if_relationship_start_node_is_null() {
    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("The actual start node should not be null");

    assertThat(relationship).startsWithNode(mock(Node.class));
  }

  @Test
  public void should_fail_if_given_start_node_is_null() {
    given_relationship_starts_with_node(mock(Node.class));

    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The start node to look for should not be null");

    assertThat(relationship).startsWithNode(null);
  }

  @Test
  public void should_fail_if_relationship_does_NOT_start_with_given_node() {
    given_relationship_starts_with_node(mock(Node.class));

    expectedException.expect(AssertionError.class);

    assertThat(relationship).startsWithNode(mock(Node.class));
  }

  private void given_relationship_starts_with_node(Node node) {
    when(relationship.getStartNode()).thenReturn(node);
  }

}
