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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NodeAssert_doesNotHaveLabel_Test {

  private final Node node = mock(Node.class);
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void should_pass_if_node_does_not_have_label() {
    given_node_with_label("MAMMAL");

    assertThat(node).doesNotHaveLabel(Label.label("REPTILE"));
  }

  @Test
  public void should_fail_if_node_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((Node) null).doesNotHaveLabel(Label.label("LABEL"));
  }

  @Test
  public void should_fail_if_label_value_is_null() {
    expectedException.expect(IllegalArgumentException.class);

    assertThat(node).doesNotHaveLabel((Label) null);
  }

  @Test
  public void should_fail_if_node_has_label() {
    expectedException.expect(AssertionError.class);

    given_node_with_label("REPTILE");

    assertThat(node).doesNotHaveLabel(Label.label("REPTILE"));
  }

  @SuppressWarnings("unchecked")
  private void given_node_with_label(String value) {
    ResourceIterable<Label> labels = mock(ResourceIterable.class);
    ResourceIterator<Label> iterator = mock(ResourceIterator.class);
    Label label = Label.label(value);
    when(iterator.next()).thenReturn(label);
    when(iterator.hasNext()).thenReturn(true, false);
    when(labels.iterator()).thenReturn(iterator);
    when(node.hasLabel(label)).thenReturn(true);
  }

}
