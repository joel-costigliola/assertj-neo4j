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
package org.assertj.neo4j.api.indexdefinition;

import org.assertj.neo4j.api.IndexDefinitionAssert;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.schema.IndexDefinition;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IndexDefinitionAssert_doesNotHaveLabel_Test {

  private final IndexDefinition indexDefinition = mock(IndexDefinition.class);
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void should_pass_if_index_definition_does_not_have_label() {
    given_index_definition_with_label("Gamora");

    Assert.assertThat(assertThat(indexDefinition).doesNotHaveLabel(Label.label("Hulk")), instanceOf(
      IndexDefinitionAssert.class));
  }

  @Test
  public void should_pass_if_index_definition_does_not_have_label_string() {
    given_index_definition_with_label("Gamora");

    Assert.assertThat(assertThat(indexDefinition).doesNotHaveLabel("Hulk"), instanceOf(
      IndexDefinitionAssert.class));
  }

  @Test
  public void should_fail_if_index_definition_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((IndexDefinition) null).doesNotHaveLabel(Label.label("Star Lord"));
  }

  @Test
  public void should_fail_if_index_definition_is_null_with_string() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((IndexDefinition) null).doesNotHaveLabel("Star Lord");
  }

  @Test
  public void should_fail_if_label_is_null() {
    expectedException.expect(IllegalArgumentException.class);

    assertThat(indexDefinition).doesNotHaveLabel((Label) null);
  }

  @Test
  public void should_fail_if_label_string_is_null() {
    expectedException.expect(IllegalArgumentException.class);

    assertThat(indexDefinition).doesNotHaveLabel((String) null);
  }

  @Test
  public void should_fail_if_index_definition_has_label() {
    expectedException.expect(AssertionError.class);

    given_index_definition_with_label("Nick Fury");

    assertThat(indexDefinition).doesNotHaveLabel(Label.label("Nick Fury"));
  }

  @Test
  public void should_fail_if_index_definition_has_label_string() {
    expectedException.expect(AssertionError.class);

    given_index_definition_with_label("Nick Fury");

    assertThat(indexDefinition).doesNotHaveLabel("Nick Fury");
  }

  private void given_index_definition_with_label(String value) {
    Label label = Label.label(value);
    when(indexDefinition.getLabel()).thenReturn(label);
  }
}

