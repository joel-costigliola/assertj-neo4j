package org.assertj.neo4j.api.beta.util;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.DbValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.util.Objects.castIfBelongsToType;

/**
 * @author patouche - 14/02/2021
 */
abstract class EntityComparisonStrategy<ENTITY extends DbEntity>
        extends StandardComparisonStrategy
        implements ComparisonStrategy {

    private final Class<ENTITY> entityType;
    private final boolean ignoreId;
    private final Set<String> ignoredProperties;

    protected EntityComparisonStrategy(final Class<ENTITY> entityType, final boolean ignoreId,
                                       final Set<String> ignoredProperties) {
        this.ignoreId = ignoreId;
        this.ignoredProperties = ignoredProperties;
        this.entityType = Objects.requireNonNull(entityType, "The entity type cannot be null.");
    }

    @Override
    public boolean areEqual(final Object actual, final Object other) {
        final ENTITY actualEntity = castIfBelongsToType(actual, this.entityType);
        final ENTITY otherEntity = castIfBelongsToType(other, this.entityType);
        if (actualEntity != null && otherEntity != null) {
            return this.areEqual(actualEntity, otherEntity);
        }
        return super.areEqual(actual, other);
    }

    protected boolean areIdsEqual(final ENTITY actual, final ENTITY other) {
        return this.ignoreId || Objects.equals(actual.getId(), other.getId());
    }

    protected boolean arePropertiesEqual(final ENTITY actual, final ENTITY other) {
        final Map<String, DbValue> actualProperties = dropIgnoredProperties(actual.getProperties());
        final Map<String, DbValue> otherProperties = dropIgnoredProperties(other.getProperties());
        return Objects.deepEquals(actualProperties, otherProperties);
    }

    private Map<String, DbValue> dropIgnoredProperties(Map<String, DbValue> properties) {
        final Map<String, DbValue> resultMap = new HashMap<>(properties);
        this.ignoredProperties.forEach(resultMap::remove);
        return resultMap;
    }

    protected abstract boolean areEqual(final ENTITY actual, final ENTITY other);

    @Override
    public boolean isStandard() {
        return false;
    }

    //@formatter:off
    static abstract class EntityComparisonStrategyBuilder<E extends DbEntity,
                                                   CS extends EntityComparisonStrategy<E>,
                                                   B extends EntityComparisonStrategyBuilder<E, CS, B>> {
    //@formatter:on

        protected B myself;
        protected boolean ignoreId = false;
        protected Set<String> ignoredProperties = new HashSet<>();

        protected EntityComparisonStrategyBuilder(final Class<B> selfType) {
            this.myself = selfType.cast(this);
        }

        public B ignoreId(boolean ignoreId) {
            this.ignoreId = ignoreId;
            return myself;
        }

        public B ignoredProperty(String property) {
            this.ignoredProperties.add(property);
            return myself;
        }

        public B ignoredProperties(String... properties) {
            this.ignoredProperties.addAll(Arrays.asList(properties));
            return myself;
        }

        public abstract CS build();

    }

}
