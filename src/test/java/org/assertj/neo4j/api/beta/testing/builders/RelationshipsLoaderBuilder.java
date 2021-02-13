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
package org.assertj.neo4j.api.beta.testing.builders;

import org.assertj.neo4j.api.beta.type.DataLoader;
import org.assertj.neo4j.api.beta.type.LoaderFactory;
import org.assertj.neo4j.api.beta.type.Relationships;
import org.neo4j.driver.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author patouche - 12/02/2021
 */
public class RelationshipsLoaderBuilder {
    private DataLoader<?> factoryResult;
    private List<Relationships.DbRelationship> entities = new ArrayList<>();
    private Query query;

    public RelationshipsLoaderBuilder factoryResult(DataLoader<?> factoryResult) {
        this.factoryResult = factoryResult;
        return this;
    }

    public RelationshipsLoaderBuilder entities(Relationships.DbRelationship... entities) {
        this.entities = Arrays.asList(entities);
        return this;
    }

    public RelationshipsLoaderBuilder query(Query query) {
        this.query = query;
        return this;
    }

    public Relationships build() {
        return new Relationships() {

            @Override
            public Query query() {
                return query;
            }

            @Override
            public List<DbRelationship> load() {
                return entities;
            }

            @Override
            public <E, D extends DataLoader<E>> D chain(final LoaderFactory<E, D> factory) {
                return (D) factory;
            }
        };
    }
}
