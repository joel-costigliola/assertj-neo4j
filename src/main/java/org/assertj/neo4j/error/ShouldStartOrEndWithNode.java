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
package org.assertj.neo4j.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class ShouldStartOrEndWithNode extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link ShouldStartOrEndWithNode}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param node the node used in the failed assertion to compare the actual label value to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldStartOrEndWithNode(Relationship actual, Node node) {
    return new ShouldStartOrEndWithNode(actual, node);
  }

  private ShouldStartOrEndWithNode(Relationship actual, Node other) {
    super("\nExpecting %s\nto either start or end with node:\n  <%s>\n", textRepresentation(actual), other,
          StandardComparisonStrategy.instance());
  }

  private static CharSequence textRepresentation(Relationship actual) {
    return unquotedString(String.format("relationship with ID: %d and type: %s",
                         actual.getId(),
                         actual.getType().name()));
  }
}
