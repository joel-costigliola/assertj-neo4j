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

import org.junit.Test;
import org.neo4j.cypher.javacompat.ExecutionResult;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for <code>{@link org.assertj.neo4j.api.Assertions#assertThat(org.neo4j.cypher.javacompat.ExecutionResult)}</code>
 *
 * @author Florent Biville
 */
public class Assertions_assertThat_with_ExecutionResult_Test {

  @Test
  public void should_create_Assert() {
    ExecutionResultAssert executionResultAssert = assertThat(mock(ExecutionResult.class));
    assertNotNull(executionResultAssert);
  }

  @Test
  public void should_pass_actual() {
    ExecutionResult executionResult = mock(ExecutionResult.class);
    ExecutionResultAssert executionResultAssert = assertThat(executionResult);
    assertSame(executionResult, executionResultAssert.getActual());
  }
}
