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
import org.assertj.core.internal.Iterables;
import org.assertj.core.presentation.Representation;
import org.assertj.core.util.Streams;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.neo4j.api.beta.util.DbRepresentation;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Abstract DB Assertion.
 *
 * @param <SELF>          the current assertions type.
 * @param <ACTUAL>        the actual tested type
 * @param <PARENT_ASSERT> the parent assertions type
 * @param <ROOT_ASSERT>   the root assertions type
 * @author Patrick Allain - 17/02/2020
 */
//@formatter:off
public abstract class AbstractDbAssert<SELF extends AbstractDbAssert<SELF, ACTUAL, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT>,
                                       ACTUAL,
                                       NEW_SELF extends Navigable<SELF, ROOT_ASSERT>,
                                       PARENT_ASSERT extends ParentAssert ,
                                       ROOT_ASSERT>
        extends AbstractAssert<SELF, ACTUAL>
        implements Navigable<PARENT_ASSERT, ROOT_ASSERT>, ParentAssert {
//@formatter:on

    // TODO : As this is an internal object of AssertJ. Check if we can we use it safely ??
    /** Iterable to factorize assertions on iterable. */
    protected Iterables iterables = Iterables.instance();

    /** Factory for creating new assertions on restricted list of entities. */
    protected final DbAssertFactory<SELF, ACTUAL, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT> newSelfFactory;

    /** Root assert. May be {@code null}. */
    private final ROOT_ASSERT rootAssert;

    /** Previous assert. May be {@code null}. */
    protected final PARENT_ASSERT parentAssert;

    /**
     * Class constructor.
     *
     * @param actual         the actual object under test.
     * @param selfType       the self class type.
     * @param newSelfFactory the factory to chain from the current assertion.
     * @param parentAssert   the parent assertion for navigation.
     * @param rootAssert     the parent assertion for navigation.
     */
    protected AbstractDbAssert(
            final ACTUAL actual,
            final Class<?> selfType,
            final DbAssertFactory<SELF, ACTUAL, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT> newSelfFactory,
            final PARENT_ASSERT parentAssert,
            final ROOT_ASSERT rootAssert) {
        super(actual, selfType);
        this.newSelfFactory = Objects.requireNonNull(newSelfFactory);
        this.parentAssert = parentAssert;
        this.rootAssert = rootAssert;
        if (parentAssert != null) {
            this.info.useRepresentation(parentAssert.representation());
            // TODO : Copy the parent comparison strategy ?
        }
    }

    /**
     * Get the actual entities for assertions.
     *
     * @return a list of entities.
     */
    @VisibleForTesting
    protected ACTUAL getActual() {
        return this.actual;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public PARENT_ASSERT toParentAssert() {
        // Parent assert should be null only when we are on the root assert
        if (this.parentAssert == null) {
            return (PARENT_ASSERT) toRootAssert();
        }
        return this.parentAssert;
    }

    /** {@inheritDoc} */
    @Override
    public ROOT_ASSERT toRootAssert() {
        return rootAssert().orElseThrow(() -> new IllegalArgumentException("Root assertion shouldn't be null !"));
    }

    protected Optional<ROOT_ASSERT> rootAssert() {
        return Optional.ofNullable(this.rootAssert);
    }

    /**
     * From a parent assertion, retrieve the root assertion or {@code null}, if there is no parent assertion.
     * <p/>
     * This is intent to be used in constructor of root assertion.
     *
     * @param parent the parent assertion
     * @param <A>    the current assertion type
     * @param <P>    the parent assertion type
     * @param <R>    the root assertion type
     * @return the root assertion based on the parent provided
     */
    protected static <A extends Navigable<P, R>, P extends ParentAssert, R> R rootAssert(final A parent) {
        return Optional.ofNullable(parent).map(Navigable::toRootAssert).orElse(null);
    }

    /** {@inheritDoc} */
    @Override
    public Representation representation() {
        return info.representation();
    }

    /** {@inheritDoc} */
    @Override
    public SELF usingDefaultComparator() {
        iterables = Iterables.instance();
        return super.usingDefaultComparator();
    }

    /**
     * Customize the display of an error message providing the full entity description.
     * <p/>
     * The display will be forward to all children assertions but this will not impact the parent assertions.
     *
     * @return {@code this} assertion object.
     */
    public SELF withFullRepresentation() {
        return withRepresentation(DbRepresentation.full());
    }

    /**
     * Customize the display of an error message providing an abbreviate entity description.
     * <p/>
     * Reset
     *
     * @return {@code this} assertion object.
     */
    public SELF withAbbreviateRepresentation() {
        return withRepresentation(DbRepresentation.abbreviate());
    }

    /**
     * Ensure all entities verify the provided predicate. If not, the {@code callback} will be invoke to create a new
     * instance of a {@link ErrorMessageFactory}
     *
     * @param iterable  the element to tests.
     * @param predicate the predicate that all entities should validate.
     * @param callback  the callback to create the {@link ErrorMessageFactory}.
     * @param <T>       the type of elements in the iterator.
     * @return {@code this} assertion object.
     */
    protected <T> SELF shouldAllVerify(
            final Iterable<? extends T> iterable, final Predicate<T> predicate, final DbMessageCallback<T> callback) {
        // TODO : OR => iterables.assertAllMatch(info, actual, predicate, new PredicateDescription("TOTO"));?
        final List<T> notSatisfies = Streams.stream(iterable).filter(predicate.negate()).collect(Collectors.toList());
        if (!notSatisfies.isEmpty()) {
            throwAssertionError(callback.create(notSatisfies));
        }
        return myself;
    }

    /**
     * Factory for creating new {@link SELF} assertions with the another list of entities.
     *
     * @param <SELF>   the self type
     * @param <ACTUAL> the actual type
     */
    //@formatter:off
    @FunctionalInterface
    protected interface DbAssertFactory<SELF extends Navigable<PARENT_ASSERT, ROOT_ASSERT> & ParentAssert,
                                        ACTUAL,
                                        NEW_SELF extends Navigable<SELF,ROOT_ASSERT>,
                                        PARENT_ASSERT extends ParentAssert,
                                        ROOT_ASSERT> {
    //@formatter:on

        /**
         * Create a new assertions on a restricted part of actual.
         *
         * @param actual the actual object
         * @param self   the current assertion
         * @return a new assertion of the current type
         */
        NEW_SELF create(ACTUAL actual, SELF self);

    }

    @FunctionalInterface
    protected interface DbMessageCallback<I> {

        /**
         * Create the error message factory providing from a list of elements.
         *
         * @param notSatisfies the elements that cause the failing case
         * @return a new {@link ErrorMessageFactory}
         */
        ErrorMessageFactory create(final List<I> notSatisfies);

    }

}
