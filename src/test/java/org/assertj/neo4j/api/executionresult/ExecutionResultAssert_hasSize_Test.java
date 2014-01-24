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


import org.assertj.core.util.Maps;
import org.junit.Test;
import org.neo4j.cypher.javacompat.ExecutionResult;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Checks <code>{@link org.assertj.neo4j.api.ExecutionResultAssert#hasSize(int)}</code> behavior.
 *
 * @author Florent Biville
 */
public class ExecutionResultAssert_hasSize_Test {

  private ExecutionResult executionResult = mock(ExecutionResult.class, RETURNS_DEEP_STUBS);

  @Test
  public void should_pass_if_execution_result_has_size() {
    given_execution_result_yields_row_count(5);

    assertThat(executionResult).hasSize(5);
  }

  private void given_execution_result_yields_row_count(int rowCount) {
    Boolean[] booleans = new Boolean[rowCount];
    Map<String,Object>[] rows = new Map[rowCount];
    for (int i = 0; i < rowCount - 1; i++) {
      booleans[i] = Boolean.TRUE;
      rows[i] = new HashMap<>();
    }
    booleans[rowCount - 1] = Boolean.FALSE;

    when(executionResult.iterator().hasNext()).thenReturn(Boolean.TRUE, booleans);
    when(executionResult.iterator().next()).thenReturn(new HashMap<String,Object>(), rows);
  }
}
