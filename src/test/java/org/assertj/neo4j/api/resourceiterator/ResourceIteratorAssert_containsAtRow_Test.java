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
package org.assertj.neo4j.api.resourceiterator;

import org.assertj.neo4j.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.ResourceIterator;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResourceIteratorAssert_containsAtRow_Test {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void should_pass_if_iterator_contains_specified_value_at_specified_row() {
    ResourceIterator<String> resourceIterator = mock(ResourceIterator.class);
    when(resourceIterator.hasNext()).thenReturn(
        true, true, // 1st iteration
        true, false // 2nd iteration
    );
    when(resourceIterator.next()).thenReturn("florent", "biville");

    Assertions.<String> assertThat(resourceIterator)
            .containsAtRow(0, "florent")
            .containsAtRow(1, "biville");
  }

  @Test
  public void should_fail_if_assertion_is_not_called_in_increasing_order() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("Subsequent ResourceIteratorAssert assertion calls should specify index in **increasing** order.\n" +
            "  Previous call specified index: <1>\n" +
            "  Current call specifies index: <0>\n" +
            "Current call index should be larger than the previous one.");

    ResourceIterator<String> resourceIterator = mock(ResourceIterator.class);
    when(resourceIterator.hasNext()).thenReturn(
        true, true, // 1st iteration
        true, false // 2nd iteration
    );
    when(resourceIterator.next()).thenReturn("florent", "biville");

    Assertions.<String> assertThat(resourceIterator)
            .containsAtRow(1, "biville")
            .containsAtRow(0, "florent");
  }

  @Test
  public void should_fail_if_row_index_is_negative() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The specified row index should be positive, but was <-1>.");

    assertThat(mock(ResourceIterator.class)).containsAtRow(-1, "flo");
  }

  @Test
  public void should_fail_if_given_row_does_not_contain_value() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("with row at index:\n"
        + "  <0>\n"
        + "to contain\n"
        + "  <\"biville\">\n"
        + "while row actually contains\n"
        + "  <\"florent\">"
    );

    ResourceIterator<String> resourceIterator = mock(ResourceIterator.class);
    when(resourceIterator.hasNext()).thenReturn(true, false);
    when(resourceIterator.next()).thenReturn("florent");

    Assertions.<String> assertThat(resourceIterator).containsAtRow(0, "biville");
  }
}
