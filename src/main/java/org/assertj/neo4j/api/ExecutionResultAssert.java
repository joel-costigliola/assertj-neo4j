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
 * Copyright 2013-2014 the original author or authors.
 */
package org.assertj.neo4j.api;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.error.ShouldHaveSize;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.ResourceIterator;

import java.util.Map;

/**
 * Assertions for Neo4J {@link org.neo4j.cypher.javacompat.ExecutionResult}
 *
 * @author Florent Biville
 */
public class ExecutionResultAssert extends AbstractAssert<ExecutionResultAssert, ExecutionResult> {

  protected ExecutionResultAssert(ExecutionResult actual) {
    super(actual, ExecutionResultAssert.class);
  }

  public ExecutionResult getActual() {
    return actual;
  }

  /**
   * Verifies that the execution result row count equals the given one<br/>
   * This consumes the whole iterator and therefore <strong>cannot be chained</strong>.
   * <p>
   * Example:
   *
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * // [...]
   * ExecutionResult simpsonQueryResult = myExecutionEngine.execute("MATCH (s:SIMPSON) RETURN s");
   *
   * // true story! http://upload.wikimedia.org/wikipedia/commons/a/ad/Simpson_familt_tree.PNG
   * assertThat(simpsonQueryResult).hasSize(42);
   * </pre>
   *
   * If the <code>rowCount</code> is strictly negative, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param rowCount the expected row count of the {@link org.neo4j.cypher.javacompat.ExecutionResult} wrapper
   * @return this {@link org.assertj.neo4j.api.ExecutionResultAssert} for assertions chaining
   *
   * @throws IllegalArgumentException if <code>length</code> is strictly negative.
   * @throws AssertionError if the actual {@link org.neo4j.graphdb.Path} has a different length
   */
  public ExecutionResultAssert hasSize(int rowCount) {
    Objects.instance().assertNotNull(info, actual);

    if (rowCount < 0) {
      throw new IllegalArgumentException("The execution result row count to compare against should be positive.");
    }

    int actualSize = sizeOf(actual.iterator());
    if (actualSize != rowCount) {
      throw Failures.instance().failure(info, ShouldHaveSize.shouldHaveSize(actual, actualSize, rowCount));
    }
    return this;
  }

  private static int sizeOf(ResourceIterator<Map<String, Object>> iterator) {
    int count = 0;
    while (iterator.hasNext()) {
      iterator.next();
      count++;
    }
    return count;
  }
}
