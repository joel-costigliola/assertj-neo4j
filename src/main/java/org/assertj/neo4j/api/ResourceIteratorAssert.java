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
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.neo4j.graphdb.ResourceIterator;

import static java.lang.String.format;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;

/**
 * Assertions for Neo4J {@link org.neo4j.graphdb.ResourceIterator}
 * 
 * @author Florent Biville
 */
public class ResourceIteratorAssert<T> extends AbstractAssert<ResourceIteratorAssert<T>, ResourceIterator<T>> {

  public ResourceIteratorAssert(ResourceIterator<T> resourceIterator) {
    super(resourceIterator, ResourceIteratorAssert.class);
  }

  public ResourceIterator<T> getActual() {
    return actual;
  }

  /**
   * Verifies that the actual {@link org.neo4j.graphdb.ResourceIterator} has the given size.<br/>
   * <b>WARNING</b>: this consumes the entire iterator.<br/>
   * <p>
   * Example:
   * 
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * ResourceIterator&lt;String&gt; doughnutLoveLevelIterator = cypherExecutionEngine.execute(
   *    "MATCH (:CHARACTER)-[l:LOVES]->(:DOUGHNUT) RETURN l.level AS level"
   * ).columnAs("level");
   * 
   * assertThat(doughnutLoveLevelIterator).hasSize(3);
   * 
   * </pre>
   * 
   * If any of the <code>key</code> or <code>value</code> is {@code null}, an {@link IllegalArgumentException} is
   * thrown.
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

  private int computeActualSize() {
    int actualSize = 0;
    while (actual.hasNext()) {
      actual.next();
      actualSize++;
    }
    return actualSize;
  }
}
