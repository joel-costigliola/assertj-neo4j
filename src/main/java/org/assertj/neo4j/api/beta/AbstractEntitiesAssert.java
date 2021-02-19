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

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.Iterables;
import org.assertj.neo4j.api.beta.error.ShouldHavePropertyInstanceOf;
import org.assertj.neo4j.api.beta.error.ShouldHavePropertyKeys;
import org.assertj.neo4j.api.beta.error.ShouldHavePropertyListOfType;
import org.assertj.neo4j.api.beta.error.ShouldHavePropertySize;
import org.assertj.neo4j.api.beta.error.ShouldHavePropertyValue;
import org.assertj.neo4j.api.beta.error.ShouldHavePropertyValueType;
import org.assertj.neo4j.api.beta.error.ShouldPropertyMatch;
import org.assertj.neo4j.api.beta.error.ShouldQueryResultBeEmpty;
import org.assertj.neo4j.api.beta.error.ShouldQueryResultBeNotEmpty;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.ObjectType;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.assertj.neo4j.api.beta.type.loader.DataLoader;
import org.assertj.neo4j.api.beta.util.Checks;
import org.assertj.neo4j.api.beta.util.EntityComparisonStrategy;
import org.assertj.neo4j.api.beta.util.EntityRepresentation;
import org.assertj.neo4j.api.beta.util.NodeComparisonStrategy;
import org.assertj.neo4j.api.beta.util.Predicates;
import org.assertj.neo4j.api.beta.util.RelationshipComparisonStrategy;
import org.assertj.neo4j.api.beta.util.Utils;

import java.util.List;
import java.util.Objects;
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
public abstract class AbstractEntitiesAssert<SELF extends AbstractEntitiesAssert<SELF, ENTITY, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT>,
                                             ENTITY extends DbEntity<ENTITY>,
                                             NEW_SELF extends Navigable<SELF, ROOT_ASSERT>,
                                             PARENT_ASSERT extends ParentalAssert,
                                             ROOT_ASSERT>
        extends AbstractDbAssert<SELF, List<ENTITY>, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT>
        implements Navigable<PARENT_ASSERT, ROOT_ASSERT>,
                   ParentalAssert {
//@formatter:on

    /** The record type */
    protected final ObjectType recordType;

    /** The data loader. */
    protected final DataLoader<ENTITY> dataLoader;

    /**
     * Class constructor.
     *
     * @param recordType     the record type
     * @param selfType       the self class type
     * @param dataLoader     the data loader
     * @param entities       the entities
     * @param newSelfFactory the factory to chain from the current assertion
     * @param parentAssert   the parent assertion for navigation
     * @param rootAssert     the parent assertion for navigation
     */
    protected AbstractEntitiesAssert(
            final ObjectType recordType,
            final Class<?> selfType,
            final DataLoader<ENTITY> dataLoader,
            final List<ENTITY> entities,
            final EntitiesAssertFactory<SELF, ENTITY, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT> newSelfFactory,
            final PARENT_ASSERT parentAssert,
            final ROOT_ASSERT rootAssert) {
        super(entities, selfType, (a, c) -> newSelfFactory.create(a, dataLoader, c), parentAssert, rootAssert);
        this.recordType = Objects.requireNonNull(recordType);
        this.dataLoader = dataLoader;
    }

    /**
     * Customize the display of an error message providing the full entity description.
     *
     * @return {@code this} assertion object.
     */
    public SELF withFullEntityRepresentation() {
        return withRepresentation(EntityRepresentation.full());
    }

    /**
     * Customize the display of an error message providing an abbreviate entity description.
     *
     * @return {@code this} assertion object.
     */
    public SELF withAbbreviateEntityRepresentation() {
        return withRepresentation(EntityRepresentation.full());
    }

    /**
     * Using a comparison without any entity ids. This provide a easy way to create new assertions on the current list
     * of {@link ENTITY} without specifying the {@link ENTITY#getId()}
     * <p/>
     * Example:
     * <pre><code class='java'> // for nodes
     * Nodes relationships = new Nodes(driver, "Developer");
     * assertThat(nodes)
     *     .usingNoEntityIdComparison()
     *     .contains(Drivers.node().label("Developer").build());</code></pre>
     * <p/>
     * TODO : as a alternative, this may be declare in typed entity assertions. ?
     *
     * @return {@code this} assertion object.
     */
    public SELF usingNoEntityIdComparison() {
        final ComparisonStrategy comparison = EntityComparisonStrategy.composite(
                NodeComparisonStrategy.builder().ignoreId(true).build(),
                RelationshipComparisonStrategy.builder().ignoreId(true).ignoreStartId(true).ignoreEndId(true).build()
        );
        objects = new org.assertj.core.internal.Objects(comparison);
        iterables = new Iterables(comparison);
        return myself;
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
    protected SELF shouldAllVerify(final Predicate<ENTITY> predicate, final DbMessageCallback<ENTITY> callback) {
        // TODO : OR => iterables.assertAllMatch(info, actual, predicate, new PredicateDescription("TOTO"));?
        isNotEmpty();
        return shouldAllVerify(actual, predicate, callback);
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
        // OR : iterables.assertEmpty(info,actual);
        return isEmpty((entities) -> ShouldQueryResultBeEmpty.create(entities, dataLoader.query()));
    }

    protected SELF isEmpty(final DbMessageCallback<ENTITY> callback) {
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
        // TODO: OR => iterables.assertNotEmpty(info, actual); ?
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
        return newSelfFactory.create(entities, myself);
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
        havePropertyKeys(Objects.requireNonNull(key, "The property key shouldn't be null"));
        final ValueType type = Objects.requireNonNull(expectedType, "The expected value type shouldn't be null");
        // Preconditions.checkArgument();
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
        havePropertyKeys(Objects.requireNonNull(key, "The property key shouldn't be null"));
        final Class<?> clazz = Objects.requireNonNull(expectedClass, "The expected class shouldn't be null");
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
     * <p/>
     * FIXME : List property type can contains null which is not properly handle right now.
     *
     * @param key          the property key all entities should have.
     * @param expectedType the expected type for the property.
     * @return {@code this} assertion object.
     * @throws AssertionError if the actual value is not equal to the given one.
     */
    public SELF haveListPropertyOfType(final String key, final ValueType expectedType) {
        havePropertyKeys(Objects.requireNonNull(key, "The property key shouldn't be null"));
        havePropertyOfType(key, ValueType.LIST);
        final ValueType type = Objects.requireNonNull(expectedType, "The expected value type shouldn't be null");
        return shouldAllVerify(
                Predicates.propertyListContainsValueType(key, type),
                (notSatisfies) -> ShouldHavePropertyListOfType.elements(actual, key, type).notSatisfies(notSatisfies)
        );
    }

    /**
     * Verifies that all actual entities (nodes or a relationships) have a property named {@code key} of which validate
     * the provided {@link Predicate}.
     * <p/>
     * To have a proper typed {@link Predicate}, you can use as an alternative the {@link
     * #havePropertyValueMatching(String, Class, Predicate)} which will first check if the property value is of the
     * expected class before executing the predicate.
     * <p/>
     * Example :
     * <pre><code class='java'> // assertions on nodes
     * Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes)
     *   .havePropertyValueMatching("nicknames", s -> {
     *       if (!(s instanceof String)) {
     *           return false;
     *       }
     *       return ((String) s).length() > 3
     *   })
     * </code></pre>
     *
     * @param key       the property key all entities should have.
     * @param predicate the expected predicate that the property value should match
     * @return {@code this} assertion object.
     * @throws AssertionError if the actual value is not equal to the given one.
     */
    public SELF havePropertyValueMatching(final String key, final Predicate<Object> predicate) {
        havePropertyKeys(Objects.requireNonNull(key, "The property key shouldn't be null"));
        return shouldAllVerify(
                Predicates.propertyValueMatch(key, predicate),
                (notSatisfies) -> ShouldPropertyMatch.elements(actual, key).notSatisfies(notSatisfies)
        );
    }

    /**
     * Verifies that all actual entities (nodes or a relationships) have a property named {@code key} of which validate
     * the provided {@link Predicate}.
     * <p/>
     * Example :
     * <pre><code class='java'> // assertions on nodes
     * Nodes nodes = new Nodes(driver, "Person");
     * assertThat(nodes)
     *   .havePropertyValueMatching("nicknames", String.class, s -> s.length() > 3)
     * </code></pre>
     *
     * @param key           the property key all entities should have.
     * @param expectedClass the expected type that the property value should have
     * @param predicate     the expected predicate that the property value should match
     * @param <T>           the type of the expected property value
     * @return {@code this} assertion object.
     * @throws AssertionError if the actual value is not equal to the given one.
     */
    public <T> SELF havePropertyValueMatching(final String key, final Class<T> expectedClass,
                                              final Predicate<T> predicate) {
        havePropertyKeys(Objects.requireNonNull(key, "The property key shouldn't be null"));
        havePropertyInstanceOf(key, Objects.requireNonNull(expectedClass, "The expected class shouldn't be null"));
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
        havePropertyKeys(Objects.requireNonNull(key, "The property key shouldn't be null"));
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
    protected interface EntitiesAssertFactory<SELF extends Navigable<PARENT_ASSERT, ROOT_ASSERT> & ParentalAssert,
                                              ENTITY,
                                              NEW_SELF extends Navigable<SELF,ROOT_ASSERT>,
                                              PARENT_ASSERT extends ParentalAssert,
                                              ROOT_ASSERT> {
    //@formatter:on

        /**
         * Create a new assertions on a restricted part of entities.
         *
         * @param entities the filtered entities
         * @param loader   the data loader
         * @param current  the current assertions
         * @return a new assertion of the current type
         */
        NEW_SELF create(List<ENTITY> entities, DataLoader<ENTITY> loader, SELF current);

    }

}
