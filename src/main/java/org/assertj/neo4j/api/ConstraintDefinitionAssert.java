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
 * Copyright 2013-2017 the original author or authors.
 */
package org.assertj.neo4j.api;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.schema.ConstraintDefinition;

import static org.assertj.neo4j.error.ShouldHaveLabel.shouldHaveLabel;
import static org.assertj.neo4j.error.ShouldNotHaveLabel.shouldNotHaveLabel;

/**
 * Assertions for Neo4J {@link ConstraintDefinition}
 *
 * @author Brice Boutamdja
 * @author Agathe Vaisse
 */
public class ConstraintDefinitionAssert extends AbstractAssert<ConstraintDefinitionAssert, ConstraintDefinition> {

  protected ConstraintDefinitionAssert(ConstraintDefinition constraintDefinition) {
    super(constraintDefinition, ConstraintDefinitionAssert.class);
  }

  public ConstraintDefinition getActual() {
    return actual;
  }

  /**
   * Verifies that the actual {@link ConstraintDefinition} has the given label</br>
   * <p>
   *   Example :
   *
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * ConstraintDefinition constraintDefinition = graph.schema().constraintFor(Label.label(&quot;User&quot;))
   *    .assertPropertyIsUnique(&quot;Login&quot;)
   *    .create();
   *
   * assertThat(constraintDefinition).hasLabel(Label.label(&quot;User&quot;));
   * </pre>
   * </p>
   *
   *
   * If the <code>label</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param label the label to look for in the actual {@link ConstraintDefinition}
   * @return this {@link ConstraintDefinitionAssert} for assertions chaining
   *
   * @throws IllegalArgumentException if <code>label</code> is {@code null}.
   * @throws AssertionError if the actual {@link ConstraintDefinition} does not contain the given label
   */
  public ConstraintDefinitionAssert hasLabel(Label label) {
    Objects.instance().assertNotNull(info, actual);

    if (label == null) {
      throw new IllegalArgumentException("The label to look for should not be null");
    }

    if (!label.equals(actual.getLabel())) {
      throw Failures.instance().failure(info, shouldHaveLabel(actual, label.name()));
    }

    return this;
  }

  /**
   * Verifies that the actual {@link ConstraintDefinition} has the given label name</br>
   * <p>
   *   Example :
   *
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * ConstraintDefinition constraintDefinition = graph.schema()
   *    .constraintFor(&quot;User&quot;)
   *    .assertPropertyIsUnique(&quot;Login&quot;)
   *    .create();
   *
   * assertThat(constraintDefinition).hasLabel(&quot;User&quot;);
   * </pre>
   * </p>
   *
   *
   * If the <code>labelString</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param labelValue the label name to look for in the actual {@link ConstraintDefinition}
   * @return this {@link ConstraintDefinitionAssert} for assertions chaining
   *
   * @throws IllegalArgumentException if <code>labelValue</code> is {@code null}.
   * @throws AssertionError if the actual {@link ConstraintDefinition} does not contain the given label name
   */
  public ConstraintDefinitionAssert hasLabel(String labelValue) {
    Objects.instance().assertNotNull(info, actual);

    if (labelValue == null) {
      throw new IllegalArgumentException("The label to look for should not be null");
    }

    if (!labelValue.equals(actual.getLabel().name())) {
      throw Failures.instance().failure(info, shouldHaveLabel(actual, labelValue));
    }

    return this;
  }


  /**
   * Verifies that the actual {@link ConstraintDefinition} does NOT have the given label<br/>
   * <p>
   * Example:
   *
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * ConstraintDefinition constraintDefinition = graph.schema()
   *    .indexFor(Label.label(&quot;ATWA&quot;))
   *    .on(&quot;name&quot;).create();
   *
   * assertThat(constraintDefinition).doesNotHaveLabel(Label.label(&quot;Toxicity&quot;));
   * </pre>
   *
   * If the <code>label</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param label the label to look for in the actual {@link ConstraintDefinition}
   * @return this {@link ConstraintDefinitionAssert} for assertions chaining
   *
   * @throws IllegalArgumentException if <code>label</code> is {@code null}.
   * @throws AssertionError if the actual {@link ConstraintDefinition} does contain the given label
   */
  public ConstraintDefinitionAssert doesNotHaveLabel(Label label) {
    Objects.instance().assertNotNull(info, actual);

    if (label == null) {
      throw new IllegalArgumentException("The label value to look for should not be null");
    }
    if (actual.getLabel().equals(label)) {
      throw Failures.instance().failure(info, shouldNotHaveLabel(actual, label.name()));
    }
    return this;
  }


  /**
   * Verifies that the actual {@link ConstraintDefinition} has the given label name<br/>
   * <p>
   * Example:
   *
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * ConstraintDefinition constraintDefinition = graph.schema()
   *    .indexFor(Label.label(&quot;Chop Suey&quot;))
   *    .on(&quot;name&quot;).create();
   *
   * assertThat(constraintDefinition).doesNotHaveLabel(&quot;Lost in Hollywood&quot;);
   * </pre>
   *
   * If the <code>labelValue</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param labelValue the label name to look for in the actual {@link ConstraintDefinition}
   * @return this {@link ConstraintDefinitionAssert} for assertions chaining
   *
   * @throws IllegalArgumentException if <code>labelValue</code> is {@code null}.
   * @throws AssertionError if the actual {@link ConstraintDefinition} does contain the given label
   */
  public ConstraintDefinitionAssert doesNotHaveLabel(String labelValue){
    Objects.instance().assertNotNull(info, actual);

    if (labelValue == null){
      throw new IllegalArgumentException("The label value to look for should not be null");
    }
    if (actual.getLabel().name().equals(labelValue)) {
      throw Failures.instance().failure(info, shouldNotHaveLabel(actual, labelValue));
    }
    return this;
  }
}
