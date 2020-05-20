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
package org.assertj.neo4j.api.path;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.Path;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PathAssert_hasLength_Test {

  private final Path path = mock(Path.class);
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void should_pass_if_path_has_length() {
    given_path_of_length(1);

    assertThat(path).hasLength(1);
  }

  @Test
  public void should_fail_if_path_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((Path) null).hasLength(0);
  }

  @Test
  public void should_fail_if_given_length_is_negative() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The path length to compare against should be positive");

    assertThat(path).hasLength(-1);
  }

  @Test
  public void should_fail_if_path_has_a_different_length() {
    given_path_of_length(1);

    expectedException.expect(AssertionError.class);

    assertThat(path).hasLength(2);
  }

  private void given_path_of_length(int length) {
    when(path.length()).thenReturn(length);
  }

}
