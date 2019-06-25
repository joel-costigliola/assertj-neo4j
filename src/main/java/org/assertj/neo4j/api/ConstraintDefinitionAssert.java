package org.assertj.neo4j.api;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.schema.ConstraintDefinition;

import static org.assertj.neo4j.error.ShouldHaveLabel.shouldHaveLabel;

public class ConstraintDefinitionAssert extends AbstractAssert<ConstraintDefinitionAssert, ConstraintDefinition> {

  public ConstraintDefinitionAssert(ConstraintDefinition constraintDefinition)  {
    super(constraintDefinition, ConstraintDefinitionAssert.class);
  }

  public ConstraintDefinition getActual() {
    return actual;
  }

  public ConstraintDefinitionAssert hasLabel(Label label) {
    Objects.instance().assertNotNull(info, actual);

    if (label == null){
      throw new IllegalArgumentException("The label to look for should not be null");
    }

    if (!label.equals(actual.getLabel())){
      throw Failures.instance().failure(info, shouldHaveLabel(actual, label.name()));
    }

    return this;
  }

  public ConstraintDefinitionAssert hasLabel(String labelString) {
    Objects.instance().assertNotNull(info, actual);

    if (labelString == null){
      throw new IllegalArgumentException("The label to look for should not be null");
    }

    if (!labelString.equals(actual.getLabel().name())){
      throw Failures.instance().failure(info, shouldHaveLabel(actual, labelString));
    }

    return this;
  }
}
