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
import org.assertj.core.presentation.PredicateDescription;
import org.assertj.neo4j.api.beta.util.Checks;

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
                                           PARENT_ASSERT extends ParentAssert<ROOT_ASSERT>,
                                           ROOT_ASSERT>
        extends AbstractDbAssert<SELF, List<ELEMENT>, NEW_SELF, PARENT_ASSERT, ROOT_ASSERT> {
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

    protected SELF isEmpty(final DbMessageCallback<ELEMENT> callback) {
        if (!actual.isEmpty()) {
            throwAssertionError(callback.create(actual));
        }
        return myself;
    }

    /**
     * Verifies that the actual group of values is empty.
     * <p/>
     * Example:
     * <pre><code class='java'> // assertions will pass
     * assertThat(new ArrayList()).isEmpty();
     * assertThat(new int[] { }).isEmpty();
     *
     * // assertions will fail
     * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).isEmpty();
     * assertThat(Arrays.asList(1, 2, 3)).isEmpty();</code></pre>
     *
     * @throws AssertionError if the actual group of values is not empty.
     */
    public SELF isEmpty() {
        return isEmpty(ShouldBeEmpty::shouldBeEmpty);
    }

    /**
     * Verifies that the actual group of values is not empty.
     * <p/>
     * Example:
     * <pre><code class='java'> // assertions will pass
     * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).isNotEmpty();
     * assertThat(Arrays.asList(1, 2, 3)).isNotEmpty();
     *
     * // assertions will fail
     * assertThat(new ArrayList()).isNotEmpty();
     * assertThat(new int[] { }).isNotEmpty();</code></pre>
     *
     * @return {@code this} assertion object.
     * @throws AssertionError if the actual group of values is empty.
     */
    public SELF isNotEmpty() {
        iterables.assertNotEmpty(info, actual);
        return myself;
    }

    /**
     * Verifies that the actual group contains the given values, in any order.
     * <p/>
     * Example:
     * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
     *
     * // assertions will pass
     * assertThat(abc).contains("b", "a");
     * assertThat(abc).contains("b", "a", "b");
     *
     * // assertion will fail
     * assertThat(abc).contains("d");</code></pre>
     *
     * @param values the given values.
     * @return {@code this} assertion object.
     * @throws NullPointerException     if the given argument is {@code null}.
     * @throws IllegalArgumentException if the given argument is an empty array.
     * @throws AssertionError           if the actual group is {@code null}.
     * @throws AssertionError           if the actual group does not contain the given values.
     */
    public SELF contains(ELEMENT... values) {
        Checks.notNullOrEmpty(values, "The provided values cannot be empty or null");
        iterables.assertContains(info, actual, values);
        return myself;
    }

    /**
     * Verifies that the number of values in the actual group is equal to the given one.
     * <p>
     * Example:
     * <pre><code class='java'> // assertions will pass
     * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).hasSize(2);
     * assertThat(Arrays.asList(1, 2, 3)).hasSize(3);
     *
     * // assertions will fail
     * assertThat(new ArrayList()).hasSize(1);
     * assertThat(new int[] { 1, 2, 3 }).hasSize(2);</code></pre>
     *
     * @param expected the expected number of values in the actual group.
     * @return {@code this} assertion object.
     * @throws AssertionError if the number of values of the actual group is not equal to the given one.
     */
    public SELF hasSize(int expected) {
        iterables.assertHasSize(info, actual, expected);
        return myself;
    }

    public SELF anyMatch(final Predicate<? super ELEMENT> predicate) {
        return anyMatch(predicate, PredicateDescription.GIVEN);
    }

    public SELF anyMatch(final Predicate<? super ELEMENT> predicate, final PredicateDescription description) {
        iterables.assertAnyMatch(info, actual, predicate, description);
        return myself;
    }

    public SELF allMatch(final Predicate<? super ELEMENT> predicate) {
        return allMatch(predicate, PredicateDescription.GIVEN);
    }

    public SELF allMatch(final Predicate<? super ELEMENT> predicate, final PredicateDescription description) {
        iterables.assertAllMatch(info, actual, predicate, description);
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
