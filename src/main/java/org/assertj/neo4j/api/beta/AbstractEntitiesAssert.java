package org.assertj.neo4j.api.beta;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Iterables;
import org.assertj.core.util.Arrays;
import org.assertj.core.util.IterableUtil;
import org.assertj.core.util.Lists;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.neo4j.api.beta.error.ElementsShouldHavePropertyKeys;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.LoadingType;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.util.Entities;
import org.assertj.neo4j.api.beta.util.Wip;

import java.util.ArrayList;
import java.util.List;

/**
 * @author patouche - 24/11/2020
 */
//@formatter:off
public abstract class AbstractEntitiesAssert<SELF extends AbstractEntitiesAssert<SELF, LOADER, ENTITY>,
                                             LOADER extends LoadingType<ENTITY>,
                                             ENTITY extends DbEntity<ENTITY>>
        extends AbstractAssert<SELF, List<ENTITY>> {
//@formatter:on



    protected Iterables iterables = Iterables.instance();

    /** The record type */
    protected final RecordType recordType;

    /** The loading type. */
    protected final LOADER loadingType;

    /** Parent nodes asserts. May be {@code null}. */
    private final SELF parent;

    protected AbstractEntitiesAssert(
            final RecordType recordType, final LOADER dbLoader, final List<ENTITY> entities,
            final Class<?> selfType, SELF parent) {
        super(entities, selfType);
        this.recordType = recordType;
        this.parent = parent;
        this.loadingType = dbLoader;
    }

    @VisibleForTesting
    protected List<ENTITY> getActual() {
        return this.actual;
    }

    public SELF hasSize(final int expectedSize) {
        iterables.assertHasSize(info, actual, expectedSize);
        return myself;
    }

    public SELF contains(ENTITY... values) {
        iterables.assertContains(info, actual, values);
        return myself;
    }

    /**
     * Provide a easy way to create new assertions on the current list of
     * {@link ENTITY} without specifying the {@link ENTITY#getId()}
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
    public abstract SELF ignoringIds();

    protected static <T> List<T> checkArray(final T[] items, String message) {
        if (Arrays.isNullOrEmpty(items)) {
            throw new IllegalArgumentException(message);
        }
        return java.util.Arrays.asList(items);
    }

    /**
     * Verifies that the actual entity (a node or a relationship) have properties with the given {@code keys}.
     * <p />
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
     * Verifies that the actual entity (a node or a relationship) have properties with the given {@code keys}.
     * <p />
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
    public SELF havePropertyKeys(final Iterable<String> expectedKeys) {
        if (IterableUtil.isNullOrEmpty(expectedKeys)) {
            throw new IllegalArgumentException("The iterable of property keys to look for should not be empty");
        }
        if (!Entities.haveAllKeys(actual, expectedKeys)) {
            final ArrayList<String> keys = Lists.newArrayList(expectedKeys);
            throwAssertionError(ElementsShouldHavePropertyKeys.create(
                    recordType, actual, keys, Entities.missingPropertyKeys(actual, keys)
            ));
        }
        return myself;
    }

    public SELF haveProperties(final String... keys) {
        return myself;
    }

    public SELF haveProperties(final Iterable<String> keys) {
        throw Wip.TODO(this);
        // return myself;
    }

}
