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
package org.assertj.neo4j.api;

import org.junit.Test;
import org.neo4j.graphdb.index.Index;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for <code>{@link Assertions#assertThat(Index)}</code>
 *
 * @author Florent Biville
 * @author bel3atar
 */
public class Assertions_assertThat_with_Index_Test {

  @Test
  @SuppressWarnings("unchecked")
  public void should_create_Assert() {
    IndexAssert indexAssert = assertThat(mock(Index.class));
    assertNotNull(indexAssert);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void should_pass_actual() {
    Index index = mock(Index.class);
    IndexAssert indexAssert = assertThat(index);
    assertSame(index, indexAssert.getActual());
  }
}
