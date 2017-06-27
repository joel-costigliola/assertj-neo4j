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
package org.assertj.neo4j.error;

import java.util.Map;
import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.neo4j.graphdb.Result;

public class ShouldContainColumnNames extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link ShouldContainColumnNames}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param rowIndex the specified position of the row
   * @param row the row used in the failed assertion to find the column names/values.
   * @param columnNames the expected column names included by the row.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainColumnNames(Result actual, int rowIndex,
                                                             Map<String, Object> row, String... columnNames) {
    return new ShouldContainColumnNames(actual, rowIndex, row, columnNames);
  }

  private ShouldContainColumnNames(Result actual, int rowIndex, Map<String, Object> row, String... columnNames) {
    super("\nExpecting:\n  <%s>\nwith row at index:\n  <%s>\nto contain column names\n  <%s>"
        + "\nbut row actually contains\n  <%s>", actual, rowIndex, columnNames, row, StandardComparisonStrategy
        .instance());
  }
}
