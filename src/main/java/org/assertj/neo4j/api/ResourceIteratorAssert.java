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
import org.assertj.neo4j.error.ShouldContainEntry;
import org.neo4j.graphdb.ResourceIterator;

import static java.lang.String.format;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.neo4j.api.SearchErrorHelper.checkIndexAccess;
import static org.assertj.neo4j.api.SearchErrorHelper.checkIndexInBound;

/**
 * Assertions for Neo4J {@link org.neo4j.graphdb.ResourceIterator}
 * 
 * @author Florent Biville
 */
public class ResourceIteratorAssert<T> extends AbstractAssert<ResourceIteratorAssert<T>, ResourceIterator<T>> {

  private Integer previousRowIndex = null;

  public ResourceIteratorAssert(ResourceIterator<T> resourceIterator) {
    super(resourceIterator, ResourceIteratorAssert.class);
  }

  public ResourceIterator<T> getActual() {
    return actual;
  }

  /**
   * Verifies that the actual {@link org.neo4j.graphdb.ResourceIterator} has the given size<br/>
   * <strong>Warning</strong>: this consumes the entire iterator.<br/>
   * <p>
   * Example:
   * 
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * ResourceIterator&lt;String&gt; doughnutLoveLevelIterator = cypherExecutionEngine.execute(
   *     &quot;MATCH (:CHARACTER)-[l:LOVES]-&gt;(:DOUGHNUT) RETURN l.level AS level&quot;).columnAs(&quot;level&quot;);
   * 
   * assertThat(doughnutLoveLevelIterator).hasSize(3);
   * 
   * </pre>
   * 
   * If <code>expectedSize</code> is negative, an {@link IllegalArgumentException} is thrown.
   * <p>
   * 
   * @param expectedSize the expected size of the {@link org.neo4j.graphdb.ResourceIterator}
   * @return this {@link org.assertj.neo4j.api.ResourceIteratorAssert} for assertions chaining
   * 
   * @throws IllegalArgumentException if <code>expectedSize</code> is negative.
   * @throws AssertionError if the actual {@link org.neo4j.graphdb.ResourceIterator} has a size different from
   *           <code>expectedSize</code>.
   */
  public ResourceIteratorAssert<T> hasSize(int expectedSize) {
    Objects.instance().assertNotNull(info, actual);

    if (expectedSize < 0) {
      throw new IllegalArgumentException(format(
          "\nResourceIterator size should be positive, while expected size was:    <%s>", expectedSize));
    }

    int actualSize = computeActualSize();
    if (actualSize != expectedSize) {
      throw Failures.instance().failure(info, shouldHaveSameSizeAs(actual, actualSize, expectedSize));
    }

    return this;
  }

  /**
   * Verifies that the actual {@link org.neo4j.graphdb.ResourceIterator} contains the specified non-null value at the
   * given index<br/>
   * <strong>Warning: </strong>If you plan to chain this assertion, be sure that chained calls specify row indices in
   * increasing order!<br />
   * <p>
   * Example:
   * 
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * ResourceIterator&lt;String&gt; doughnutLoveLevelIterator = cypherExecutionEngine.execute(
   *     &quot;MATCH (:CHARACTER)-[l:LOVES]-&gt;(:DOUGHNUT) RETURN l.level AS level&quot;).columnAs(&quot;level&quot;);
   * 
   * assertThat(doughnutLoveLevelIterator).containsAtRow(0, &quot;A LOT&quot;).containsAtRow(1, &quot;A LITTLE&quot;);
   * 
   * </pre>
   * 
   * The following will <strong>NOT</strong> work:
   * 
   * <pre>
   * assertThat(doughnutLoveLevelIterator).containsAtRow(1, &quot;A LITTLE&quot;).containsAtRow(0, &quot;A LOT&quot;);
   * 
   * </pre>
   * 
   * If <code>rowIndex</code> is negative or <code>expectedValue</code> is null, an exception will be thrown.
   * <p>
   * 
   * @param rowIndex the position of the row to verify in the actual {@link org.neo4j.graphdb.ResourceIterator}
   * @param expectedValue the expected value to be contained in the iterator
   * @return this {@link org.assertj.neo4j.api.ResourceIteratorAssert} for assertions chaining
   * 
   * @throws IllegalArgumentException if <code>rowIndex</code> is negative.
   * @throws IllegalArgumentException if <code>expectedValue</code> is <code>null</code>.
   * @throws IllegalArgumentException if <code>rowIndex</code> has already been traversed during previous calls.
   * @throws AssertionError if the actual {@link org.neo4j.graphdb.ResourceIterator} does not contain the specified
   *           value at the given index.
   */
  public ResourceIteratorAssert<T> containsAtRow(int rowIndex, T expectedValue) throws IllegalArgumentException {
    Objects.instance().assertNotNull(info, actual);

    if (expectedValue == null) {
      throw new NullPointerException("\nExpecting value not to be null");
    }

    checkIndexAccess(rowIndex, previousRowIndex, this.getClass());
    SearchResultRow<T> result = search(rowIndex);
    checkIndexInBound(rowIndex, result.getVisitedLines(), info, actual);

    T actualValue = result.getValue();
    if (!expectedValue.equals(actualValue)) {
      throw Failures.instance().failure(info,
          ShouldContainEntry.shouldContainEntry(actual, rowIndex, expectedValue, actualValue));
    }

    return this;
  }

  private SearchResultRow<T> search(int rowIndex) {
    int visitedRowCount = previousRowIndex == null ? 0 : 1 + previousRowIndex;
    T actualValue = null;
    while (actual.hasNext() && visitedRowCount < rowIndex + 1) {
      actualValue = actual.next();
      visitedRowCount++;
    }
    previousRowIndex = rowIndex;
    return new SearchResultRow<T>(visitedRowCount, actualValue);
  }

  private int computeActualSize() {
    int actualSize = 0;
    while (actual.hasNext()) {
      actual.next();
      actualSize++;
    }
    return actualSize;
  }
}
