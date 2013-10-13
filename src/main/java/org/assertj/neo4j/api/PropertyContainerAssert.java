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
 * Copyright 2013-2013 the original author or authors.
 */
package org.assertj.neo4j.api;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.neo4j.graphdb.PropertyContainer;

import static org.assertj.neo4j.error.ShouldHaveProperty.shouldHaveProperty;
import static org.assertj.neo4j.error.ShouldHavePropertyWithValue.shouldHavePropertyWithValue;
import static org.assertj.neo4j.error.ShouldNotHaveProperty.shouldNotHaveProperty;
import static org.assertj.neo4j.error.ShouldNotHavePropertyWithValue.shouldNotHavePropertyWithValue;

/**
 * Assertions for Neo4J {@link PropertyContainer}
 * 
 * @author Florent Biville
 */
public class PropertyContainerAssert extends AbstractAssert<PropertyContainerAssert, PropertyContainer> {

  protected PropertyContainerAssert(PropertyContainer actual) {
    super(actual, PropertyContainerAssert.class);
  }

  public PropertyContainer getActual() {
    return actual;
  }

  /**
   * Verifies that the actual {@link PropertyContainer} contains the given property key<br/>
   * <p>
   * Example:
   * 
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * Node actual = graph.createNode();
   * // it would also work for relationships:
   * // Relationship actual = homer.createRelationshipTo(donut, DynamicRelationshipType.withName(&quot;LOVES&quot;))
   * actual.setProperty(&quot;firstName&quot;, &quot;Homer&quot;);
   * 
   * assertThat(actual).hasProperty(&quot;firstName&quot;);
   * </pre>
   * 
   * If the given <code>key</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   * 
   * @param key the property key to look for in the actual {@link PropertyContainer}
   * @return this {@link PropertyContainerAssert} for assertions chaining
   * 
   * @throws IllegalArgumentException if <code>key</code> is {@code null}.
   * @throws AssertionError if the actual {@link PropertyContainer} does not contain any property key matching
   *           <code>key</code>.
   */
  public PropertyContainerAssert hasProperty(String key) {
    Objects.instance().assertNotNull(info, actual);

    if (key == null) {
      throw nullKeyException();
    }
    if (!actual.hasProperty(key)) {
      throw Failures.instance().failure(info, shouldHaveProperty(actual, key));
    }
    return this;
  }

  /**
   * Verifies that the actual {@link PropertyContainer} contains the given property key with the given value<br/>
   * <p>
   * Example:
   * 
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * Node actual = graph.createNode();
   * // it would also work for relationships:
   * // Relationship actual = homer.createRelationshipTo(donut, DynamicRelationshipType.withName(&quot;LOVES&quot;))
   * actual.setProperty(&quot;firstName&quot;, &quot;Homer&quot;);
   * 
   * assertThat(actual).hasProperty(&quot;firstName&quot;, &quot;Homer&quot;);
   * </pre>
   * 
   * If any of the <code>key</code> or <code>value</code> is {@code null}, an {@link IllegalArgumentException} is
   * thrown.
   * <p>
   * 
   * @param key the property key to look for in the actual {@link PropertyContainer}
   * @param value the property value to look for in the actual {@link PropertyContainer}
   * @return this {@link PropertyContainerAssert} for assertions chaining
   * 
   * @throws IllegalArgumentException if <code>key</code> is {@code null}.
   * @throws IllegalArgumentException if <code>value</code> is {@code null}.
   * @throws AssertionError if the actual {@link PropertyContainer} does not contain any property with key matching
   *           <code>key</code> or value matching <code>value</code>.
   */
  public PropertyContainerAssert hasProperty(String key, Object value) {
    hasProperty(key);

    if (value == null) {
      throw nullValueException();
    }
    if (!value.equals(actual.getProperty(key, null))) {
      throw Failures.instance().failure(info, shouldHavePropertyWithValue(actual, key, value));
    }
    return this;
  }

  /**
   * Verifies that the actual {@link PropertyContainer} does <strong>NOT</strong> contain the given property key<br/>
   * <p>
   * Example:
   * 
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * Node actual = graph.createNode();
   * // it would also work for relationships:
   * // Relationship actual = homer.createRelationshipTo(donut, DynamicRelationshipType.withName(&quot;LOVES&quot;))
   * actual.setProperty(&quot;firstName&quot;, &quot;Homer&quot;);
   * 
   * assertThat(actual).hasNotProperty(&quot;lastName&quot;);
   * </pre>
   * 
   * If the <code>key</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   * 
   * @param key the property key to look for in the actual {@link PropertyContainer}
   * @return this {@link PropertyContainerAssert} for assertions chaining
   * 
   * @throws IllegalArgumentException if <code>key</code> is {@code null}.
   * @throws AssertionError if the actual {@link PropertyContainer} does not contain any property key matching
   *           <code>key</code>.
   */
  public PropertyContainerAssert hasNotProperty(String key) {
    Objects.instance().assertNotNull(info, actual);

    if (key == null) {
      throw nullKeyException();
    }
    if (actual.hasProperty(key)) {
      throw Failures.instance().failure(info, shouldNotHaveProperty(actual, key));
    }
    return this;
  }

  /**
   * Verifies that the actual {@link PropertyContainer} contains the given property key <strong>NOT</strong> associated
   * with the given value<br/>
   * <p>
   * Example:
   * 
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * Node actual = graph.createNode();
   * // it would also work for relationships:
   * // Relationship actual = homer.createRelationshipTo(donut, DynamicRelationshipType.withName(&quot;LOVES&quot;))
   * actual.setProperty(&quot;firstName&quot;, &quot;Homer&quot;);
   * 
   * assertThat(actual).hasNotProperty(&quot;firstName&quot;, &quot;Bart&quot;);
   * </pre>
   * 
   * If any of the <code>key</code> or <code>value</code> is {@code null}, an {@link IllegalArgumentException} is
   * thrown.
   * <p>
   * 
   * @param key the property key to look for in the actual {@link PropertyContainer}
   * @param value the property value to look for in the actual {@link PropertyContainer}
   * @return this {@link PropertyContainerAssert} for assertions chaining
   * 
   * @throws IllegalArgumentException if <code>key</code> is {@code null}.
   * @throws IllegalArgumentException if <code>value</code> is {@code null}.
   * @throws AssertionError if the actual {@link PropertyContainer} does not contain any property key matching
   *           <code>key</code>.
   * @throws AssertionError if the actual {@link PropertyContainer} contains a property with the given <code>key</code>
   *           <strong>AND</strong> given <code>value</code>.
   */
  public PropertyContainerAssert hasNotProperty(String key, Object value) {
    hasProperty(key);

    if (value == null) {
      throw nullValueException();
    }
    if (value.equals(actual.getProperty(key, null))) {
      throw Failures.instance().failure(info, shouldNotHavePropertyWithValue(actual, key, value));
    }
    return this;
  }

  private IllegalArgumentException nullKeyException() {
    return new IllegalArgumentException("The key to look for should not be null");
  }

  private IllegalArgumentException nullValueException() {
    return new IllegalArgumentException("The value to look for should not be null");
  }
}
