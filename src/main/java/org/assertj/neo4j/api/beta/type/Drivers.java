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

/**
 * Create builder for generating object to compare
 *
 * @author patouche - 27/11/2020
 */
public final class Drivers {

    /**
     * Create a new {@link Nodes.DbNodeBuilder} to compare with the node you have in the database.
     *
     * @return a new {@link Nodes.DbNodeBuilder} instance
     */
    public static Nodes.DbNodeBuilder node() {
        return new Nodes.DbNodeBuilder();
    }

    /**
     * Create a new {@link Relationships.DbRelationshipBuilder} to compare with the relationships you have in the
     * database.
     *
     * @return a new {@link Relationships.DbRelationshipBuilder} instance
     */
    public static Relationships.DbRelationshipBuilder relation() {
        return new Relationships.DbRelationshipBuilder();
    }

    /**
     * Create a new {@link Relationships.DbRelationshipBuilder} with a specified type to compare with the
     * relationships you have in the database.
     *
     * @param type the relationships type
     * @return a new {@link Relationships.DbRelationshipBuilder} instance
     */
    public static Relationships.DbRelationshipBuilder relation(final String type) {
        return relation().type(type);
    }
}
