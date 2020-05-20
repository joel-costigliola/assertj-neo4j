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
package org.assertj.neo4j.api.result;

import org.assertj.neo4j.api.ResultAssert;
import org.junit.Assert;
import org.junit.Test;
import org.neo4j.graphdb.Result;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.assertj.core.util.Maps.newHashMap;
import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResultAssert_contains_Test {
  /*
   * This class basically makes sure <code>{@link IterableAssert}</code> is the superclass under the hood
   * while keeping the code coverage high enough.
   */
  @Test
  public void should_pass_if_result_contains_given_rows() {
    List<Map<String, Object>> actualResults = Collections.singletonList(newHashMap("foo", "fighters"));
    Result result = result(actualResults);

    Assert.assertThat(assertThat(result)
      .contains(newHashMap("foo", "fighters"))
      .doesNotContain(newHashMap("fou", "rire")), instanceOf(ResultAssert.class));
  }

  private Result result(Iterable<Map<String, Object>> rows) {
    Iterator<Map<String, Object>> iterator = rows.iterator();
    Result result = mock(Result.class);
    when(result.hasNext()).thenAnswer((ignored) -> iterator.hasNext());
    when(result.next()).thenAnswer((ignored) -> iterator.next());
    return result;
  }
}
