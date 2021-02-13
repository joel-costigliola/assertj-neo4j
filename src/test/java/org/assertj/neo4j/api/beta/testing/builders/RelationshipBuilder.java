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
import org.neo4j.driver.internal.InternalRelationship;
import org.neo4j.driver.types.Relationship;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Patrick Allain - 05/12/2020
 */
public class RelationshipBuilder {

    private long id = 0L;
    private long start = 0L;
    private long end = 0L;
    private final String type;
    private final Map<String, Value> properties = new HashMap<>();
    public RelationshipBuilder(final String type) {
        this.type = type;
    }

    public RelationshipBuilder id(final long id) {
        this.id = id;
        return this;
    }
    public RelationshipBuilder start(final long start) {
        this.start = start;
        return this;
    }
    public RelationshipBuilder end(final long end) {
        this.end = end;
        return this;
    }

    public RelationshipBuilder properties(final String key, final Value value) {
        this.properties.put(key, value);
        return this;
    }

    public Relationship build() {
        return new InternalRelationship(this.id, this.start, this.end, this.type, this.properties);
    }


}