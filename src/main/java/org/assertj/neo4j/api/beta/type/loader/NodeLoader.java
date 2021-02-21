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
import org.assertj.neo4j.api.beta.type.ObjectType;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Query;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data loader for {@link DbNode}.
 *
 * @author Patrick Allain - 31/10/2020
 */
class NodeLoader extends AbstractDataLoader<DbNode> implements Nodes {

    /**
     * Class constructor for {@link NodeLoader}.
     * <p>
     * This will allow you to write easy assertions on your database nodes.
     *
     * @param driver the neo4j driver
     * @param labels the nodes labels to watch
     */
    public NodeLoader(final Driver driver, final String... labels) {
        this(driver, Queries.nodes(labels));
    }

    NodeLoader(final Driver driver, final Query query) {
        super(driver, ObjectType.NODE, query);
    }

    /** {@inheritDoc} */
    @Override
    protected List<DbNode> load(final List<Record> records) {
        return records.stream()
                .map(Record::values)
                .flatMap(Collection::stream)
                .map(Value::asNode)
                .map(ObjectType.NODE::convert)
                .map(DbNode.class::cast)
                .collect(Collectors.toList());
    }

}
