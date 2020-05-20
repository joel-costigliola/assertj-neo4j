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
package org.assertj.neo4j.api.constraintdefinition;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.schema.ConstraintDefinition;

public class ConstraintDefinitionAssert_doesNotHaveRelationshipType_Test {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private ConstraintDefinition constraintDefinition = mock(ConstraintDefinition.class);

  @Test
  public void should_pass_if_constraint_definition_does_not_have_relationship_type() {
    when(constraintDefinition.getRelationshipType()).thenReturn(RelationshipType.withName("Foo"));

    assertNotNull(assertThat(constraintDefinition).doesNotHaveRelationshipType(RelationshipType.withName("Bar")));
  }

  @Test
  public void should_fail_if_constraint_definition_have_given_relationshiptype() {
    when(constraintDefinition.getRelationshipType()).thenReturn(RelationshipType.withName("Foo"));
    expectedException.expect(AssertionError.class);

    assertThat(constraintDefinition).doesNotHaveRelationshipType(RelationshipType.withName("Foo"));
  }

  @Test
  public void should_fail_if_relationshiptype_is_null() {
    when(constraintDefinition.getRelationshipType()).thenReturn(RelationshipType.withName("Foo"));
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The relationship type to look for should not be null");

    assertThat(constraintDefinition).doesNotHaveRelationshipType((RelationshipType) null);
  }

  @Test
  public void should_fail_constraint_definition_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((ConstraintDefinition) null).doesNotHaveRelationshipType(RelationshipType.withName("Foo"));
  }

  @Test
  public void should_pass_if_constraint_definition_does_not_have_relationshiptype_with_name() {
    when(constraintDefinition.getRelationshipType()).thenReturn(RelationshipType.withName("Foo"));

    assertNotNull(assertThat(constraintDefinition).doesNotHaveRelationshipType("Bar"));
  }

  @Test
  public void should_fail_if_consrtraint_definition_has_relationshiptype_with_name() {
    when(constraintDefinition.getRelationshipType()).thenReturn(RelationshipType.withName("Foo"));
    expectedException.expect(AssertionError.class);

    assertThat(constraintDefinition).doesNotHaveRelationshipType("Foo");
  }

  @Test
  public void should_fail_if_relationshiptype_name_is_null() {
    when(constraintDefinition.getRelationshipType()).thenReturn(RelationshipType.withName("Foo"));
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The relationship type name to look for should not be null");

    assertThat(constraintDefinition).doesNotHaveRelationshipType((String) null);
  }

  @Test
  public void should_fail_constraint_definition_is_null_with_relationshiptype_name() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((ConstraintDefinition) null).doesNotHaveRelationshipType("B");
  }
  
}
