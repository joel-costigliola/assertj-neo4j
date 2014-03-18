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
import org.neo4j.graphdb.ResourceIterator;

public class ShouldContainEntry extends BasicErrorMessageFactory {
  /**
   * Creates a new </code>{@link org.assertj.neo4j.error.ShouldHaveProperty}</code>.
   * 
   * @param actual the actual iterator in the failed assertion.
   * @param rowIndex the specified position of the row
   * @param expectedValue the expected value contained in the row
   * @param actualValue the actual value contained in the row
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainEntry(ResourceIterator actual, int rowIndex, Object expectedValue,
      Object actualValue) {
    return new ShouldContainEntry(actual, rowIndex, expectedValue, actualValue);
  }

  /**
   * Creates a new </code>{@link org.assertj.core.error.BasicErrorMessageFactory}</code>.
   */
  private ShouldContainEntry(ResourceIterator actual, int rowIndex, Object expectedValue, Object actualValue) {
    super("\nExpecting:\n  <%s>\nwith row at index:\n  <%s>\nto contain\n  <%s>"
        + "\nwhile row actually contains\n  <%s>", actual, rowIndex, expectedValue, actualValue,
        StandardComparisonStrategy.instance());
  }
}
