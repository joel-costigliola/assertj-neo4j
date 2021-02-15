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

import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.ValueType;

import java.util.List;

/**
 * Creates an error message indicating that an assertion that verifies {@link ENTITY}  don't have the expected property
 * value type.
 *
 * @param <ENTITY> the entity type
 * @author Patrick Allain - 31/01/2021
 */
public class ShouldHavePropertyValueType<ENTITY extends DbEntity> extends BasicEntityErrorMessageFactory<ENTITY> {

    private ShouldHavePropertyValueType(final ENTITY actual, final String key, final ValueType expectedType) {
        super(
                "%nExpecting %s to have property value type for key %s:%n"
                + "  <%s>%n"
                + "but actual value type for this property key is:%n"
                + "  <%s>%n%n"
                + "Actual property value:%n"
                + "  <%s>",
                actual,
                ArgDetail.excluded(key),
                ArgDetail.excluded(expectedType),
                ArgDetail.included("Actual value type", actual.getPropertyType(key)),
                ArgDetail.included("Actual value", actual.getPropertyValue(key))
        );
    }

    public static <E extends DbEntity> ShouldHavePropertyValueType<E> create(
            final E actual, final String key, final ValueType expectedType) {
        return new ShouldHavePropertyValueType<>(actual, key, expectedType);
    }

    public static <E extends DbEntity> GroupingEntityErrorFactory<E> elements(
            final List<E> actual, final String key, final ValueType expectedType) {
        return new BasicGroupingEntityErrorFactory<>(
                actual,
                (e) -> create(e, key, expectedType),
                "%nExpecting %3$s:%n"
                + "  <%1$s>%n"
                + "to have a property %4$s with a value type:%n"
                + "  <%5$s>%n"
                + "but some %3$s have a different property value type:",
                key,
                expectedType
        );
    }
}
