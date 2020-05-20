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
package org.assertj.neo4j.api;

import org.junit.Test;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.IndexDefinition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.neo4j.api.InstanceOfAssertFactories.CONSTRAINT_DEFINITION;
import static org.assertj.neo4j.api.InstanceOfAssertFactories.INDEX_DEFINITION;
import static org.assertj.neo4j.api.InstanceOfAssertFactories.NODE;
import static org.assertj.neo4j.api.InstanceOfAssertFactories.PATH;
import static org.assertj.neo4j.api.InstanceOfAssertFactories.RELATIONSHIP;
import static org.assertj.neo4j.api.InstanceOfAssertFactories.RESULT;
import static org.assertj.neo4j.api.InstanceOfAssertFactories.propertyContainer;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;

public class InstanceOfAssertFactoriesTest {

  @Test
  public void property_container_factory_should_allow_property_container_assertions() {
    Object value = mock(PropertyContainer.class);

    PropertyContainerAssert<?, PropertyContainer> result = assertThat(value)
      .asInstanceOf(propertyContainer(PropertyContainer.class));

    result.doesNotHavePropertyKey("key");
  }

  @Test
  public void node_factory_should_allow_node_assertions() {
    Object value = mock(Node.class);

    NodeAssert result = assertThat(value).asInstanceOf(NODE);

    result.doesNotHaveLabel(Label.label("label"));
  }

  @Test
  public void relationship_factory_should_allow_relationship_assertions() {
    Object value = mock(Relationship.class, RETURNS_MOCKS);

    RelationshipAssert result = assertThat(value).asInstanceOf(RELATIONSHIP);

    result.doesNotHaveType("type");
  }

  @Test
  public void path_factory_should_allow_path_assertions() {
    Object value = mock(Path.class);

    PathAssert result = assertThat(value).asInstanceOf(PATH);

    result.hasLength(0);
  }

  @Test
  public void result_factory_should_allow_result_assertions() {
    Object value = mock(Result.class);

    ResultAssert result = assertThat(value).asInstanceOf(RESULT);

    result.isEmpty();
  }

  @SuppressWarnings("CastCanBeRemovedNarrowingVariableType")
  @Test
  public void constraint_definition_factory_should_allow_constraint_definition_assertions() {
    Object value = mock(ConstraintDefinition.class, RETURNS_DEEP_STUBS);
    given(((ConstraintDefinition) value).getLabel().name()).willReturn("label");

    ConstraintDefinitionAssert result = assertThat(value).asInstanceOf(CONSTRAINT_DEFINITION);

    result.hasLabel("label");
  }

  @Test
  public void index_definition_factory_should_allow_index_definition_assertions() {
    Object value = mock(IndexDefinition.class, RETURNS_MOCKS);

    IndexDefinitionAssert result = assertThat(value).asInstanceOf(INDEX_DEFINITION);

    result.doesNotHaveLabel("label");
  }

}
