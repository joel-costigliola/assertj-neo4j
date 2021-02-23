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

import org.assertj.neo4j.api.beta.testing.builders.NodesLoaderBuilder;
import org.assertj.neo4j.api.beta.testing.builders.RelationshipsLoaderBuilder;
import org.assertj.neo4j.api.beta.type.DbNode;
import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.assertj.neo4j.api.beta.type.loader.Nodes;
import org.assertj.neo4j.api.beta.type.loader.Relationships;

/**
 * @author Patrick Allain - 12/02/2021
 */
public interface Loaders {

    static RelationshipsLoaderBuilder relationships() {
        return new RelationshipsLoaderBuilder();
    }

    static Relationships relationships(DbRelationship... entities) {
        return relationships().entities(entities).build();
    }

    static NodesLoaderBuilder nodes() {
        return new NodesLoaderBuilder();
    }

    static Nodes nodes(DbNode ... entities) {
        return nodes().entities(entities).build();
    }
}
