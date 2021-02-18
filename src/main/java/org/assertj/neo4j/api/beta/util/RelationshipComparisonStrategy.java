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

import org.assertj.neo4j.api.beta.type.DbRelationship;

import java.util.Set;

/**
 * Comparison strategy for {@link DbRelationship}.
 *
 * @author Patrick Allain - 14/02/2021
 */
public class RelationshipComparisonStrategy extends EntityComparisonStrategy<DbRelationship> {

    private final boolean ignoreType;
    private final boolean ignoreStartId;
    private final boolean ignoreEndId;

    private RelationshipComparisonStrategy(
            final boolean ignoreId, final boolean ignoreType, final boolean ignoreStartId, final boolean ignoreEndId,
            final Set<String> ignoreProperties) {
        super(DbRelationship.class, ignoreId, ignoreProperties);
        this.ignoreType = ignoreType;
        this.ignoreStartId = ignoreStartId;
        this.ignoreEndId = ignoreEndId;
    }

    @Override
    protected boolean areEntityEqual(final DbRelationship actual, final DbRelationship other) {
        return Utils
                .combine(
                        Utils.lazyDeepEquals(this.ignoreType, DbRelationship::getType),
                        Utils.lazyDeepEquals(this.ignoreStartId, DbRelationship::getStart),
                        Utils.lazyDeepEquals(this.ignoreEndId, DbRelationship::getEnd)
                )
                .test(actual, other);
    }

    public static RelationshipComparisonStrategyBuilder builder() {
        return new RelationshipComparisonStrategyBuilder();
    }

    //@formatter:off
    public static class RelationshipComparisonStrategyBuilder
            extends EntityComparisonStrategyBuilder<DbRelationship,
                                                    RelationshipComparisonStrategy,
                                                    RelationshipComparisonStrategyBuilder> {
    //@formatter:on

        private boolean ignoreType;
        private boolean ignoreStartId;
        private boolean ignoreEndId;

        private RelationshipComparisonStrategyBuilder() {
            super(RelationshipComparisonStrategyBuilder.class);
        }

        public RelationshipComparisonStrategyBuilder ignoreType(final boolean ignoreType) {
            this.ignoreType = ignoreType;
            return myself;
        }

        public RelationshipComparisonStrategyBuilder ignoreStartId(final boolean ignoreStartId) {
            this.ignoreStartId = ignoreStartId;
            return myself;
        }

        public RelationshipComparisonStrategyBuilder ignoreEndId(final boolean ignoreEndId) {
            this.ignoreEndId = ignoreEndId;
            return myself;
        }

        public RelationshipComparisonStrategy build() {
            return new RelationshipComparisonStrategy(
                    this.ignoreId, this.ignoreType, this.ignoreStartId, this.ignoreEndId, this.ignoreProperties
            );
        }

    }

}
