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

import org.assertj.core.api.Assert;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.IndexDefinition;

/**
 * Neo4j {@link InstanceOfAssertFactory InstanceOfAssertFactories} for {@link Assert#asInstanceOf(InstanceOfAssertFactory)}.
 *
 * @author Stefano Cordio
 * @since 2.0.2
 */
public interface InstanceOfAssertFactories {

  /**
   * {@link InstanceOfAssertFactory} for a {@link PropertyContainer}.
   *
   * @param <T>                   the {@code PropertyContainer} type.
   * @param propertyContainerType the property container type instance.
   * @return the factory instance.
   */
  static <T extends PropertyContainer> InstanceOfAssertFactory<T, PropertyContainerAssert<?, T>> propertyContainer(Class<T> propertyContainerType) {
    return new InstanceOfAssertFactory<>(propertyContainerType, Assertions::assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link Node}.
   */
  InstanceOfAssertFactory<Node, NodeAssert> NODE = new InstanceOfAssertFactory<>(Node.class, Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Relationship}.
   */
  InstanceOfAssertFactory<Relationship, RelationshipAssert> RELATIONSHIP = new InstanceOfAssertFactory<>(Relationship.class,
                                                                                                         Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Path}.
   */
  InstanceOfAssertFactory<Path, PathAssert> PATH = new InstanceOfAssertFactory<>(Path.class, Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Result}.
   */
  InstanceOfAssertFactory<Result, ResultAssert> RESULT = new InstanceOfAssertFactory<>(Result.class, Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link ConstraintDefinition}.
   */
  InstanceOfAssertFactory<ConstraintDefinition, ConstraintDefinitionAssert> CONSTRAINT_DEFINITION = new InstanceOfAssertFactory<>(ConstraintDefinition.class,
                                                                                                                                  Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@link IndexDefinition}.
   */
  InstanceOfAssertFactory<IndexDefinition, IndexDefinitionAssert> INDEX_DEFINITION = new InstanceOfAssertFactory<>(IndexDefinition.class,
                                                                                                                   Assertions::assertThat);

}
