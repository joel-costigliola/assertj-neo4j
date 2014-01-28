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
package org.assertj.neo4j.api.executionresult;

import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.ResourceIterator;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.copyOfRange;
import static org.mockito.Mockito.when;

class ExecutionResultAssertTestUtils {

  private ExecutionResult executionResult;

  public ExecutionResultAssertTestUtils(ExecutionResult executionResult) {
    this.executionResult = executionResult;
  }

  @SuppressWarnings("unchecked")
  public void given_execution_result_yields_row_count(int rowCount) {
    Map<String, Object>[] rows = new Map[rowCount];
    for (int i = 0; i < rowCount; i++) {
      rows[i] = new HashMap<>();
    }

    given_execution_result_yields_rows(rows);
  }

  @SuppressWarnings("unchecked")
  public void given_execution_result_yields_rows(Map<String, Object>... resultRows) {
    given_execution_result_yields_rows_with_chained_calls(1, resultRows);
  }

  @SuppressWarnings("unchecked")
  public void given_execution_result_yields_rows_with_chained_calls(int chainedCallCount, Map<String, Object>... resultRows) {
    ResourceIterator<Map<String,Object>> iterator = executionResult.iterator();

    int rowCount = resultRows.length;
    when(iterator.hasNext()).thenReturn(Boolean.TRUE, subsequentHasNextReturnValues(rowCount, chainedCallCount));
    when(iterator.next()).thenReturn(resultRows[0], copyOfRange(resultRows, 1, rowCount));
  }

  private static Boolean[] subsequentHasNextReturnValues(int rowCount, int chainedCallCount) {
    int responseSize = subsequentHasNextCallCount(rowCount, chainedCallCount);
    Boolean[] hasNextValues = new Boolean[responseSize];
    for (int i = 0; i < responseSize-1; i++) {
      hasNextValues[i] = Boolean.TRUE;
    }
    hasNextValues[responseSize-1] = Boolean.FALSE;
    return hasNextValues;
  }

  /**
   * Returns the number of times hasNext should define a return value, given a number of rows returned by a query and
   * the number of times the testing assertions is chained.
   *
   * example with a result of 3 rows:
   *
   *  1 initial call of hasNext (true) + 5 subsequent calls (4 true and 1 false)
   *    assertThat(executionResult)
   *      .containsColumnNamesAtRow(0, "foo", "bar") // calls hasNext for element 0 and 1
   *      .containsColumnNamesAtRow(1, "foo", "bar") // calls hasNext for element 1 and 2
   *      .containsColumnNamesAtRow(2, "foo", "bar") // calls hasNext for element 2 and 3
   *
   *  1 initial call of hasNext (true) + 3 subsequent calls (2 true and 1 false)
   *    assertThat(executionResult)
   *      .containsColumnNamesAtRow(2, "foo", "bar") // calls hasNext for element 0, 1, 2 and 3
   */
  private static int subsequentHasNextCallCount(int rowCount, int chainedCallCount) {
    return chainedCallCount - 1 + rowCount;
  }

}
