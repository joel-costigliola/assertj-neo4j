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
package org.assertj.neo4j.api.beta.testing;

import org.neo4j.driver.Record;
import org.neo4j.driver.Value;
import org.neo4j.driver.internal.InternalRecord;
import org.neo4j.driver.internal.value.NodeValue;
import org.neo4j.driver.types.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author patouche - 11/11/2020
 */
public class RecordBuilder {

    private final List<String> keys = new ArrayList<>();

    private final List<Value> values = new ArrayList<>();

    RecordBuilder() {
    }

    public RecordBuilder key(String key) {
        this.keys.add(key);
        return this;
    }

    /**
     * Use {@link org.neo4j.driver.Values} to build your values.
     *
     * @param value the value to add
     * @return the current instance
     */
    public RecordBuilder value(Value value) {
        this.values.add(value);
        return this;
    }

    public RecordBuilder node(Node node) {
        this.values.add(new NodeValue(node));
        return this;
    }

    public Record build() {
        return new InternalRecord(keys, values.toArray(new Value[0]));
    }

}
