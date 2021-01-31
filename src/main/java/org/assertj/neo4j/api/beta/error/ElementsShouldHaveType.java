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
package org.assertj.neo4j.api.beta.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.type.Relationships.DbRelationship;
import org.assertj.neo4j.api.beta.util.Presentations;
import org.assertj.neo4j.api.beta.util.RelationshipTypes;

import java.util.List;

/**
 * @author patouche - 25/11/2020
 */
public class ElementsShouldHaveType extends DbEntitiesMessageFactory<DbRelationship, String, DbRelationship> {

    /**
     * Creates a new <code>{@link BasicErrorMessageFactory}</code>.
     *
     * @param actual       the relationships
     * @param expectedType the expected type
     */
    public ElementsShouldHaveType(final List<DbRelationship> actual, final String expectedType) {
        super(
                RecordType.RELATIONSHIP,
                "to be of type",
                "but some relationships have an other type",
                actual,
                expectedType,
                RelationshipTypes.others(expectedType, actual),
                factory()
        );
    }

    private static ItemMessageFactory<String, DbRelationship> factory() {
        return (expected, relationship) -> String.format(
                "  - %s doesn't have the expected type:%n"
                + "      Actual  : %s%n"
                + "      Expected: %s",
                Presentations.outputId(relationship), relationship.getType(), expected
        );
    }

    public static ElementsShouldHaveType create(
            final List<DbRelationship> actual, final String expectedType) {
        return new ElementsShouldHaveType(actual, expectedType);
    }
}
