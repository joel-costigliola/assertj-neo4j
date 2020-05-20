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
package org.assertj.neo4j.api.node;

import org.assertj.neo4j.api.NodeAssert;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NodeAssert_hasLabel_Test {

  private final Node node = mock(Node.class);
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void should_pass_if_node_has_label() {
    given_node_with_label("Mammal");

    Assert.assertThat(assertThat(node).hasLabel(Label.label("Mammal")), instanceOf(
      NodeAssert.class));
  }

  @Test
  public void should_pass_if_node_has_label_string() {
    given_node_with_label("Mammal");

    Assert.assertThat(assertThat(node).hasLabel("Mammal"), instanceOf(
      NodeAssert.class));
  }

  @Test
  public void should_fail_if_node_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((Node) null).hasLabel(Label.label("Label"));
  }

  @Test
  public void should_fail_if_node_is_null_with_string() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((Node) null).hasLabel("Label");
  }

  @Test
  public void should_fail_if_label_is_null() {
    expectedException.expect(IllegalArgumentException.class);

    assertThat(node).hasLabel((Label) null);
  }

  @Test
  public void should_fail_if_label_string_is_null() {
    expectedException.expect(IllegalArgumentException.class);

    assertThat(node).hasLabel((String) null);
  }

  @Test
  public void should_fail_if_node_does_NOT_have_label() {
    expectedException.expect(AssertionError.class);

    given_node_with_label("Reptile");

    assertThat(node).hasLabel(Label.label("Mammal"));
  }

  @Test
  public void should_fail_if_node_does_NOT_have_label_string() {
    expectedException.expect(AssertionError.class);

    given_node_with_label("Reptile");

    assertThat(node).hasLabel("Mammal");
  }

  private void given_node_with_label(String value) {
    Label label = Label.label(value);
    when(node.hasLabel(label)).thenReturn(true);
    GraphDatabaseService graph = mock(GraphDatabaseService.class);
    when(graph.beginTx()).thenReturn(mock(Transaction.class));
    when(node.getGraphDatabase()).thenReturn(graph);
  }

}
