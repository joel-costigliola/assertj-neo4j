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
import org.assertj.neo4j.api.beta.type.Relationships.DbRelationship;

import java.util.List;

/**
 * @author Patrick Allain - 25/11/2020
 */
public class ShouldRelationshipHaveType extends BasicEntityErrorMessageFactory<DbRelationship> {

    /**
     * Creates a new <code>{@link BasicErrorMessageFactory}</code>.
     *
     * @param actual       the relationship
     * @param expectedType the expected type
     */
    public ShouldRelationshipHaveType(final DbRelationship actual, final String expectedType) {
        super(
                "%nExpecting relationship to have type:%n <%2$s>%n"
                + "but actual type is:%n <%3$s>%n",
                actual,
                ArgDetail.excluded(expectedType),
                ArgDetail.included("Actual type", actual.getType())
        );
    }

    public static ShouldRelationshipHaveType create(final DbRelationship actual, final String expectedType) {
        return new ShouldRelationshipHaveType(actual, expectedType);
    }

    public static GroupingEntityErrorFactory<DbRelationship> elements(
            final List<DbRelationship> actual, final String expectedType) {
        return new BasicGroupingEntityErrorFactory<>(
                actual,
                (r) -> create(r, expectedType),
                "%nExpecting relationships:%n"
                + "  <%1$s>%n"
                + "to have type:%n"
                + "  <%4$s>%n"
                + "but found other types for some relationships:",
                expectedType
        );
    }
}
