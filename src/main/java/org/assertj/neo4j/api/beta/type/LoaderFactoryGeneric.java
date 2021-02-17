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

import org.neo4j.driver.Driver;
import org.neo4j.driver.Query;

import java.util.function.BiFunction;

/**
 * Generic {@link LoaderFactory} that wrap a query to be executed on the provided driver.
 *
 * @author Patrick Allain - 12/02/2021
 */
class LoaderFactoryGeneric<ENTITY, LOADER extends DataLoader<ENTITY>> implements LoaderFactory<ENTITY, LOADER> {

    private final Query query;
    private final BiFunction<Driver, Query, LOADER> function;

    LoaderFactoryGeneric(final Query query, final BiFunction<Driver, Query, LOADER> function) {
        this.query = query;
        this.function = function;
    }

    /** {@inheritDoc} */
    @Override
    public LOADER create(final Driver driver) {
        return function.apply(driver, query);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "LoaderFactory{query=" + query + '}';
    }
}
