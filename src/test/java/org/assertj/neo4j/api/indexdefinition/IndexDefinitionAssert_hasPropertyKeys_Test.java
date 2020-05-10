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
 * Copyright 2013-2019 the original author or authors.
 */
package org.assertj.neo4j.api.indexdefinition;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.schema.IndexDefinition;

import java.util.Arrays;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Checks <code>{@link org.assertj.neo4j.api.IndexDefinitionAssert#hasPropertyKeys}}</code> behavior.
 *
 * @author Rushi Chaudhari
 */
public class IndexDefinitionAssert_hasPropertyKeys_Test {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private IndexDefinition indexDefinition = mock(IndexDefinition.class);

  @Test
  public void should_pass_if_index_definition_has_iterable_property_keys() {
    given_index_definition_with_property_keys("name", "power");

    assertNotNull(assertThat(indexDefinition).hasPropertyKeys(Arrays.asList("name", "power")));
  }

  @Test
  public void should_pass_if_index_definition_has_varargs_property_keys() {
    given_index_definition_with_property_keys("name", "power");

    assertNotNull(assertThat(indexDefinition).hasPropertyKeys("name", "power"));
  }

  @Test
  public void should_fail_if_index_definition_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((IndexDefinition) null).hasPropertyKeys(Arrays.asList("name", "power"));
  }

  @Test
  public void should_fail_if_index_definition_expected_iterable_property_keys_are_null() {
    given_index_definition_with_property_keys("name", "power");
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The property keys to look for should not be null");

    assertThat(indexDefinition).hasPropertyKeys((Iterable<String>) null);
  }

  @Test
  public void should_fail_if_index_definition_expected_varargs_property_keys_are_null() {
    given_index_definition_with_property_keys("name", "power");
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The property keys to look for should not be null");

    assertThat(indexDefinition).hasPropertyKeys((String[]) null);
  }

  @Test
  public void should_fail_if_index_definition_does_not_have_all_expected_iterable_property_keys() {
    given_index_definition_with_property_keys("name", "power");
    expectedException.expect(AssertionError.class);

    assertThat(indexDefinition).hasPropertyKeys(Arrays.asList("name", "height"));
  }

  @Test
  public void should_fail_if_index_definition_does_not_have_all_expected_varargs_property_keys() {
    given_index_definition_with_property_keys("name", "power");
    expectedException.expect(AssertionError.class);

    assertThat(indexDefinition).hasPropertyKeys("name", "height");
  }

  private void given_index_definition_with_property_keys(String... values) {
    when(indexDefinition.getPropertyKeys()).thenReturn(Arrays.asList(values));
  }
}
