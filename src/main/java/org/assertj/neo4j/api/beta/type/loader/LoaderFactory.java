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
package org.assertj.neo4j.api.beta.type.loader;

import org.assertj.neo4j.api.beta.type.DbNode;
import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.neo4j.driver.Driver;

/**
 * Factory for creating new instance of {@link DataLoader}.
 * <p/>
 * This interface prevent to expose internal constructor of {@link DataLoader} implementation.
 * <p/>
 * FIXME: This will be much more clean if the driver was directly in the Assertions. To be simplify ?
 *
 * @author Patrick Allain - 10/02/2021
 */
public interface LoaderFactory<ENTITY, LOADER extends DataLoader<ENTITY>> {

    /**
     * Create a new {@link DataLoader} from a neo4j {@link Driver}.
     *
     * @param driver the neo4j driver
     * @return a new instance of {@link LOADER}
     */
    LOADER create(Driver driver);

    /**
     * Create a new {@link LoaderFactory} for {@link DbRelationship}.
     *
     * @param types the type of relationships
     * @return a new loader factory instance.
     */
    static LoaderFactory<DbRelationship, Relationships> relationships(final String... types) {
        return new LoaderFactoryGeneric<>(Queries.relationships(types), RelationshipLoader::new);
    }

    /**
     * Create a new {@link LoaderFactory} for {@link DbNode}.
     *
     * @param ids the ids of the relationships
     * @return a new loader factory instance.
     */
    static LoaderFactory<DbNode, Nodes> nodeByIds(final Long... ids) {
        return new LoaderFactoryGeneric<>(Queries.nodeByIds(ids), NodeLoader::new);
    }

}
