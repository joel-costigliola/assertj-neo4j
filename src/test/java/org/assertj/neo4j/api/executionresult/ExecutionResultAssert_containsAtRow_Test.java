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

import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

/**
 * Checks <code>{@link org.assertj.neo4j.api.ExecutionResultAssert#containsAtRow(int, org.assertj.core.data.MapEntry...)}</code> behavior.
 *
 * @author Florent Biville
 */
public class ExecutionResultAssert_containsAtRow_Test {

  @Rule
  public ExpectedException exception = ExpectedException.none();
  private ExecutionResult executionResult = mock(ExecutionResult.class, RETURNS_DEEP_STUBS);
  private ExecutionResultAssertTestUtils utils = new ExecutionResultAssertTestUtils(executionResult);

  @Test
  public void should_pass_if_row_contains_entries() {
    Map<String, Object> firstRow = new HashMap<>();
    firstRow.put("firstName", "Florent");
    firstRow.put("lastName", "Biville");
    //noinspection unchecked
    utils.given_execution_result_yields_rows(firstRow);

    assertThat(executionResult).containsAtRow(0, entry("firstName", "Florent"), entry("lastName", "Biville"));
  }

  @Test
  public void should_pass_if_given_rows_contain_entries() {
    Map<String, Object> row = new HashMap<>();
    row.put("firstName", "Florent");
    row.put("lastName", "Biville");
    //noinspection unchecked
    utils.given_execution_result_yields_rows_with_chained_calls(2, row, row);

    assertThat(executionResult)
      .containsAtRow(0, entry("firstName", "Florent"))
      .containsAtRow(1, entry("lastName", "Biville"));
  }

  @Test
  public void should_fail_if_row_index_is_strictly_negative() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("The execution result row index should be positive.");

    assertThat(executionResult).containsAtRow(-1, entry("firstName", "Florent"));
  }

  @Test
  public void should_fail_if_no_entry_is_specified() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("There should be at least one entry to verify.");

    assertThat(executionResult).containsAtRow(0);
  }

  @Test
  public void should_fail_if_no_row_found_at_given_index() {
    exception.expect(AssertionError.class);
    exception.expectMessage("to include row at index:  <2>\n" +
      "but has size:  <2>");

    utils.given_execution_result_yields_row_count(2);

    assertThat(executionResult).containsAtRow(2, entry("firstName", "Florent"));
  }

  @Test
  public void should_fail_if_given_row_index_is_not_specified_in_increasing_order() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Subsequent ExecutionResultAssert assertion calls should specify index in **increasing** order.\n" +
      "  Previous call specified index: <1>\n" +
      "  Current call specifies index: <0>\n" +
      "Current call index should be larger than the previous one.");

    Map<String, Object> row = new HashMap<>();
    row.put("firstName", "Florent");
    row.put("lastName", "Biville");
    //noinspection unchecked
    utils.given_execution_result_yields_rows_with_chained_calls(2, row, row);

    assertThat(executionResult)
      .containsAtRow(1, entry("firstName", "Florent"))
      .containsAtRow(0, entry("lastName", "Biville"));
  }

  @Test
  public void should_fail_if_row_does_not_contain_column_name() {
    exception.expect(AssertionError.class);
    exception.expectMessage("with row at index:\n" +
      "  <1>\n" +
      "to contain\n" +
      "  <[MapEntry[key='firstName', value='Florent']]>\n" +
      "while row actually contains\n" +
      "  <{}>\n" +
      "could not find\n" +
      "  <[MapEntry[key='firstName', value='Florent']]>");

    utils.given_execution_result_yields_row_count(2);

    assertThat(executionResult).containsAtRow(1, entry("firstName", "Florent"));
  }

  @Test
  public void should_fail_if_row_does_not_contain_value() {
    exception.expect(AssertionError.class);
    exception.expectMessage("with row at index:\n" +
      "  <0>\n" +
      "to contain\n" +
      "  <[MapEntry[key='firstName', value='Florent']]>\n" +
      "while row actually contains\n" +
      "  <{'firstName'='Peter'}>\n" +
      "could not find\n" +
      "  <[MapEntry[key='firstName', value='Florent']]>");

    Map<String, Object> firstRow = new HashMap<>();
    firstRow.put("firstName", "Peter");
    //noinspection unchecked
    utils.given_execution_result_yields_rows(firstRow);

    assertThat(executionResult).containsAtRow(0, entry("firstName", "Florent"));
  }
}
