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
package org.assertj.neo4j.api;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.schema.IndexDefinition;

import static org.assertj.neo4j.error.ShouldHaveLabel.shouldHaveLabel;
import static org.assertj.neo4j.error.ShouldNotHaveLabel.shouldNotHaveLabel;

/**
 * Assertions for Neo4J {@link IndexDefinition}
 *
 * @author Agathe Vaisse
 * @author Gwenaelle Rispal
 */
public class IndexDefinitionAssert extends AbstractAssert<IndexDefinitionAssert, IndexDefinition> {

  protected IndexDefinitionAssert(IndexDefinition actual) {
    super(actual, IndexDefinitionAssert.class);
  }

  public IndexDefinition getActual() {
    return actual;
  }

  /**
   * Verifies that the actual {@link IndexDefinition} has the given label<br/>
   * <p>
   * Example:
   *
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * IndexDefinition indexDefinition = graph.schema()
   *    .indexFor(Label.label(&quot;War Machine&quot;))
   *    .on(&quot;name&quot;).create();
   *
   * assertThat(node).hasLabel(warMachine);
   * </pre>
   *
   * If the <code>label</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param label the label to look for in the actual {@link IndexDefinition}
   * @return this {@link IndexDefinitionAssert} for assertions chaining
   *
   * @throws IllegalArgumentException if <code>label</code> is {@code null}.
   * @throws AssertionError if the actual {@link IndexDefinition} does not contain the given label
   */

  public IndexDefinitionAssert hasLabel(Label label) {
    Objects.instance().assertNotNull(info, actual);

    if (label == null) {
      throw new IllegalArgumentException("The label to look for should not be null");

    }
    if (!actual.getLabel().equals(label)) {
      throw Failures.instance().failure(info, shouldHaveLabel(actual, label.name()));
    }
    return this;
  }

  /**
   * Verifies that the actual {@link IndexDefinition} does NOT have the given label<br/>
   * <p>
   * Example:
   *
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * IndexDefinition indexDefinition = graph.schema()
   *    .indexFor(Label.label(&quot;Spiderman&quot;))
   *    .on(&quot;name&quot;).create();
   *
   * assertThat(node).doesNotHaveLabel(Label.label(&quot;Shuri&quot;));
   * </pre>
   *
   * If the <code>label</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param label the label to look for in the actual {@link IndexDefinition}
   * @return this {@link IndexDefinitionAssert} for assertions chaining
   *
   * @throws IllegalArgumentException if <code>label</code> is {@code null}.
   * @throws AssertionError if the actual {@link IndexDefinition} does contain the given label
   */

  public IndexDefinitionAssert doesNotHaveLabel(Label label) {
    Objects.instance().assertNotNull(info, actual);

    if (label == null) {
      throw new IllegalArgumentException("The label to look for should not be null");
    }
    if (actual.getLabel().equals(label)) {
      throw Failures.instance().failure(info, shouldNotHaveLabel(actual, label.name()));
    }
    return this;
  }

  /**
   * Verifies that the actual {@link IndexDefinition} has the given label name<br/>
   * <p>
   * Example:
   *
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * IndexDefinition indexDefinition = graph.schema()
   *    .indexFor(Label.label(&quot;Spiderman&quot;))
   *    .on(&quot;name&quot;).create();
   *
   * assertThat(indexDefinition).hasLabel(&quot;Spiderman&quot;);
   * </pre>
   *
   * If the <code>labelValue</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param labelValue the label name to look for in the actual {@link IndexDefinition}
   * @return this {@link IndexDefinitionAssert} for assertions chaining
   *
   * @throws IllegalArgumentException if <code>labelValue</code> is {@code null}.
   * @throws AssertionError if the actual {@link IndexDefinition} does not contain the given label
   */

  public IndexDefinitionAssert hasLabel(String labelValue) {
    Objects.instance().assertNotNull(info, actual);

    if (labelValue == null) {
      throw new IllegalArgumentException("The label value to look for should not be null");
    }
    if (!actual.getLabel().name().equals(labelValue)) {
      throw Failures.instance().failure(info, shouldHaveLabel(actual, labelValue));
    }
    return this;
  }

  /**
   * Verifies that the actual {@link IndexDefinition} does NOT have the given label name<br/>
   * <p>
   * Example:
   *
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * IndexDefinition indexDefinition = graph.schema()
   *    .indexFor(Label.label(&quot;Spiderman&quot;))
   *    .on(&quot;name&quot;).create();
   *
   * assertThat(indexDefinition).doesNotHaveLabel(&quot;Pepper Potts&quot;);
   * </pre>
   *
   * If the <code>labelValue</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param labelValue the label name to look for in the actual {@link IndexDefinition}
   * @return this {@link IndexDefinitionAssert} for assertions chaining
   *
   * @throws IllegalArgumentException if <code>labelValue</code> is {@code null}.
   * @throws AssertionError if the actual {@link IndexDefinition} does contain the given label
   */

  public IndexDefinitionAssert doesNotHaveLabel(String labelValue) {
    Objects.instance().assertNotNull(info, actual);

    if (labelValue == null) {
      throw new IllegalArgumentException("The label value to look for should not be null");
    }
    if (actual.getLabel().name().equals(labelValue)) {
      throw Failures.instance().failure(info, shouldNotHaveLabel(actual, labelValue));
    }
    return this;
  }

}

