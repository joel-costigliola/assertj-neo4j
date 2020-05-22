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
package org.assertj.neo4j.api.constraintdefinition;

import org.assertj.neo4j.api.ConstraintDefinitionAssert;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.schema.ConstraintDefinition;

import java.util.Arrays;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class ConstraintDefinitionAssert_hasPropertyKeys_Test {
	
	  private final ConstraintDefinition constraintDefinition = mock(ConstraintDefinition.class);
	  @Rule
	  public ExpectedException expectedException = ExpectedException.none();

	  @Test
	  public void should_pass_if_constraint_definition_has_iterable_property_keys() {
	    given_constraint_definition_with_property_keys("username", "dob");

	    Assert.assertThat(assertThat(constraintDefinition).hasPropertyKeys(Arrays.asList("username", "dob")), instanceOf(
	      ConstraintDefinitionAssert.class));
	  }

	  @Test
	  public void should_pass_if_constraint_definition_has_varargs_property_keys() {
	    given_constraint_definition_with_property_keys("username", "dob");

	    Assert.assertThat(assertThat(constraintDefinition).hasPropertyKeys("username", "dob"), instanceOf(
	      ConstraintDefinitionAssert.class));
	  }

	  @Test
	  public void should_fail_if_constraint_definition_is_null() {
	    expectedException.expect(AssertionError.class);
	    expectedException.expectMessage("Expecting actual not to be null");

	    assertThat((ConstraintDefinition) null).hasPropertyKeys(Arrays.asList("username", "dob"));
	  }

	  @Test
	  public void should_fail_if_constraint_definition_expected_iterable_property_keys_are_null() {
	    given_constraint_definition_with_property_keys("username", "dob");
	    expectedException.expect(IllegalArgumentException.class);
	    expectedException.expectMessage("The property keys to look for should not be null");

	    assertThat(constraintDefinition).hasPropertyKeys((Iterable<String>) null);
	  }

	  @Test
	  public void should_fail_if_constraint_expected_varargs_property_keys_are_null() {
	    given_constraint_definition_with_property_keys("username", "dob");
	    expectedException.expect(IllegalArgumentException.class);
	    expectedException.expectMessage("The property keys to look for should not be null");

	    assertThat(constraintDefinition).hasPropertyKeys((String[]) null);
	  }

	  @Test
	  public void should_fail_if_constraint_definition_does_not_have_all_expected_iterable_property_keys() {
	    given_constraint_definition_with_property_keys("username", "dob");
	    expectedException.expect(AssertionError.class);

	    assertThat(constraintDefinition).hasPropertyKeys(Arrays.asList("username", "password"));
	  }

	  @Test
	  public void should_fail_if_constraint_definition_does_not_have_all_expected_varargs_property_keys() {
	    given_constraint_definition_with_property_keys("username", "dob");
	    expectedException.expect(AssertionError.class);

	    assertThat(constraintDefinition).hasPropertyKeys("username", "password");
	  }

	  private void given_constraint_definition_with_property_keys(String... values) {
	    when(constraintDefinition.getPropertyKeys()).thenReturn(Arrays.asList(values));
	  }

}
