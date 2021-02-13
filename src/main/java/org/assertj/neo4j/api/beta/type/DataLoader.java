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
package org.assertj.neo4j.api.beta.type;

import org.neo4j.driver.Query;

import java.util.List;

/**
 * Data loader.
 *
 * @author Patrick Allain - 27/01/2021
 */
public interface DataLoader<ENTITY> {

    /**
     * Get que the query use to load the entities
     *
     * @return the query use to load the entities
     */
    Query query();

    /**
     * Load a list of records.
     *
     * @return the list of records transform into a expected type
     */
    List<ENTITY> load();

    /**
     * Chain current assertion with the creation of an other {@link DataLoader}.
     * <p/>
     * A new query will be executed bellow which will load mode entities.
     *
     * @param factory the factory
     * @param <E>     the entity type
     * @param <D>     the data loader type
     * @return the data loader
     */
    <E, D extends DataLoader<E>> D chain(LoaderFactory<E, D> factory);

}
