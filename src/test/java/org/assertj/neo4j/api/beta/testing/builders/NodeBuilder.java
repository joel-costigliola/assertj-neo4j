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

import org.neo4j.driver.Value;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.types.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Patrick Allain - 11/11/2020
 */
public class NodeBuilder {

    private long id = 0L;

    private final List<String> labels = new ArrayList<>();

    private final Map<String, Value> properties = new HashMap<>();

    public NodeBuilder id(final long id) {
        this.id = id;
        return this;
    }

    public NodeBuilder labels(final String ... labels) {
        this.labels.addAll(Arrays.asList(labels));
        return this;
    }

    public NodeBuilder properties(final String key, final Value value) {
        this.properties.put(key, value);
        return this;
    }

    public Node build() {
        return new InternalNode(id, labels, properties);
    }
}
