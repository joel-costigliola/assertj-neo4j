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

import org.assertj.core.api.IterableAssert;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.neo4j.error.ShouldHaveRowAtIndex;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.ResourceIterator;

import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.assertj.neo4j.error.ShouldContainColumnNames.shouldContainColumnNames;

/**
 * Assertions for Neo4J {@link org.neo4j.cypher.javacompat.ExecutionResult}
 *
 * @author Florent Biville
 */
public class ExecutionResultAssert extends IterableAssert<Map<String, Object>> {

  private final ExecutionResult actual;
  private Integer previousRowIndex = null;

  protected ExecutionResultAssert(ExecutionResult actual) {
    super(actual);
    this.actual = actual;

  }

  public ExecutionResult getActual() {
    return actual;
  }

  /**
   * Verifies that the row specified at the given index of the actual {@link org.neo4j.cypher.javacompat.ExecutionResult}
   * contains the specified column names.<br/>
   *
   * <strong>Warning: </strong>If you plan to chain this assertion, be sure that chained calls specify row indices in increasing order!<br />
   *
   * <p>
   * Example:
   *
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * ExecutionEngine singletonExecutionEngine = new ExecutionEngine(graph);
   * // [...]
   * ExecutionResult result = singletonExecutionEngine.execute(&quot;MATCH (p:PEOPLE {firstName : 'Emil'}) RETURN p AS people&quot;);
   * assertThat(result)
   *  .containsColumnNamesAtRow(0, &quot;people&quot;)
   *  .containsColumnNamesAtRow(1, &quot;people&quot;);
   *
   * </pre>
   *
   * If any of the <code>key</code> or <code>value</code> is {@code null}, an {@link IllegalArgumentException} is
   * thrown.
   * <p>
   *
   * @param rowIndex the position of the row to verify in the actual {@link org.neo4j.cypher.javacompat.ExecutionResult}
   * @param columnNames the column names contained in the specified row
   * @return this {@link PropertyContainerAssert} for assertions chaining
   *
   * @throws IllegalArgumentException if <code>rowIndex</code> is strictly negative.
   * @throws IllegalArgumentException if <code>columnNames</code> is empty.
   * @throws IllegalArgumentException if <code>rowIndex</code> is smaller than the one given in the previous chained call.
   *
   * @throws AssertionError if the actual {@link org.neo4j.cypher.javacompat.ExecutionResult} row does not contain the given column names.
   */
  public ExecutionResultAssert containsColumnNamesAtRow(int rowIndex, String... columnNames) {
    Objects.instance().assertNotNull(info, actual);

    if (rowIndex < 0) {
      throw new IllegalArgumentException("The execution result row index should be positive.");
    }

    if (previousRowIndex != null && rowIndex <= previousRowIndex) {
      throw new IllegalArgumentException(String.format("\nSubsequent calls should specify index in **increasing** order." +
        "\n  Previous call specified index: <%d>" +
        "\n  Current call specifies index: <%d>" +
        "\nCurrent call index should be larger than the previous one.",
        previousRowIndex,
        rowIndex
      ));
    }

    if (columnNames == null || columnNames.length == 0) {
      throw new IllegalArgumentException("There should be at least one column name to verify");
    }

    SearchResult searchResult = search(rowIndex);
    Map<String, Object> row = searchResult.getRow();
    int rowCount = searchResult.getCount();
    if (rowIndex >= rowCount) {
      throw Failures.instance().failure(info, ShouldHaveRowAtIndex.shouldHaveRowAtIndex(actual, rowIndex, rowCount));
    }

    Set<String> actualColumnNames = row.keySet();
    if (!actualColumnNames.containsAll(asList(columnNames))) {
      throw Failures.instance().failure(info, shouldContainColumnNames(actual, rowIndex, row, columnNames));
    }

    previousRowIndex = rowIndex;
    return this;
  }

  private SearchResult search(int rowIndex) {
    ResourceIterator<Map<String, Object>> rowIterator = actual.iterator();
    int visitedRowCount = previousRowIndex == null ? 0 : 1 + previousRowIndex;
    Map<String, Object> row = null;
    while (rowIterator.hasNext() && visitedRowCount < rowIndex + 1) {
      row = rowIterator.next();
      visitedRowCount++;
    }
    return new SearchResult(visitedRowCount, row);
  }

  private class SearchResult {
    private final int count;
    private final Map<String,Object> row;

    public SearchResult(int count, Map<String, Object> row) {
      this.count = count;
      this.row = row;
    }

    public int getCount() {
      return count;
    }

    public Map<String, Object> getRow() {
      return row;
    }
  }
}
