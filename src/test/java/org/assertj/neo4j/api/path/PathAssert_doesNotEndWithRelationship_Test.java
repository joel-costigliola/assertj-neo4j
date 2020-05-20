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
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Checks <code>{@link org.assertj.neo4j.api.PathAssert#doesNotEndWithRelationship(Relationship)}</code>
 * behavior.
 *
 * @author Florent Biville
 */
public class PathAssert_doesNotEndWithRelationship_Test {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  private Path path = mock(Path.class);

  @Test
  public void should_fail_if_path_ends_with_relationship() {
    Relationship relationship = mock(Relationship.class);
    given_path_last_relationship(relationship);
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage(String.format("Expecting:%n"));
    expectedException.expectMessage(String.format("%nto not end with relationship:%n"));

    assertThat(path).doesNotEndWithRelationship(relationship);
  }

  @Test
  public void should_fail_if_path_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((Path) null).doesNotEndWithRelationship(mock(Relationship.class));
  }

  @Test
  public void should_fail_if_path_last_relationship_is_null() {
    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("The actual last relationship should not be null");

    assertThat(path).doesNotEndWithRelationship(mock(Relationship.class));
  }

  @Test
  public void should_fail_if_given_last_relationship_is_null() {
    given_path_last_relationship(mock(Relationship.class));

    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The last relationship to look for should not be null");

    assertThat(path).doesNotEndWithRelationship(null);
  }

  @Test
  public void should_fail_if_path_does_not_end_with_given_relationship() {
    given_path_last_relationship(mock(Relationship.class));

    assertThat(path).doesNotEndWithRelationship(mock(Relationship.class));
  }

  private void given_path_last_relationship(Relationship relationship) {
    when(path.lastRelationship()).thenReturn(relationship);
  }

}
