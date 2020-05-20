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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.schema.IndexDefinition;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Checks <code>{@link org.assertj.neo4j.api.IndexDefinitionAssert#hasLabel(String)}</code> behavior.
 *
 * @author Agathe Vaisse
 * @author Gwenaelle Rispal
 */

public class IndexDefinitionAssert_hasLabel_represented_as_string_Test {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private IndexDefinition indexDefinition = mock(IndexDefinition.class);

  @Test
  public void should_pass_if_index_definition_has_label() {
    given_index_definition_with_label("Loki");

    assertNotNull(assertThat(indexDefinition).hasLabel("Loki"));
  }

  @Test
  public void should_fail_if_index_definition_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((IndexDefinition) null).hasLabel("Hawkeye");
  }

  @Test
  public void should_fail_if_label_value_is_null() {
    expectedException.expect(IllegalArgumentException.class);

    assertThat(indexDefinition).hasLabel((String) null);
  }

  @Test
  public void should_fail_if_index_definition_does_NOT_have_expected_label() {
    given_index_definition_with_label("Doctor Strange");
    expectedException.expect(AssertionError.class);

    assertThat(indexDefinition).hasLabel("Ant Man");

  }

  private void given_index_definition_with_label(String value) {
    Label label = Label.label(value);
    when(indexDefinition.getLabel()).thenReturn(label);
  }
}
