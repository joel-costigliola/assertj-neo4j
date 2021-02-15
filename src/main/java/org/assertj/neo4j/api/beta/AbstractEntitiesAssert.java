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
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.ShouldContain;
import org.assertj.core.error.ShouldHaveSize;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.neo4j.api.beta.error.ShouldHavePropertyInstanceOf;
import org.assertj.neo4j.api.beta.error.ShouldHavePropertyKeys;
import org.assertj.neo4j.api.beta.error.ShouldHavePropertyListOfType;
import org.assertj.neo4j.api.beta.error.ShouldHavePropertySize;
import org.assertj.neo4j.api.beta.error.ShouldHavePropertyValue;
import org.assertj.neo4j.api.beta.error.ShouldHavePropertyValueType;
import org.assertj.neo4j.api.beta.error.ShouldPropertyMatch;
import org.assertj.neo4j.api.beta.error.ShouldQueryResultBeEmpty;
import org.assertj.neo4j.api.beta.error.ShouldQueryResultBeNotEmpty;
import org.assertj.neo4j.api.beta.type.DataLoader;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.assertj.neo4j.api.beta.util.Checks;
import org.assertj.neo4j.api.beta.util.EntityRepresentation;
import org.assertj.neo4j.api.beta.util.Predicates;
import org.assertj.neo4j.api.beta.util.Presentations;
import org.assertj.neo4j.api.beta.util.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Abstract entities assertions.
 * <p/>
 * This class don't extends {@link org.assertj.core.api.AbstractIterableAssert} to offer the navigation on all kind of
 * {@link #filteredOn(Predicate)} methods. Each methods that will return a new {@link NEW_SELF} create a new assertion
 * which will be the same kind as {@link SELF}
 *
 * @param <SELF>          the current assertions type.
 * @param <ENTITY>        the entity type
 * @param <PARENT_ASSERT> the parent assertions type
 * @param <ROOT_ASSERT>   the root assertions type
 * @author Patrick Allain - 24/11/2020
 */
//@formatter:off
public abstract class AbstractEntitiesAssert<SELF extends AbstractEntitiesAssert<SELF,
                                                                                 ENTITY,
                                                                                 NEW_SELF,
                                                                                 PARENT_ASSERT,
                                                                                 ROOT_ASSERT>,
                                             ENTITY extends DbEntity,
                                             NEW_SELF extends Navigable<SELF, ROOT_ASSERT>,
                                             PARENT_ASSERT extends ParentAssert,
                                             ROOT_ASSERT>
        extends AbstractAssert<SELF, List<ENTITY>>
        implements Navigable<PARENT_ASSERT, ROOT_ASSERT>, ParentAssert {
//@formatter:on

    /** The record type */
    protected final RecordType recordType;

    /** The data loader. */
    protected final DataLoader<ENTITY> dataLoader;

    protected final Iterables iterables = Iterables.instance();

    // /** Comparison strategy. */
    protected final ComparisonStrategy comparisonStrategy = StandardComparisonStrategy.instance();

    /** True if comparison should be made ignoring ids. */
    /** TODO: Check if it's possible to use a ComparisonStrategy instead. */
    protected final boolean ignoreIds;

    /** Factory for creating new assertions on restricted list of entities. */
    private final EntitiesAssertFactory<SELF, ENTITY, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT> factory;

    /** Root assert. May be {@code null}. */
    private final ROOT_ASSERT rootAssert;

    /** Previous assert. May be {@code null}. */
    protected final PARENT_ASSERT parentAssert;

    /**
     * Class constructor.
     *
     * @param recordType the record type
     * @param selfType   the self class type
     * @param dataLoader the data loader
     * @param entities   the entities
     * @param rootAssert the parent entities for navigation
     */
    protected AbstractEntitiesAssert(
            final RecordType recordType,
            final Class<?> selfType,
            final DataLoader<ENTITY> dataLoader,
            final List<ENTITY> entities,
            final boolean ignoreIds,
            final EntitiesAssertFactory<SELF, ENTITY, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT> factory,
            final PARENT_ASSERT parentAssert,
            final ROOT_ASSERT rootAssert) {
        super(entities, selfType);
        this.recordType = Objects.requireNonNull(recordType);
        this.dataLoader = dataLoader;
        this.ignoreIds = ignoreIds;
        this.factory = Objects.requireNonNull(factory);
        this.parentAssert = parentAssert;
        this.rootAssert = rootAssert;
    }

    /** {@inheritDoc} */
    @Override
    public PARENT_ASSERT toParentAssert() {
        return this.parentAssert;
    }

    public Optional<ROOT_ASSERT> rootAssert() {
        return Optional.ofNullable(this.rootAssert);
    }

    /** {@inheritDoc} */
    @Override
    public Representation representation() {
        return info.representation();
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
    public NEW_SELF ignoringIds() {
        return factory.create(actual, dataLoader, true, myself);
    }

    /**
     * Retrieve the entity ids.
     *
     * @return the entity ids.
     */
    protected List<Long> entityIds() {
        return actual.stream().map(DbEntity::getId).collect(Collectors.toList());
    }

    /**
     * Ensure all entities verify the provided predicate. If not, the {@code callback} will be invoke to create a new
     * instance of a {@link ErrorMessageFactory}
     *
     * @param predicate the predicate that all entities should validate
     * @param callback  the callback to create the {@link ErrorMessageFactory}
     * @return {@code this} assertion object.
     */
    protected SELF shouldAllVerify(final Predicate<ENTITY> predicate,
                                   final ErrorMessageCallback<ENTITY> callback) {
        isNotEmpty();
        final List<ENTITY> notSatisfies = actual.stream().filter(predicate.negate()).collect(Collectors.toList());
        if (!notSatisfies.isEmpty()) {
            throwAssertionError(callback.create(notSatisfies));
        }
        return myself;
    }

    /**
     * Verifies that actual entities (nodes or relationships) retrieve from the data loader query is empty.
     * <p/>
     * Example:
     * <pre><code class='java'> Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes).isEmpty();
     * </code></pre>
     *
     * @return {@code this} assertion object.
     */
    public SELF isEmpty() {
        return isEmpty((entities) -> ShouldQueryResultBeEmpty.create(entities, dataLoader.query()));
    }

    protected SELF isEmpty(final ErrorMessageCallback<ENTITY> callback) {
        if (!actual.isEmpty()) {
            throwAssertionError(callback.create(actual));
        }
        return myself;
    }

    /**
     * Verifies that actual entities (nodes or relationships) retrieve from the data loader query is not empty.
     * <p/>
     * Example:
     * <pre><code class='java'> Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes).isNotEmpty();
     * </code></pre>
     *
     * @return {@code this} assertion object.
     */
    public SELF isNotEmpty() {
        if (actual.isEmpty()) {
            throwAssertionError(ShouldQueryResultBeNotEmpty.create(recordType, dataLoader.query()));
        }
        return myself;
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
    public NEW_SELF filteredOn(final Predicate<ENTITY> predicate) {
        final List<ENTITY> entities = this.actual.stream().filter(predicate).collect(Collectors.toList());
        return factory.create(entities, dataLoader, ignoreIds, myself);
    }

    /**
     * Filtered entities having existing properties to create a new {@link SELF} assertions.
     * <p/>
     * Example:
     * <pre><code class='java'> Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes)
     *   .hasSize(10)
     *   .filteredOnPropertyExists("name", "civility")
     *   .hasSize(5)
     * </code></pre>
     *
     * @param keys the keys that entities all entities should have
     * @return a new assertion object
     */
    public NEW_SELF filteredOnPropertyExists(final String... keys) {
        return filteredOnPropertyExists(Checks.notNullOrEmpty(keys, "The property keys should not be null or empty"));
    }

    /**
     * Filtered entities having existing properties to create a new {@link SELF} assertions.
     * <p/>
     * Example:
     * <pre><code class='java'> Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes)
     *   .hasSize(10)
     *   .filteredOnPropertyExists(Arrays.asList("name", "civility"))
     *   .hasSize(5)
     * </code></pre>
     *
     * @param keys the keys that entities all entities should have
     * @return a new assertion object
     */
    public NEW_SELF filteredOnPropertyExists(final Iterable<String> keys) {
        Checks.nonNullElementsIn(keys, "The iterable of property keys should not be empty");
        return filteredOn(Predicates.propertyKeysExists(keys));
    }

    /**
     * Filtered entities on a property value to create a new {@link SELF} assertions.
     * <p/>
     * Example:
     * <pre><code class='java'> Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes)
     *   .hasSize(10)
     *   .filteredOnPropertyValue("name", "civility")
     *   .hasSize(5)
     * </code></pre>
     *
     * @param key   the property key that the filtered entities will have
     * @param value the property value that the filtered entities will have
     * @return a new assertion object
     */
    public NEW_SELF filteredOnPropertyValue(final String key, final Object value) {
        return filteredOn(Predicates.propertyValue(
                Objects.requireNonNull(key, "The property key cannot be null"),
                value
        ));
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
        return havePropertyKeys(Utils.listOf(expectedKeys));
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
        final List<String> keys = Checks
                .nonNullElementsIn(expectedKeys, "The property keys should not be null or empty");
        return shouldAllVerify(
                Predicates.propertyKeysExists(keys),
                (notSatisfies) -> ShouldHavePropertyKeys.elements(actual, keys).notSatisfies(notSatisfies)
        );
    }

    /**
     * Verifies that each actual entities have the expected size of properties.
     * <p/>
     * Example :
     * <pre><code class='java'> // assertions on nodes
     * Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes)
     *   .havePropertySize(8);
     * </code></pre>
     *
     * @param expectedSize the expected size of properties
     * @return {@code this} assertion object.
     * @throws AssertionError if one entities don't have the expected size of properties
     */
    public SELF havePropertySize(final int expectedSize) {
        return shouldAllVerify(
                Predicates.propertySize(expectedSize),
                (notSatisfies) -> ShouldHavePropertySize.elements(actual, expectedSize).notSatisfies(notSatisfies)
        );
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
     * @param expectedType the expected value type for the property
     * @return {@code this} assertion object.
     * @throws AssertionError if the actual value is not equal to the given one.
     */
    public SELF havePropertyOfType(final String key, final ValueType expectedType) {
        Objects.requireNonNull(key, "The property key shouldn't be null");
        final ValueType type = Objects.requireNonNull(expectedType, "The expected value type shouldn't be null");
        havePropertyKeys(key);
        return shouldAllVerify(
                Predicates.propertyValueType(key, type),
                (notSatisfies) -> ShouldHavePropertyValueType.elements(actual, key, type).notSatisfies(notSatisfies)
        );
    }

    /**
     * Verifies that each actual entities (nodes or a relationships) have a property {@code key} with the expected
     * type.
     * <p/>
     * Example :
     * <pre><code class='java'> // assertions on nodes
     * Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes)
     *   .havePropertyInstanceOf("name", String.class)
     *   .havePropertyInstanceOf("dateOfBirth", LocalDate.class);
     *
     * // assertions on relationships
     * Relationships relationships = new Relationships(driver, "KNOWS");
     * assertThat(relationships)
     *   .havePropertyInstanceOf("since", LocalDate.class);</code></pre>
     *
     * @param key           the property key all entities should have.
     * @param expectedClass the expected class for the property value
     * @return {@code this} assertion object.
     * @throws AssertionError if the actual value is not equal to the given one.
     */
    public SELF havePropertyInstanceOf(final String key, final Class<?> expectedClass) {
        Objects.requireNonNull(key, "The property key shouldn't be null");
        final Class<?> clazz = Objects.requireNonNull(expectedClass, "The expected class shouldn't be null");
        havePropertyKeys(key);
        return shouldAllVerify(
                Predicates.propertyValueInstanceOf(key, clazz),
                (notSatisfies) -> ShouldHavePropertyInstanceOf.elements(actual, key, clazz).notSatisfies(notSatisfies)
        );
    }

    /**
     * Verifies that all actual entities (nodes or a relationships) have a property named {@code key} of type list and
     * each items of this list contains value of the expected {@link ValueType}.
     * <p/>
     * Example :
     * <pre><code class='java'> // assertions on nodes
     * Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes)
     *   .haveListPropertyOfType("nicknames", ValueType.STRING)
     * </code></pre>
     *
     * @param key          the property key all entities should have.
     * @param expectedType the expected type for the property
     * @return {@code this} assertion object.
     * @throws AssertionError if the actual value is not equal to the given one.
     */
    public SELF haveListPropertyOfType(final String key, final ValueType expectedType) {
        havePropertyKeys(key);
        havePropertyOfType(key, ValueType.LIST);
        final ValueType type = Objects.requireNonNull(expectedType, "The expected value type shouldn't be null");
        return shouldAllVerify(
                Predicates.propertyListContainsValueType(key, type),
                (notSatisfies) -> ShouldHavePropertyListOfType.elements(actual, key, type).notSatisfies(notSatisfies)
        );
    }

    public SELF havePropertyValueMatching(final String key, final Predicate<Object> predicate) {
        havePropertyKeys(key);
        return shouldAllVerify(
                Predicates.propertyValueMatch(key, predicate),
                (notSatisfies) -> ShouldPropertyMatch.elements(actual, key).notSatisfies(notSatisfies)
        );
    }

    public <T> SELF havePropertyValueMatching(final String key, final Class<T> expectedClass,
                                              final Predicate<T> predicate) {
        havePropertyKeys(key);
        havePropertyInstanceOf(key, expectedClass);
        return shouldAllVerify(
                Predicates.propertyValueMatch(key, predicate),
                (notSatisfies) -> ShouldPropertyMatch.elements(actual, key).notSatisfies(notSatisfies)
        );
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
     * @param key           the property key
     * @param expectedValue the property value
     * @return {@code this} assertion object.
     */
    public SELF haveProperty(final String key, final Object expectedValue) {
        havePropertyKeys(key);
        return shouldAllVerify(
                Predicates.propertyValue(key, expectedValue),
                (notSatisfies) -> ShouldHavePropertyValue
                        .elements(actual, key, expectedValue)
                        .notSatisfies(notSatisfies)
        );
    }

    /**
     * Factory for creating new {@link SELF} assertions with the another list of entities.
     *
     * @param <SELF>   the self type
     * @param <ENTITY> the entity type
     */
    //@formatter:off
    @FunctionalInterface
    protected interface EntitiesAssertFactory<SELF extends Navigable<PARENT_ASSERT, ROOT_ASSERT>,
                                              ENTITY,
                                              NEW_SELF extends Navigable<SELF,ROOT_ASSERT>,
                                              PARENT_ASSERT,
                                              ROOT_ASSERT> {
    //@formatter:on

        /**
         * Create a new assertions on a restricted part of entities.
         *
         * @param entities    the filtered entities
         * @param loader      the data loader
         * @param ignoringIds if true, the ids will be ignore when using compare.
         * @param current     the current assertions
         * @return a new assertion of the current type
         */
        NEW_SELF create(List<ENTITY> entities, DataLoader<ENTITY> loader, boolean ignoringIds, SELF current);

    }

    @FunctionalInterface
    protected interface ErrorMessageCallback<ENTITY> {

        ErrorMessageFactory create(List<ENTITY> notSatisfies);

    }

}
