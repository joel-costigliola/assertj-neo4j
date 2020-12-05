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

import java.util.List;

/**
 * @author pallain - 09/11/2020
 */
public abstract class LoadingType<T> {

    /** The Neo4j Database driver. */
    protected final Driver driver;

    /** The type of record. */
    protected final RecordType recordType;

    /**
     * Class constructor.
     *
     * @param driver the Neo4J database driver
     * @param recordType   the record type
     */
    protected LoadingType(final Driver driver, final RecordType recordType) {
        this.driver = driver;
        this.recordType = recordType;
    }

    /**
     * Load a list of records.
     *
     * @return the list of records transform into a expected type
     */
    public abstract List<T> load();
}
