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

import org.assertj.core.util.Lists;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Query;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionConfig;
import org.neo4j.driver.Value;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract class for data loader.
 *
 * @author Patrick Allain - 09/11/2020
 */
abstract class AbstractDataLoader<ENTITY> implements DataLoader<ENTITY> {

    /** The Neo4j Database driver. */
    protected final Driver driver;

    /** The type of record. */
    protected final RecordType recordType;

    /** The neo4j query. */
    protected final Query query;

    /**
     * Class constructor.
     *
     * @param driver     the Neo4J database driver
     * @param recordType the record type
     */
    protected AbstractDataLoader(final Driver driver, final RecordType recordType, final Query query) {
        this.driver = driver;
        this.recordType = recordType;
        this.query = query;
    }

    /**
     * Load {@link ENTITY} list from a list of {@link Record}.
     *
     * @param records the records to be transform
     * @return a list of entities
     */
    protected abstract List<ENTITY> load(final List<Record> records);

    /** {@inheritDoc} */
    @Override
    public List<ENTITY> load() {
        try (Session session = this.driver.session()) {
            final List<Record> records = session.readTransaction(
                    (tx -> tx.run(query).list()),
                    TransactionConfig.builder().build()
            );
            return load(records);
        }
    }

    /** {@inheritDoc} */
    @Override
    public <E, D extends DataLoader<E>> D chain(final LoaderFactory<E, D> factory) {
        return factory.create(this.driver);
    }

    /** {@inheritDoc} */
    @Override
    public Query query() {
        return query;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "DataLoader:" + recordType + "{query=" + query + '}';
    }
}
