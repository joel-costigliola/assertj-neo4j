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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.ConstraintType;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConstraintDefinitionAssert_isConstraintType_Test {

  private final ConstraintDefinition constraintDefinition = mock(ConstraintDefinition.class);
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void should_pass_if_constraint_definition_is_of_constraint_type() {
    when(constraintDefinition.isConstraintType(ConstraintType.UNIQUENESS)).thenReturn(true);

    assertNotNull(assertThat(constraintDefinition).isConstraintType(ConstraintType.UNIQUENESS));
  }

  @Test
  public void should_fail_if_constraint_definition_is_not_of_constraint_type() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual to be of type UNIQUENESS but was not");

    when(constraintDefinition.isConstraintType(ConstraintType.UNIQUENESS)).thenReturn(false);

    assertThat(constraintDefinition).isConstraintType(ConstraintType.UNIQUENESS);
  }

  @Test
  public void should_fail_if_expected_constraint_type_is_null() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The constraint type to look for should not be null");

    assertThat(constraintDefinition).isConstraintType(null);
  }

  @Test
  public void should_fail_if_constraint_definition_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((ConstraintDefinition) null).isConstraintType(ConstraintType.UNIQUENESS);
  }

  @Test
  public void should_fail_with_custom_error_message() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Custom error FTW");

    assertThat(constraintDefinition)
      .overridingErrorMessage("Custom error FTW")
      .isConstraintType(ConstraintType.UNIQUENESS);
  }
}
