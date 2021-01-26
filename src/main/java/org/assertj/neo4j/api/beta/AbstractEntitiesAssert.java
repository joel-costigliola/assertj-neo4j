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

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Iterables;
import org.assertj.core.util.Arrays;
import org.assertj.core.util.IterableUtil;
import org.assertj.core.util.Lists;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.neo4j.api.beta.error.ElementsShouldHavePropertyKeys;
import org.assertj.neo4j.api.beta.error.ElementsShouldHavePropertyWithType;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.AbstractDbData;
import org.assertj.neo4j.api.beta.type.DbValue;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.assertj.neo4j.api.beta.util.Entities;
import org.assertj.neo4j.api.beta.util.Wip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Abstract entities assertions.
 *
 * @param <SELF>    the current assertions type.
 * @param <DB_DATA> the database data type that will be load from the database
 * @param <ENTITY>  the entity type
 * @author patouche - 24/11/2020
 */
//@formatter:off
public abstract class AbstractEntitiesAssert<SELF extends AbstractEntitiesAssert<SELF, DB_DATA, ENTITY, ROOT_ASSERT>,
                                             DB_DATA extends AbstractDbData<ENTITY>,
                                             ENTITY extends DbEntity<ENTITY>,
                                             ROOT_ASSERT>
        extends AbstractAssert<SELF, List<ENTITY>>
        implements Navigable<SELF, ROOT_ASSERT> {
//@formatter:on

    protected Iterables iterables = Iterables.instance();

    /** The record type */
    protected final RecordType recordType;

    // FIXME: Should be remove ?
    /** The loading type. */
    protected final DB_DATA dbData;

    /** Factory for creating new assertions on restricted list of entities. */
    private EntitiesAssertFactory<SELF, ENTITY, ROOT_ASSERT> factory;

    /** Root assert. May be {@code null}. */
    protected final ROOT_ASSERT rootAssert;

    /** Previous assert. May be {@code null}. */
    protected final SELF parentAssert;

    /**
     * Factory for creating new {@link SELF} assertions with the another list of entities.
     *
     * @param <SELF>   the self type
     * @param <ENTITY> the entity type
     */
    @FunctionalInterface
    protected interface EntitiesAssertFactory<SELF extends Navigable<SELF, ROOT_ASSERT>, ENTITY, ROOT_ASSERT> {

        SELF create(final List<ENTITY> entities, final SELF current);

    }

    /**
     * Class constructor.
     *
     * @param recordType the record type
     * @param selfType   the self class type
     * @param dbData     the loading type
     * @param entities   the entities
     * @param rootAssert the parent entities for navigation
     */
    protected AbstractEntitiesAssert(
            final RecordType recordType, final Class<?> selfType, final DB_DATA dbData,
            final List<ENTITY> entities, final EntitiesAssertFactory<SELF, ENTITY, ROOT_ASSERT> factory,
            final SELF parentAssert, final ROOT_ASSERT rootAssert) {
        super(entities, selfType);
        this.recordType = recordType;
        this.dbData = dbData;
        this.factory = factory;
        this.parentAssert = parentAssert;
        this.rootAssert = rootAssert;
    }

    /** {@inheritDoc} */
    @Override
    public SELF toParentAssert() {
        throw Wip.TODO(this);
    }

    /** {@inheritDoc} */
    @Override
    public ROOT_ASSERT toRootAssert() {
        throw Wip.TODO(this);
    }

    /**
     * Get the actual entities for assertions.
     *
     * @return a list of entities.
     */
    @VisibleForTesting
    protected List<ENTITY> getActual() {
        return this.actual;
    }

    protected static <T> List<T> checkArray(final T[] items, String message) {
        if (Arrays.isNullOrEmpty(items)) {
            throw new IllegalArgumentException(message);
        }
        return java.util.Arrays.asList(items);
    }

    protected static <INSTANCE extends AbstractEntitiesAssert<INSTANCE, DB_DATA, ENTITY, ROOT_ASSERT>,
            DB_DATA extends AbstractDbData<ENTITY>,
            ENTITY extends DbEntity<ENTITY>,
            ROOT_ASSERT>
    ROOT_ASSERT rootAssert(INSTANCE parent) {
        return Optional.ofNullable(parent).map(i -> i.rootAssert).orElse(null);
    }

    /**
     * Verifies that actual entities (nodes or relationships) retrieve have the expected size.
     * <p/>
     * Example:
     * <pre><code class='java'> Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes).hasSize(12);
     * </code></pre>
     *
     * @param expectedSize the expected size
     * @return {@code this} assertion object.
     */
    public SELF hasSize(final int expectedSize) {
        iterables.assertHasSize(info, actual, expectedSize);
        return myself;
    }

    /**
     * Verifies that actual entities (nodes or relationships) contains the expected entities.
     * <p/>
     * Example:
     * <pre><code class='java'>
     * // assertions on nodes
     * Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes).contains(
     *   node().id(24)
     *         .property("name", "James Bond")
     *         .property("alias", "007")
     *         .build(),
     *   node().id(42)
     *         .property("name", "Miles Messervy")
     *         .property("alias", "M")
     *         .build()
     * );
     * </code></pre>
     *
     * @param expectedEntities the expected entities
     * @return {@code this} assertion object.
     * @throws AssertionError if one of the expected entities cannot be found in the actual list of entities
     */
    public SELF contains(final ENTITY... expectedEntities) {
        iterables.assertContains(info, actual, expectedEntities);
        return myself;
    }

    /**
     * Filtered entities to create a new {@link SELF} assertions.
     * <p/>
     * Example:
     * <pre><code class='java'> Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes)
     *   .hasSize(10)
     *   .filteredOn(n -> Objects.equals(n.getProperty("civility"), "Mrs"))
     *   .hasSize(5)
     * </code></pre>
     *
     * @param predicate the predicate that entities should match.
     * @return a new assertion object
     */
    public SELF filteredOn(final Predicate<ENTITY> predicate) {
        final List<ENTITY> entities = this.actual.stream().filter(predicate).collect(Collectors.toList());
        return factory.create(entities, myself);
    }

    public SELF filteredOnPropertyExists(String... keys) {
        throw Wip.TODO(this);
    }

    /**
     * Provide a easy way to create new assertions on the current list of {@link ENTITY} without specifying the {@link
     * ENTITY#getId()}
     * <p/>
     * With node entities:
     * <pre><code class='java'>
     * Nodes relationships = new Nodes(driver, "Developer");
     * assertThat(nodes)
     *     .ignoringIds()
     *     .contains(Drivers.node().label("Developer").build());</code></pre>
     * <p/>
     * With relationship entities:
     * <pre><code class='java'>
     * Relationships relationships = new Relationships(driver, "KNOW");
     * assertThat(relationships)
     *     .ignoringIds()
     *     .contains(Drivers.relation("KNOW").build());</code></pre>
     *
     * @return a new instance of {@link DriverNodesAssert}
     */
    public SELF ignoringIds() {
        final List<ENTITY> entities = actual.stream().map(DbEntity::withoutId).collect(Collectors.toList());
        return factory.create(entities, myself);
    }

    /**
     * Verifies that actual entities (nodes or relationships) have properties with the given {@code keys}.
     * <p/>
     * Example :
     * <pre><code class='java'> // assertions on nodes
     * Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes).havePropertyKeys("name", "dateOfBirth");
     *
     * // assertions on relationships
     * Relationships relationships = new Relationships(driver, "KNOWS");
     * assertThat(relationships).havePropertyKeys("since");</code></pre>
     *
     * @param expectedKeys the expected property keys the entities should have.
     * @return {@code this} assertion object.
     * @throws AssertionError if the actual value is not equal to the given one.
     */
    public SELF havePropertyKeys(final String... expectedKeys) {
        return havePropertyKeys(checkArray(expectedKeys, "The property keys to look for should not be null or empty"));
    }

    /**
     * Verifies that all actual entities (nodes or a relationships) have properties with the given {@code keys}.
     * <p/>
     * Example :
     * <pre><code class='java'> // assertions on nodes
     * Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes).havePropertyKeys(Arrays.asList("name", "dateOfBirth"));
     *
     * // assertions on relationships
     * Relationships relationships = new Relationships(driver, "KNOWS");
     * assertThat(relationships).havePropertyKeys(Collections.singletonList("since"));</code></pre>
     *
     * @param expectedKeys the expected property keys the entities should have.
     * @return {@code this} assertion object.
     * @throws AssertionError if the actual value is not equal to the given one.
     */
    public SELF havePropertyKeys(final Iterable<String> expectedKeys) {
        if (IterableUtil.isNullOrEmpty(expectedKeys)) {
            throw new IllegalArgumentException("The iterable of property keys to look for should not be empty");
        }
        if (!Entities.haveAllKeys(actual, expectedKeys)) {
            final ArrayList<String> keys = Lists.newArrayList(expectedKeys);
            throwAssertionError(ElementsShouldHavePropertyKeys.create(recordType, actual, keys));
        }
        return myself;
    }

    /**
     * Verifies that each actual entities have the expected number of properties.
     * <p/>
     * Example :
     * <pre><code class='java'> // assertions on nodes
     * Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes)
     *   .havePropertyNumber(8);
     * </code></pre>
     *
     * @param expectedNumberOfProperties the expected number of properties
     * @return {@code this} assertion object.
     * @throws AssertionError if one entities don't have the expected number of properties
     */
    public SELF havePropertyNumber(int expectedNumberOfProperties) {
        Wip.TODO(this);
        return myself;
    }

    /**
     * Verifies that each actual entities (nodes or a relationships) have a property {@code key} with the expected
     * type.
     * <p/>
     * Example :
     * <pre><code class='java'> // assertions on nodes
     * Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes)
     *   .havePropertyOfType("name", ValueType.STRING)
     *   .havePropertyOfType("dateOfBirth", ValueType.DATE);
     *
     * // assertions on relationships
     * Relationships relationships = new Relationships(driver, "KNOWS");
     * assertThat(relationships)
     *   .havePropertyOfType("since", ValueType.DATE);</code></pre>
     *
     * @param key          the property key all entities should have.
     * @param expectedType the expected type for the property
     * @return {@code this} assertion object.
     * @throws AssertionError if the actual value is not equal to the given one.
     */
    public SELF havePropertyType(final String key, final ValueType expectedType) {
        havePropertyKeys(key);
        final boolean b = this.actual.stream().map(e -> e.getPropertyType(key)).allMatch(t -> expectedType == t);
        if (!b) {
            throwAssertionError(ElementsShouldHavePropertyWithType.create(recordType, actual, key, expectedType));
        }
        return myself;
    }

    public SELF havePropertyType(final String key, final Class<?> expectedClass) {
        havePropertyKeys(key);
        Wip.TODO(this, "havePropertyType");
        return myself;
    }

    /**
     * Verifies that actual entities (nodes or a relationships) have a property {@code key} with the expected type.
     * <p/>
     * Example :
     * <pre><code class='java'> // assertions on nodes
     * Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes)
     *   .havePropertyOfType("name", ValueType.STRING)
     *   .havePropertyOfType("dateOfBirth", ValueType.DATE);
     *
     * // assertions on relationships
     * Relationships relationships = new Relationships(driver, "KNOWS");
     * assertThat(relationships)
     *   .havePropertyOfType("since", ValueType.DATE);</code></pre>
     *
     * @param key          the property key all entities should have.
     * @param expectedType the expected type for the property
     * @return {@code this} assertion object.
     * @throws AssertionError if the actual value is not equal to the given one.
     */
    public SELF haveListPropertyContainingType(final String key, final ValueType expectedType) {
        havePropertyKeys(key);
        havePropertyType(key, ValueType.LIST);
        final boolean b = this.actual.stream()
                .map(e -> e.getPropertyList(key))
                .flatMap(Collection::stream)
                .map(DbValue::getType)
                .allMatch(t -> expectedType == t);
        if (!b) {
            throwAssertionError(ElementsShouldHavePropertyWithType.create(recordType, actual, key, expectedType));
        }
        return myself;
    }

    public SELF havePropertyMatching(final String key, Predicate<Object> predicate) {
        Wip.TODO(this, "havePropertyMatching");
        return myself;
    }

    /**
     * Verifies that actual entities (nodes or a relationships) have a property {@code key} with the expected type.
     * <p/>
     * Example :
     * <pre><code class='java'> // assertions on nodes
     * Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes)
     *   .filteredOn(n -> Objects.equals(n.getPropertyValue("company"), "my-little-company"))
     *   .haveProperty("country", "FR");
     * </code></pre>
     *
     * @param key the property key
     * @param value the property value
     * @return
     */
    public SELF haveProperty(final String key, Object value) {
        Wip.TODO(this, "haveProperty");
        return myself;
    }

}
