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
package org.assertj.neo4j.api.querystatistics;

import org.assertj.neo4j.api.QueryStatisticsAssert;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.neo4j.graphdb.QueryStatistics;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.when;

public class QueryStatisticsAssert_containsUpdate_Test {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  QueryStatistics mock = Mockito.mock(QueryStatistics.class);

  @Test
  public void should_pass_if_query_statistics_does_contain_updates() {
    when(mock.containsUpdates()).thenReturn(true);

    Assert.assertThat(assertThat(mock).containsUpdates(), instanceOf(QueryStatisticsAssert.class));
  }

  @Test
  public void should_fail_if_query_statistics_does_not_contain_updates() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual to contain updates but does not");
    when(mock.containsUpdates()).thenReturn(false);

    assertThat(mock).containsUpdates();
  }

  @Test
  public void should_fail_with_custom_message_if_query_statistics_does_not_contain_updates() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Custom error FTW");
    when(mock.containsUpdates()).thenReturn(false);

    assertThat(mock).overridingErrorMessage("Custom error FTW").containsUpdates();
  }

  @Test
  public void should_fail_if_query_statistics_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((QueryStatistics) null).containsUpdates();
  }

}
