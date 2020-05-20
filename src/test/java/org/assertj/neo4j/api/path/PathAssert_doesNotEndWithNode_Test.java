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
package org.assertj.neo4j.api.path;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PathAssert_doesNotEndWithNode_Test {

  private final Path path = mock(Path.class);
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void should_fail_if_path_ends_with_node() {
    Node node = mock(Node.class);
    given_path_ends_with_node(node);
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage(String.format("Expecting:%n"));
    expectedException.expectMessage(String.format("%nto not end with node:%n"));

    assertThat(path).doesNotEndWithNode(node);
  }

  @Test
  public void should_fail_if_path_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((Path) null).doesNotEndWithNode(mock(Node.class));
  }

  @Test
  public void should_fail_if_path_end_node_is_null() {
    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("The actual end node should not be null");

    assertThat(path).doesNotEndWithNode(mock(Node.class));
  }

  @Test
  public void should_fail_if_given_end_node_is_null() {
    given_path_ends_with_node(mock(Node.class));

    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The end node to look for should not be null");

    assertThat(path).doesNotEndWithNode(null);
  }

  @Test
  public void should_pass_if_path_does_not_end_with_given_node() {
    given_path_ends_with_node(mock(Node.class));

    assertThat(path).doesNotEndWithNode(mock(Node.class));
  }

  private void given_path_ends_with_node(Node node) {
    when(path.endNode()).thenReturn(node);
  }

}
