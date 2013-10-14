/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright 2013-2013 the original author or authors.
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
 * Checks <code>{@link org.assertj.neo4j.api.PathAssert#hasLastRelationship(org.neo4j.graphdb.Relationship)}</code>
 * behavior.
 * 
 * @author Florent Biville
 */
public class PathAssert_hasLastRelationship_Test {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  private Path path = mock(Path.class);

  @Test
  public void should_pass_hasLastRelationship_if_relationship_ends_with_node() {
    Relationship relationship = mock(Relationship.class);
    given_path_last_relationship(relationship);

    assertThat(path).hasLastRelationship(relationship);
  }

  @Test
  public void should_fail_hasLastRelationship_if_relationship_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((Path) null).hasLastRelationship(mock(Relationship.class));
  }

  @Test
  public void should_fail_hasLastRelationship_if_relationship_end_node_is_null() {
    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("The actual last relationship should not be null");

    assertThat(path).hasLastRelationship(mock(Relationship.class));
  }

  @Test
  public void should_fail_hasLastRelationship_if_passed_end_node_is_null() {
    given_path_last_relationship(mock(Relationship.class));

    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The last relationship to look for should not be null");

    assertThat(path).hasLastRelationship((Relationship) null);
  }

  @Test
  public void should_fail_hasLastRelationship_if_relationship_does_NOT_start_with_passed_node() {
    given_path_last_relationship(mock(Relationship.class));

    expectedException.expect(AssertionError.class);

    assertThat(path).hasLastRelationship(mock(Relationship.class));
  }

  private void given_path_last_relationship(Relationship relationship) {
    when(path.lastRelationship()).thenReturn(relationship);
  }

}
