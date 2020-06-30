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

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.QueryStatistics;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.IndexDefinition;

/**
 * The entry point for all Neo4j assertions.
 *
 * @author Joel Costigliola
 * @author Florent Biville
 */
public class Assertions implements InstanceOfAssertFactories {

  /**
   * Creates a new </code>{@link Assertions}</code>.
   */
  protected Assertions() {
    // empty
  }

  public static <T extends PropertyContainer> PropertyContainerAssert<?, T> assertThat(T propertyContainer) {
    return new PropertyContainerAssert<>(propertyContainer, PropertyContainerAssert.class);
  }

  public static NodeAssert assertThat(Node node) {
    return new NodeAssert(node);
  }

  public static RelationshipAssert assertThat(Relationship relationship) {
    return new RelationshipAssert(relationship);
  }

  public static PathAssert assertThat(Path path) {
    return new PathAssert(path);
  }

  public static ResultAssert assertThat(Result result) {
    return new ResultAssert(result);
  }

  public static ConstraintDefinitionAssert assertThat(ConstraintDefinition constraintDefinition) {
    return new ConstraintDefinitionAssert(constraintDefinition);
  }

  public static IndexDefinitionAssert assertThat(IndexDefinition indexDefinition) {
    return new IndexDefinitionAssert(indexDefinition);
  }

  public static QueryStatisticsAssert assertThat(QueryStatistics queryStatistics) {
    return new QueryStatisticsAssert(queryStatistics);
  }
}
