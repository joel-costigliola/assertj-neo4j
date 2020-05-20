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

import org.assertj.neo4j.api.RelationshipAssert;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RelationshipAssert_doesNotHaveType_Test {

  private final Relationship relationship = mock(Relationship.class);
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void should_pass_if_relationship_has_not_given_type() {
    given_relationship_has_type(RelationshipType.withName("LINKS"));

    Assert.assertThat(assertThat(relationship).doesNotHaveType(RelationshipType.withName("UNLINKS")), instanceOf(
      RelationshipAssert.class));
  }

  @Test
  public void should_pass_if_relationship_has_not_given_type_string() {
    given_relationship_has_type(RelationshipType.withName("LINKS"));

    Assert.assertThat(assertThat(relationship).doesNotHaveType("UNLINKS"), instanceOf(
      RelationshipAssert.class));
  }

  @Test
  public void should_fail_if_relationship_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((Relationship) null).doesNotHaveType(mock(RelationshipType.class));
  }

  @Test
  public void should_fail_if_relationship_is_null_with_string() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((Relationship) null).doesNotHaveType("");
  }

  @Test
  public void should_fail_if_actual_relationship_type_is_null() {
    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("The actual relationship type should not be null");

    assertThat(relationship).doesNotHaveType(mock(RelationshipType.class));
  }

  @Test
  public void should_fail_if_actual_relationship_type_string_is_null() {
    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("The actual relationship type should not be null");

    assertThat(relationship).doesNotHaveType("some type");
  }

  @Test
  public void should_fail_if_expected_relationship_type_is_null() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The relationship type to look for should not be null");

    given_relationship_has_type(mock(RelationshipType.class));

    assertThat(relationship).doesNotHaveType((RelationshipType) null);
  }

  @Test
  public void should_fail_if_expected_relationship_type_string_is_null() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The relationship type to look for should not be null");

    given_relationship_has_type(mock(RelationshipType.class));

    assertThat(relationship).doesNotHaveType((String) null);
  }

  @Test
  public void should_fail_if_relationship_has_given_type() {
    expectedException.expect(AssertionError.class);

    RelationshipType relationshipType = RelationshipType.withName("FOLLOWS");
    given_relationship_has_type(relationshipType);

    assertThat(relationship).doesNotHaveType(relationshipType);
  }

  @Test
  public void should_fail_if_relationship_has_given_type_string() {
    expectedException.expect(AssertionError.class);

    RelationshipType relationshipType = RelationshipType.withName("FOLLOWS");
    given_relationship_has_type(relationshipType);

    assertThat(relationship).doesNotHaveType(relationshipType.name());
  }

  private void given_relationship_has_type(RelationshipType type) {
    when(relationship.getType()).thenReturn(type);
  }

}
