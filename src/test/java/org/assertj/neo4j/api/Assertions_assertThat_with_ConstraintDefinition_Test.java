package org.assertj.neo4j.api;

import org.junit.Test;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.IndexDefinition;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class Assertions_assertThat_with_ConstraintDefinition_Test {

  @Test
  public void should_create_Assert(){
    ConstraintDefinitionAssert constraintDefinitionAssert = Assertions.assertThat(mock(ConstraintDefinition.class));
    assertNotNull(constraintDefinitionAssert);
  }

  @Test
  public void should_pass_actual() {
    ConstraintDefinition constraintDefinition = mock(ConstraintDefinition.class);
    ConstraintDefinitionAssert constraintDefinitionAssert = assertThat(constraintDefinition);
    assertSame(constraintDefinition, constraintDefinitionAssert.getActual());
  }
}
