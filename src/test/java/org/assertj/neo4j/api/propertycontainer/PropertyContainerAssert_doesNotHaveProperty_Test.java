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
package org.assertj.neo4j.api.propertycontainer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.PropertyContainer;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PropertyContainerAssert_doesNotHaveProperty_Test {

  private final PropertyContainer propertyContainer = mock(PropertyContainer.class);
  @Rule
  public ExpectedException expectedException = none();

  @Test
  public void should_pass_when_property_container_does_not_have_key_and_value() {
    given_property_container_with_key_and_value("firstName", "Emil Eifrem");

    assertThat(propertyContainer).doesNotHaveProperty("name", "Peter Neubauer");
  }

  @Test
  public void should_pass_when_property_container_has_key_but_NOT_value() {
    given_property_container_with_key_and_value("name", "Emil Eifrem");

    assertThat(propertyContainer).doesNotHaveProperty("name", "Peter Neubauer");
  }

  @Test
  public void should_pass_when_property_container_has_value_but_NOT_key() {
    given_property_container_with_key_and_value("name", "Emil Eifrem");

    assertThat(propertyContainer).doesNotHaveProperty("firstName", "Emil Eifrem");
  }

  @Test
  public void should_fail_when_given_property_key_is_null() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The key to look for should not be null");

    given_property_container_with_key_and_value("name", "Emil Eifrem");

    assertThat(propertyContainer).doesNotHaveProperty(null, "Emil Eifrem");
  }

  @Test
  public void should_fail_when_given_property_value_is_null() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The value to look for should not be null");

    given_property_container_with_key_and_value("name", "Emil Eifrem");

    assertThat(propertyContainer).doesNotHaveProperty("name", null);
  }

  @Test
  public void should_fail_when_property_container_has_key_AND_value() {
    expectedException.expect(AssertionError.class);

    given_property_container_with_key_and_value("name", "Emil Eifrem");

    assertThat(propertyContainer).doesNotHaveProperty("name", "Emil Eifrem");
  }

  private void given_property_container_with_key_and_value(String key, String value) {
    when(propertyContainer.hasProperty(key)).thenReturn(true);
    when(propertyContainer.getProperty(key, null)).thenReturn(value);
  }

}
