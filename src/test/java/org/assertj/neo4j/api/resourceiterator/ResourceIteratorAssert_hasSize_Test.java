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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.ResourceIterator;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Checks <code>{@link org.assertj.neo4j.api.ResourceIteratorAssert#hasSize(int)} (int)}</code> behavior.
 * 
 * @author Florent Biville
 */
public class ResourceIteratorAssert_hasSize_Test {

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Test
  public void should_pass_if_passed_size_match() {
    ResourceIterator resourceIterator = mock(ResourceIterator.class);
    when(resourceIterator.hasNext()).thenReturn(true, true, false);
    when(resourceIterator.next()).thenReturn(1, 2);

    assertThat(resourceIterator).hasSize(2);
  }

  @Test
  public void should_fail_if_size_if_negative() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("\nResourceIterator size should be positive, while expected size was:    <-1>");

    assertThat(mock(ResourceIterator.class)).hasSize(-1);
  }

  @Test
  public void should_fail_if_size_does_not_match() {
    exception.expect(AssertionError.class);
    exception
        .expectMessage("\nActual and expected should have same size but actual size is:\n <0>\nwhile expected is:\n <32>");

    assertThat(mock(ResourceIterator.class)).hasSize(32);
  }
}
