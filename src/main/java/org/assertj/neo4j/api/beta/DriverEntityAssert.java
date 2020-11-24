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
package org.assertj.neo4j.api.beta;

import org.assertj.core.internal.Objects;
import org.assertj.neo4j.api.beta.util.Wip;
import org.neo4j.driver.types.Entity;
import org.neo4j.graphdb.PropertyContainer;

/**
 * Assertions for Neo4J {@link Entity}
 *
 * @author Florent Biville
 */
public class DriverEntityAssert<A extends DriverEntityAssert<A, T>, T extends Entity> extends DriverMapAccessorAssert<A, T> {

    protected DriverEntityAssert(T actual, Class<? extends A> assertClass) {
        super(actual, assertClass);
    }

    private static void checkPropertyValueIsNotNull(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("The value to look for should not be null");
        }
    }

    private static void checkPropertyKeyIsNotNull(String key) {
        if (key == null) {
            throw new IllegalArgumentException("The key to look for should not be null");
        }
    }

    public T getActual() {
        return actual;
    }

    /**
     * Verifies that the actual {@link PropertyContainer} has the given property key<br/>
     * <p>
     * Example:
     *
     * <pre>
     * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
     * Node node = graph.createNode();
     * node.setProperty(&quot;firstName&quot;, &quot;Homer&quot;);
     *
     * assertThat(node).hasPropertyKey(&quot;firstName&quot;);
     *
     * // it also works with relationships:
     * Relationship relationship = homer.createRelationshipTo(donut, RelationshipType.withName(&quot;LOVES&quot;));
     * relationship.setProperty(&quot;firstName&quot;, &quot;Homer&quot;);
     *
     * assertThat(relationship).hasPropertyKey(&quot;firstName&quot;);
     * </pre>
     *
     * If the given <code>key</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
     * <p>
     *
     * @param key the property key to look for in the actual {@link PropertyContainer}
     * @return this {@link DriverEntityAssert} for assertions chaining
     * @throws IllegalArgumentException if <code>key</code> is {@code null}.
     * @throws AssertionError           if the actual {@link PropertyContainer} does not have a property with the given
     *                                  key.
     */
    public A hasPropertyKey(String key) {
        Objects.instance().assertNotNull(info, actual);

        checkPropertyKeyIsNotNull(key);
        throw Wip.TODO(myself, "hasPropertyKey");
    }

    /**
     * Verifies that the actual {@link PropertyContainer} has the given property key with the given value<br/>
     * <p>
     * Example:
     *
     * <pre>
     * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
     * ResourceIterator&lt;String&gt; loveLevelIterator = myExecutionEngine.execute(
     *     &quot;MATCH (:CHARACTER)-[l:LOVES]-&gt;(:DOUGHNUT) RETURN l.level AS level&quot;).columnAs(&quot;level&quot;);
     *
     * assertThat(loveLevelIterator).hasSize(3);
     * </pre>
     *
     * If the given <code>size</code> is negative, an {@link IllegalArgumentException} is thrown.
     * <p>
     *
     * @param key   the property key to look for in the actual {@link PropertyContainer}
     * @param value the property value to look for in the actual {@link PropertyContainer}
     * @return this {@link DriverEntityAssert} for assertions chaining
     * @throws IllegalArgumentException if <code>key</code> is {@code null}.
     * @throws IllegalArgumentException if <code>value</code> is {@code null}.
     * @throws AssertionError           if the actual {@link PropertyContainer} does not have a property with given key
     *                                  and value.
     */
    public A hasProperty(String key, Object value) {
        hasPropertyKey(key);

        checkPropertyValueIsNotNull(value);
        throw Wip.TODO(myself, "hasProperty");
    }

    /**
     * Verifies that the actual {@link PropertyContainer} does not have the given property key<br/>
     * <p>
     * Example:
     *
     * <pre>
     * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
     * Node node = graph.createNode();
     * node.setProperty(&quot;firstName&quot;, &quot;Homer&quot;);
     *
     * assertThat(node).doesNotHavePropertyKey(&quot;lastName&quot;);
     *
     * // it also works with relationships:
     * Relationship relationship = homer.createRelationshipTo(donut, RelationshipType.withName(&quot;LOVES&quot;));
     * relationship.setProperty(&quot;firstName&quot;, &quot;Homer&quot;);
     *
     * assertThat(relationship).doesNotHavePropertyKey(&quot;lastName&quot;);
     * </pre>
     *
     * If the <code>key</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
     * <p>
     *
     * @param key the property key to look for in the actual {@link PropertyContainer}
     * @return this {@link DriverEntityAssert} for assertions chaining
     * @throws IllegalArgumentException if <code>key</code> is {@code null}.
     * @throws AssertionError           if the actual {@link PropertyContainer} has a property with given key.
     */
    public A doesNotHavePropertyKey(String key) {
        Objects.instance().assertNotNull(info, actual);

        checkPropertyKeyIsNotNull(key);
        throw Wip.TODO(myself, "doesNotHavePropertyKey");
    }

    /**
     * Verifies that the actual {@link PropertyContainer} does not have a property with given key and value.<br/>
     * <p>
     * Example:
     *
     * <pre>
     * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
     * Node node = graph.createNode();
     * node.setProperty(&quot;firstName&quot;, &quot;Homer&quot;);
     *
     * assertThat(node).doesNotHaveProperty(&quot;firstName&quot;, &quot;Bart&quot;);
     * assertThat(node).doesNotHaveProperty(&quot;lastName&quot;, &quot;Homer&quot;);
     *
     * // it also works with relationships:
     * Relationship relationship = homer.createRelationshipTo(donut, RelationshipType.withName(&quot;LOVES&quot;));
     * relationship.setProperty(&quot;firstName&quot;, &quot;Homer&quot;);
     *
     * assertThat(relationship).doesNotHaveProperty(&quot;firstName&quot;, &quot;Bart&quot;);
     * assertThat(relationship).doesNotHaveProperty(&quot;lastName&quot;, &quot;Homer&quot;);
     *
     * </pre>
     *
     * If any of the <code>key</code> or <code>value</code> is {@code null}, an {@link IllegalArgumentException} is
     * thrown.
     * <p>
     *
     * @param key   the property key to look for in the actual {@link PropertyContainer}
     * @param value the property value to look for in the actual {@link PropertyContainer}
     * @return this {@link DriverEntityAssert} for assertions chaining
     * @throws IllegalArgumentException if <code>key</code> is {@code null}.
     * @throws IllegalArgumentException if <code>value</code> is {@code null}.
     * @throws AssertionError           if the actual {@link PropertyContainer} has a property with given key and value.
     */
    public A doesNotHaveProperty(String key, Object value) {
        Objects.instance().assertNotNull(info, actual);

        checkPropertyKeyIsNotNull(key);
        checkPropertyValueIsNotNull(value);
        throw Wip.TODO(myself, "doesNotHaveProperty");
    }

}
