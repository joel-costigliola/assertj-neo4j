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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.cypher.javacompat.ExecutionResult;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

/**
 * Checks <code>{@link org.assertj.neo4j.api.ExecutionResultAssert#containsColumnNamesAtRow(int, String...)}</code>
 * behavior.
 * 
 * @author Florent Biville
 */
public class ExecutionResultAssert_containsColumnNamesAtRow_Test {

  @Rule
  public ExpectedException exception = ExpectedException.none();
  private ExecutionResult executionResult = mock(ExecutionResult.class, RETURNS_DEEP_STUBS);
  private ExecutionResultAssertTestUtils utils = new ExecutionResultAssertTestUtils(executionResult);

  @Test
  public void should_pass_if_given_row_contains_columns() {
    Map<String, Object> firstRow = new HashMap<>();
    firstRow.put("firstName", "Florent");
    firstRow.put("lastName", "Biville");
    // noinspection unchecked
    utils.given_execution_result_yields_rows(firstRow);

    assertThat(executionResult).containsColumnNamesAtRow(0, "firstName", "lastName");
  }

  @Test
  public void should_pass_if_given_rows_contain_columns() {
    Map<String, Object> row = new HashMap<>();
    row.put("firstName", "Florent");
    row.put("lastName", "Biville");
    // noinspection unchecked
    utils.given_execution_result_yields_rows_with_chained_calls(2, row, row);

    assertThat(executionResult).containsColumnNamesAtRow(0, "firstName", "lastName").containsColumnNamesAtRow(1,
        "firstName", "lastName");
  }

  @Test
  public void should_fail_if_given_row_index_is_strictly_negative() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("The specified row index should be positive, but was <-1>.");

    assertThat(executionResult).containsColumnNamesAtRow(-1, "firstName", "lastName");
  }

  @Test
  public void should_fail_if_no_column_names_is_specified() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("There should be at least one column name to verify.");

    assertThat(executionResult).containsColumnNamesAtRow(2);
  }

  @Test
  public void should_fail_if_no_row_found_at_given_index() {
    exception.expect(AssertionError.class);
    exception.expectMessage("to include row at index:  <2>\n" + "but has size:  <2>");

    utils.given_execution_result_yields_row_count(2);

    assertThat(executionResult).containsColumnNamesAtRow(2, "firstName", "lastName");
  }

  @Test
  public void should_fail_if_given_row_index_is_not_specified_in_increasing_order() {
    exception.expect(IllegalArgumentException.class);
    exception
        .expectMessage("Subsequent ExecutionResultAssert assertion calls should specify index in **increasing** order.\n"
            + "  Previous call specified index: <1>\n"
            + "  Current call specifies index: <0>\n"
            + "Current call index should be larger than the previous one.");

    Map<String, Object> row = new HashMap<>();
    row.put("firstName", "Florent");
    row.put("lastName", "Biville");
    // noinspection unchecked
    utils.given_execution_result_yields_rows_with_chained_calls(2, row, row);

    assertThat(executionResult).containsColumnNamesAtRow(1, "firstName", "lastName").containsColumnNamesAtRow(0,
        "firstName", "lastName");
  }

  @Test
  public void should_fail_if_row_does_not_contain_given_column_names() {
    exception.expect(AssertionError.class);
    exception.expectMessage("with row at index:\n" + "  <1>\n" + "to contain column names\n"
        + "  <['firstName', 'lastName']>\n" + "but row actually contains\n" + "  <{}>");

    utils.given_execution_result_yields_row_count(2);

    assertThat(executionResult).containsColumnNamesAtRow(1, "firstName", "lastName");
  }
}
