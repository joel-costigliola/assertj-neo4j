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

import org.neo4j.driver.Record;
import org.neo4j.driver.Value;
import org.neo4j.driver.internal.InternalRecord;
import org.neo4j.driver.internal.value.NodeValue;
import org.neo4j.driver.internal.value.RelationshipValue;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Patrick Allain - 11/11/2020
 */
public class RecordBuilder {

    private final Map<String, Value> values = new HashMap<>();

    public RecordBuilder() {
    }

    /**
     * Use {@link org.neo4j.driver.Values} to build your values.
     *
     * @param value the value to add
     * @return the current instance
     */
    public RecordBuilder value(String key, Value value) {
        this.values.put(key, value);
        return this;
    }

    public RecordBuilder node(String key, Node node) {
        this.values.put(key, new NodeValue(node));
        return this;
    }

    public RecordBuilder relation(String key, Relationship relationship) {
        this.values.put(key, new RelationshipValue(relationship));
        return this;
    }

    public Record build() {
        final List<String> keys = new ArrayList<>(values.keySet());
        return new InternalRecord(keys, keys.stream().map(values::get).toArray(Value[]::new));
    }

}
