package org.assertj.neo4j.api.constraintedefinition;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.schema.ConstraintDefinition;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class ContributionDefinitionAssert_hasLabel_Test {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();


  @Test
  public void should_pass_if_constraint_definition_has_label(){

    ConstraintDefinition constraintDefinition = mock(ConstraintDefinition.class);
    Mockito.when(constraintDefinition.getLabel()).thenReturn(Label.label("Foo"));
    assertNotNull(assertThat(constraintDefinition).hasLabel(Label.label("Foo")));
  }

  @Test
  public void should_fail_if_constraint_definition_has_label(){
    ConstraintDefinition constraintDefinition = mock(ConstraintDefinition.class);
    Mockito.when(constraintDefinition.getLabel()).thenReturn(Label.label("Foo"));
    expectedException.expect(AssertionError.class);
    assertThat(constraintDefinition).hasLabel(Label.label("Bar"));
  }

  @Test
  public void should_fail_if_label_is_null(){
    ConstraintDefinition constraintDefinition = mock(ConstraintDefinition.class);
    Mockito.when(constraintDefinition.getLabel()).thenReturn(Label.label("Foo"));
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The label to look for should not be null");
    assertThat(constraintDefinition).hasLabel((Label) null);
  }

  @Test
  public void should_fail_constraint_definition_is_null(){
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");
    assertThat((ConstraintDefinition) null).hasLabel(Label.label("Bar"));
  }

  @Test
  public void should_pass_if_constraint_definition_has_label_with_string(){

    ConstraintDefinition constraintDefinition = mock(ConstraintDefinition.class);
    Mockito.when(constraintDefinition.getLabel()).thenReturn(Label.label("Foo"));
    assertNotNull(assertThat(constraintDefinition).hasLabel("Foo"));
  }

  @Test
  public void should_fail_if_constraint_definition_has_label_with_string(){
    ConstraintDefinition constraintDefinition = mock(ConstraintDefinition.class);
    Mockito.when(constraintDefinition.getLabel()).thenReturn(Label.label("Foo"));
    expectedException.expect(AssertionError.class);
    assertThat(constraintDefinition).hasLabel("Bar");
  }

  @Test
  public void should_fail_if_label_string_is_null(){
    ConstraintDefinition constraintDefinition = mock(ConstraintDefinition.class);
    Mockito.when(constraintDefinition.getLabel()).thenReturn(Label.label("Foo"));
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The label to look for should not be null");
    assertThat(constraintDefinition).hasLabel((String) null);
  }

  @Test
  public void should_fail_constraint_definition_is_null_with_string(){
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");
    assertThat((ConstraintDefinition) null).hasLabel("Bar");
  }
}
