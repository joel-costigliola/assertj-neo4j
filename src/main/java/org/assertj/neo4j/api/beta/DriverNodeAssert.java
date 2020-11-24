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
package org.assertj.neo4j.api.beta;

import org.assertj.core.internal.Objects;
import org.assertj.neo4j.api.beta.util.Wip;
import org.neo4j.driver.types.Node;
import org.neo4j.graphdb.Label;

/**
 * Assertions for Neo4J {@link Node}
 *
 * @author Florent Biville
 */
public class DriverNodeAssert extends DriverEntityAssert<DriverNodeAssert, Node> {

  protected DriverNodeAssert(Node actual) {
    super(actual, DriverNodeAssert.class);
  }

  public Node getActual() {
    return actual;
  }

  /**
   * Verifies that the actual {@link Node} has the given label name<br/>
   * <p>
   * Example:
   *
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * Node node = graph.createNode();
   * node.addLabel(Label.label(&quot;DOUGHNUT_LOVER&quot;));
   *
   * assertThat(node).hasLabel(&quot;DOUGHNUT_LOVER&quot;);
   * </pre>
   *
   * If the <code>labelValue</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param labelValue the label name to look for in the actual {@link Node}
   * @return this {@link DriverNodeAssert} for assertions chaining
   *
   * @throws IllegalArgumentException if <code>labelValue</code> is {@code null}.
   * @throws AssertionError if the actual {@link Node} does not contain the given label
   */
  public DriverNodeAssert hasLabel(String labelValue) {
    Objects.instance().assertNotNull(info, actual);

    if (labelValue == null) {
      throw new IllegalArgumentException("The label value to look for should not be null");
    }
      throw Wip.TODO("DriverNodeAssert#hasLabel");
  }

  /**
   * Verifies that the actual {@link Node} does NOT have the given label name<br/>
   * <p>
   * Example:
   *
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * Node node = graph.createNode();
   * node.addLabel(Label.label(&quot;DOUGHNUT_LOVER&quot;));
   *
   * assertThat(node).doesNotHaveLabel(&quot;FRUIT_LOVER&quot;);
   * </pre>
   *
   * If the <code>labelValue</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param labelValue the label name to look for in the actual {@link Node}
   * @return this {@link DriverNodeAssert} for assertions chaining
   *
   * @throws IllegalArgumentException if <code>labelValue</code> is {@code null}.
   * @throws AssertionError if the actual {@link Node} does contain the given label
   */
  public DriverNodeAssert doesNotHaveLabel(String labelValue) {
    Objects.instance().assertNotNull(info, actual);

    if (labelValue == null) {
      throw new IllegalArgumentException("The label value to look for should not be null");
    }

      throw Wip.TODO("DriverNodeAssert#hasLabel");
  }

  /**
   * Verifies that the actual {@link Node} has the given label<br/>
   * <p>
   * Example:
   *
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * Node node = graph.createNode();
   * Label doughnutLover = Label.label(&quot;DOUGHNUT_LOVER&quot;);
   * node.addLabel(doughnutLover);
   *
   * assertThat(node).hasLabel(doughnutLover);
   * </pre>
   *
   * If the <code>label</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param label the label to look for in the actual {@link Node}
   * @return this {@link DriverNodeAssert} for assertions chaining
   *
   * @throws IllegalArgumentException if <code>label</code> is {@code null}.
   * @throws AssertionError if the actual {@link Node} does not contain the given label
   */
  public DriverNodeAssert hasLabel(Label label) {
    Objects.instance().assertNotNull(info, actual);

    if (label == null) {
      throw new IllegalArgumentException("The label to look for should not be null");
    }

      throw Wip.TODO("DriverNodeAssert#hasLabel");
  }

  /**
   * Verifies that the actual {@link Node} does NOT have the given label<br/>
   * <p>
   * Example:
   *
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * Node node = graph.createNode();
   * node.addLabel(Label.label(&quot;DOUGHNUT_LOVER&quot;));
   *
   * assertThat(node).doesNotHaveLabel(Label.label(&quot;FRUIT_LOVER&quot;));
   * </pre>
   *
   * If the <code>label</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param label the label to look for in the actual {@link Node}
   * @return this {@link DriverNodeAssert} for assertions chaining
   *
   * @throws IllegalArgumentException if <code>label</code> is {@code null}.
   * @throws AssertionError if the actual {@link Node} does contain the given label
   */
  public DriverNodeAssert doesNotHaveLabel(Label label) {
    Objects.instance().assertNotNull(info, actual);

    if (label == null) {
      throw new IllegalArgumentException("The label to look for should not be null");
    }

      throw Wip.TODO("DriverNodeAssert#hasLabel");
  }


}
