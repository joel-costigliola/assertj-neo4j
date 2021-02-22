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

import org.assertj.core.error.ShouldBeEmpty;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Abstract Database List Assertions.
 * <p/>
 * This class don't extends {@link org.assertj.core.api.AbstractIterableAssert} to offer the navigation on all kind of
 * {@link #filteredOn(Predicate)} methods. Each methods that will return a new {@link NEW_SELF} create a new assertion
 * which will be the same kind as {@link SELF}
 *
 * @param <SELF>          the current assertions type.
 * @param <ACTUAL>        the actual object under test type
 * @param <PARENT_ASSERT> the parent assertions type
 * @param <ROOT_ASSERT>   the root assertions type
 * @author Patrick Allain - 24/11/2020
 */
//@formatter:off
public abstract class AbstractDbListAssert<SELF extends AbstractDbListAssert<SELF,
                                                                             ACTUAL,
                                                                             ELEMENT,
                                                                             NEW_SELF,
                                                                             PARENT_ASSERT,
                                                                             ROOT_ASSERT>,
                                           ACTUAL extends List<ELEMENT>,
                                           ELEMENT,
                                           NEW_SELF extends Navigable<SELF, ROOT_ASSERT>,
                                           PARENT_ASSERT extends ParentalAssert,
                                           ROOT_ASSERT>
        extends AbstractDbAssert<SELF, List<ELEMENT>, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT>
        implements Navigable<PARENT_ASSERT, ROOT_ASSERT>, ParentalAssert {
//@formatter:on

    /**
     * Class constructor.
     *
     * @param actual         the actual object under test
     * @param selfType       the self class type
     * @param newSelfFactory the factory to chain from the current assertion
     * @param parentAssert   the parent assertion for navigation
     * @param rootAssert     the parent assertion for navigation
     */
    protected AbstractDbListAssert(
            final List<ELEMENT> actual,
            final Class<?> selfType,
            final DbAssertFactory<SELF, List<ELEMENT>, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT> newSelfFactory,
            final PARENT_ASSERT parentAssert,
            final ROOT_ASSERT rootAssert) {
        super(actual, selfType, newSelfFactory, parentAssert, rootAssert);
    }

    /**
     * TODO
     *
     * @return {@code this} assertion object.
     */
    public SELF isEmpty() {
        return isEmpty(ShouldBeEmpty::shouldBeEmpty);
    }

    protected SELF isEmpty(final DbMessageCallback<ELEMENT> callback) {
        if (!actual.isEmpty()) {
            throwAssertionError(callback.create(actual));
        }
        return myself;
    }

    /**
     * TODO: Rewrite documentation after extract to a generic method
     *
     * @return {@code this} assertion object.
     */
    public SELF isNotEmpty() {
        iterables.assertNotEmpty(info, actual);
        return myself;
    }

    /**
     * TODO: Rewrite documentation after extract to a generic method
     *
     * @return {@code this} assertion object.
     */
    public SELF hasSize(final int expectedSize) {
        iterables.assertHasSize(info, actual, expectedSize);
        return myself;
    }

    /**
     * TODO: Rewrite documentation after extract to a generic method
     *
     * @return {@code this} assertion object.
     */
    public SELF contains(final ELEMENT... values) {
        iterables.assertContains(info, actual, values);
        return myself;
    }

    /**
     * TODO: Rewrite documentation after extract to a generic method
     *
     * @return {@code this} assertion object.
     */
    public NEW_SELF filteredOn(final Predicate<ELEMENT> predicate) {
        final List<ELEMENT> filtered = this.actual.stream().filter(predicate).collect(Collectors.toList());
        return newSelfFactory.create(filtered, myself);
    }

}
