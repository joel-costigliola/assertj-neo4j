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
 * Copyright 2013-2019 the original author or authors.
 */
package org.assertj.neo4j.api.constraintedefinition;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.schema.ConstraintDefinition;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConstraintDefinitionAssert_hasLabel_Test {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private ConstraintDefinition constraintDefinition = mock(ConstraintDefinition.class);

  @Test
  public void should_pass_if_constraint_definition_has_label() {
    when(constraintDefinition.getLabel()).thenReturn(Label.label("Foo"));

    assertNotNull(assertThat(constraintDefinition).hasLabel(Label.label("Foo")));
  }

  @Test
  public void should_fail_if_constraint_definition_has_not_expected_label() {
    when(constraintDefinition.getLabel()).thenReturn(Label.label("Foo"));
    expectedException.expect(AssertionError.class);

    assertThat(constraintDefinition).hasLabel(Label.label("Bar"));
  }

  @Test
  public void should_fail_if_label_is_null() {
    when(constraintDefinition.getLabel()).thenReturn(Label.label("Foo"));
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The label to look for should not be null");

    assertThat(constraintDefinition).hasLabel((Label) null);
  }

  @Test
  public void should_fail_constraint_definition_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((ConstraintDefinition) null).hasLabel(Label.label("Bar"));
  }

  @Test
  public void should_pass_if_constraint_definition_has_label_with_string() {
    when(constraintDefinition.getLabel()).thenReturn(Label.label("Foo"));

    assertNotNull(assertThat(constraintDefinition).hasLabel("Foo"));
  }

  @Test
  public void should_fail_if_constraint_definition_has_not_expected_label_with_string() {
    when(constraintDefinition.getLabel()).thenReturn(Label.label("Foo"));
    expectedException.expect(AssertionError.class);

    assertThat(constraintDefinition).hasLabel("Bar");
  }

  @Test
  public void should_fail_if_label_string_is_null() {
    when(constraintDefinition.getLabel()).thenReturn(Label.label("Foo"));
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The label to look for should not be null");

    assertThat(constraintDefinition).hasLabel((String) null);
  }

  @Test
  public void should_fail_constraint_definition_is_null_with_string() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((ConstraintDefinition) null).hasLabel("Bar");
  }
}
