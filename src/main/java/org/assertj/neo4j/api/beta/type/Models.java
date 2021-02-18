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
 * @author Patrick Allain - 27/11/2020
 */
public final class Models {

    /**
     * Create a new {@link DbNode.DbNodeBuilder} to compare with the node you have in the database.
     *
     * @return a new {@link DbNode.DbNodeBuilder} instance
     */
    public static DbNode.DbNodeBuilder node() {
        return new DbNode.DbNodeBuilder();
    }

    /**
     * Create a new {@link DbRelationship.DbRelationshipBuilder} to compare with the relationships you have in the
     * database.
     *
     * @return a new {@link DbRelationship.DbRelationshipBuilder} instance
     */
    public static DbRelationship.DbRelationshipBuilder relation() {
        return new DbRelationship.DbRelationshipBuilder();
    }

    /**
     * Create a new {@link DbRelationship.DbRelationshipBuilder} with a specified type to compare with the relationships
     * you have in the database.
     *
     * @param type the relationships type
     * @return a new {@link DbRelationship.DbRelationshipBuilder} instance
     */
    public static DbRelationship.DbRelationshipBuilder relation(final String type) {
        return relation().type(type);
    }

    private Models() {}

}
