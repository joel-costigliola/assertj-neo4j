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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.neo4j.api.InstanceOfAssertFactories.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.IndexDefinition;

/**
 * @author Stefano Cordio
 * @since 2.0.2
 */
public class InstanceOfAssertFactoriesTest {

  @Test
  public void property_container_factory_should_allow_property_container_assertions() {
    // GIVEN
    Object value = mock(PropertyContainer.class);
    // WHEN
    PropertyContainerAssert<?, PropertyContainer> result = assertThat(value).asInstanceOf(propertyContainer(PropertyContainer.class));
    // THEN
    result.doesNotHavePropertyKey("key");
  }

  @Test
  public void node_factory_should_allow_node_assertions() {
    // GIVEN
    Object value = mock(Node.class);
    // WHEN
    NodeAssert result = assertThat(value).asInstanceOf(NODE);
    // THEN
    result.doesNotHaveLabel(Label.label("label"));
  }

  @Test
  public void relationship_factory_should_allow_relationship_assertions() {
    // GIVEN
    Object value = mock(Relationship.class, RETURNS_MOCKS);
    // WHEN
    RelationshipAssert result = assertThat(value).asInstanceOf(RELATIONSHIP);
    // THEN
    result.doesNotHaveType("type");
  }

  @Test
  public void path_factory_should_allow_path_assertions() {
    // GIVEN
    Object value = mock(Path.class);
    // WHEN
    PathAssert result = assertThat(value).asInstanceOf(PATH);
    // THEN
    result.hasLength(0);
  }

  @Test
  public void result_factory_should_allow_result_assertions() {
    // GIVEN
    Object value = mock(Result.class);
    // WHEN
    ResultAssert result = assertThat(value).asInstanceOf(RESULT);
    // THEN
    result.isEmpty();
  }

  @SuppressWarnings("CastCanBeRemovedNarrowingVariableType")
  @Test
  public void constraint_definition_factory_should_allow_constraint_definition_assertions() {
    // GIVEN
    Object value = mock(ConstraintDefinition.class, RETURNS_DEEP_STUBS);
    given(((ConstraintDefinition) value).getLabel().name()).willReturn("label");
    // WHEN
    ConstraintDefinitionAssert result = assertThat(value).asInstanceOf(CONSTRAINT_DEFINITION);
    // THEN
    result.hasLabel("label");
  }

  @Test
  public void index_definition_factory_should_allow_index_definition_assertions() {
    // GIVEN
    Object value = mock(IndexDefinition.class, RETURNS_MOCKS);
    // WHEN
    IndexDefinitionAssert result = assertThat(value).asInstanceOf(INDEX_DEFINITION);
    // THEN
    result.doesNotHaveLabel("label");
  }

}
