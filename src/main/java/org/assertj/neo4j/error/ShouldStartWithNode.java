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
package org.assertj.neo4j.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;

public class ShouldStartWithNode extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link ShouldStartWithNode}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param startNode the start node used in the failed assertion to compare the actual label value to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldStartWithNode(Relationship actual, Node startNode) {
    return new ShouldStartWithNode(actual, startNode);
  }

  /**
   * Creates a new </code>{@link ShouldStartWithNode}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param startNode the start node used in the failed assertion to compare the actual label value to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldStartWithNode(Path actual, Node startNode) {
    return new ShouldStartWithNode(actual, startNode);
  }

  private ShouldStartWithNode(Object actual, Node other) {
    super("\nExpecting:\n  <%s>\nto start with node:\n  <%s>\n%s", actual, other, StandardComparisonStrategy.instance());
  }
}
