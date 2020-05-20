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
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Checks
 * <code>{@link org.assertj.neo4j.api.RelationshipAssert#doesNotHaveType(org.neo4j.graphdb.RelationshipType)}</code>
 * behavior.
 * 
 * @author Florent Biville
 */
public class RelationshipAssert_doesNotHaveType_Test {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  private Relationship relationship = mock(Relationship.class);

  @Test
  public void should_pass_if_relationship_has_not_given_type() {
    given_relationship_has_type(RelationshipType.withName("LINKS"));

    assertThat(relationship).doesNotHaveType(RelationshipType.withName("UNLINKS"));
  }

  @Test
  public void should_fail_if_relationship_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((Relationship) null).doesNotHaveType(mock(RelationshipType.class));
  }

  @Test
  public void should_fail_if_relationship_type_is_null() {
    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("The actual relationship type should not be null");

    assertThat(relationship).doesNotHaveType(mock(RelationshipType.class));
  }

  @Test
  public void should_fail_if_given_relationship_type_is_null() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The relationship type to look for should not be null");

    given_relationship_has_type(mock(RelationshipType.class));

    assertThat(relationship).doesNotHaveType((RelationshipType) null);
  }

  @Test
  public void should_fail_if_relationship_has_given_type() {
    expectedException.expect(AssertionError.class);

    RelationshipType relationshipType = RelationshipType.withName("FOLLOWS");
    given_relationship_has_type(relationshipType);

    assertThat(relationship).doesNotHaveType(relationshipType);
  }

  private void given_relationship_has_type(RelationshipType type) {
    when(relationship.getType()).thenReturn(type);
  }

}
