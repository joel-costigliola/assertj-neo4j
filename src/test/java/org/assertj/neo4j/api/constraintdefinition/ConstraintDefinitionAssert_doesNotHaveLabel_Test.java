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

import org.assertj.neo4j.api.ConstraintDefinitionAssert;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.schema.ConstraintDefinition;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConstraintDefinitionAssert_doesNotHaveLabel_Test {

  private final ConstraintDefinition constraintDefinition = mock(ConstraintDefinition.class);
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void should_pass_if_constraint_definition_does_not_have_label() {
    given_constraint_definition_with_label("CanibalCorps");

    Assert.assertThat(assertThat(constraintDefinition).doesNotHaveLabel(Label.label("Lorie")), instanceOf(
      ConstraintDefinitionAssert.class));
  }

  @Test
  public void should_pass_if_constraint_definition_does_not_have_label_string() {
    given_constraint_definition_with_label("One Direction");

    Assert.assertThat(assertThat(constraintDefinition).doesNotHaveLabel("Rihanna"), instanceOf(
      ConstraintDefinitionAssert.class));
  }

  @Test
  public void should_fail_if_constraint_definition_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((ConstraintDefinition) null).doesNotHaveLabel(Label.label("System of a down"));
  }

  @Test
  public void should_fail_if_constraint_definition_is_null_with_string() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((ConstraintDefinition) null).doesNotHaveLabel("System of a down");
  }

  @Test
  public void should_fail_if_label_is_null() {
    expectedException.expect(IllegalArgumentException.class);

    assertThat(constraintDefinition).doesNotHaveLabel((Label) null);
  }

  @Test
  public void should_fail_if_label_string_is_null() {
    expectedException.expect(IllegalArgumentException.class);

    assertThat(constraintDefinition).doesNotHaveLabel((String) null);
  }

  @Test
  public void should_fail_if_constraint_definition_has_label() {
    expectedException.expect(AssertionError.class);

    given_constraint_definition_with_label("The Beatles");

    assertThat(constraintDefinition).doesNotHaveLabel(Label.label("The Beatles"));
  }

  @Test
  public void should_fail_if_constraint_definition_has_label_string() {
    expectedException.expect(AssertionError.class);

    given_constraint_definition_with_label("The Beatles");

    assertThat(constraintDefinition).doesNotHaveLabel("The Beatles");
  }

  private void given_constraint_definition_with_label(String value) {
    Label label = Label.label(value);
    when(constraintDefinition.getLabel()).thenReturn(label);
  }

}
