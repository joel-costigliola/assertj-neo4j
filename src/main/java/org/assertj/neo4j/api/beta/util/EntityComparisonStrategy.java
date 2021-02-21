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
package org.assertj.neo4j.api.beta.util;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.DbValue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.util.Objects.castIfBelongsToType;

/**
 * TODO: Review if it's not possible to use a comparator with the {@link org.assertj.core.api.AbstractAssert#usingComparator(Comparator)} ?
 *
 * @author Patrick Allain - 14/02/2021
 */
public abstract class EntityComparisonStrategy<ENTITY extends DbEntity<ENTITY>>
        extends StandardComparisonStrategy
        implements EntityComparisonStrategyPreference {

    private final Class<ENTITY> entityType;
    private final boolean ignoreId;
    private final Set<String> ignoreProperties;

    protected EntityComparisonStrategy(final Class<ENTITY> entityType, final boolean ignoreId,
                                       final Set<String> ignoreProperties) {
        this.ignoreId = ignoreId;
        this.ignoreProperties = ignoreProperties;
        this.entityType = Objects.requireNonNull(entityType, "The entity type cannot be null.");
    }

    @Override
    public boolean preferred(final Object obj) {
        return this.entityType.isInstance(obj);
    }

    /** {@inheritDoc} */
    @Override
    public boolean areEqual(final Object actual, final Object other) {
        final ENTITY actualEntity = castIfBelongsToType(actual, this.entityType);
        final ENTITY otherEntity = castIfBelongsToType(other, this.entityType);
        if (actualEntity != null && otherEntity != null) {
            return Utils.combine(
                    Utils.lazyDeepEquals(this.ignoreId, DbEntity::getId),
                    Utils.lazyDeepEquals(this::nonIgnoredProperties),
                    this::areEntityEqual
            ).test(actualEntity, otherEntity);
        }
        return super.areEqual(actual, other);
    }

    private Map<String, DbValue> nonIgnoredProperties(final ENTITY entity) {
        final Map<String, DbValue> properties = new HashMap<>(entity.getProperties());
        this.ignoreProperties.forEach(properties::remove);
        return properties;
    }

    /**
     * Entity customization to returns true if actual and other are equal for the corresponding strategy.
     *
     * @param actual the entity to compare to other
     * @param other  the entity to compare to actual
     * @return true if actual and other are equal according to the underlying comparison strategy.
     */
    protected abstract boolean areEntityEqual(final ENTITY actual, final ENTITY other);

    /**
     * Create a composite strategy from multiple {@link EntityComparisonStrategyPreference}.
     *
     * @param comparisons the {@link EntityComparisonStrategyPreference}
     * @return a new {@link ComparisonStrategy}
     */
    public static ComparisonStrategy composite(final EntityComparisonStrategyPreference... comparisons) {
        return new PreferredComparisonStrategy(comparisons);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isStandard() {
        return false;
    }

    //@formatter:off
    public static abstract class EntityComparisonStrategyBuilder<E extends DbEntity<E>,
                                                   CS extends EntityComparisonStrategy<E>,
                                                   B extends EntityComparisonStrategyBuilder<E, CS, B>> {
    //@formatter:on

        protected B myself;
        protected boolean ignoreId = false;
        protected Set<String> ignoreProperties = new HashSet<>();

        protected EntityComparisonStrategyBuilder(final Class<B> selfType) {
            this.myself = selfType.cast(this);
        }

        public B ignoreId(boolean ignoreId) {
            this.ignoreId = ignoreId;
            return myself;
        }

        public B ignoreProperties(String... properties) {
            this.ignoreProperties.addAll(Arrays.asList(properties));
            return myself;
        }

        public abstract CS build();

    }

    private static class PreferredComparisonStrategy
            extends StandardComparisonStrategy
            implements ComparisonStrategy {

        private final List<EntityComparisonStrategyPreference> comparisons;

        private PreferredComparisonStrategy(EntityComparisonStrategyPreference... comparisons) {
            this.comparisons = Arrays.asList(comparisons);
        }

        @Override
        public boolean areEqual(final Object actual, final Object other) {
            return this.comparisons.stream()
                    .filter(cs -> cs.preferred(actual))
                    .filter(cs -> cs.preferred(other))
                    .findFirst()
                    .map(cs -> cs.areEqual(actual, other))
                    .orElseGet(() -> super.areEqual(actual, other));
        }

        @Override
        public boolean isStandard() {
            return false;
        }
    }

}
