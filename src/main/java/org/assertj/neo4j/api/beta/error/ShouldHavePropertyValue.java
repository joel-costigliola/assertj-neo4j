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

import java.util.List;

/**
 * Creates an error message indicating that an assertion that verifies {@link ACTUAL}  don't have the expected property
 * value.
 *
 * @param <ACTUAL> the entity type
 * @author Patrick Allain - 31/01/2021
 */
public class ShouldHavePropertyValue<ACTUAL extends DbEntity<ACTUAL>> extends BasicDbErrorMessageFactory<ACTUAL> {

    private ShouldHavePropertyValue(final ACTUAL actual, final String key, final Object value) {
        super(
                "%nExpecting " + actual.objectName(1) + " to have a property %2$s with value:%n  <%3$s>%n"
                + "but current value of this property is:%n  <%4$s>%n",
                actual,
                ArgDetail.excluded(key),
                ArgDetail.excluded(value),
                ArgDetail.included("Actual value", actual.getPropertyValue(key)),
                ArgDetail.included("Actual type", actual.getPropertyType(key))
        );
    }

    public static <A extends DbEntity<A>> ShouldHavePropertyValue<A> create(
            final A actual, final String key, final Object value) {
        return new ShouldHavePropertyValue<>(actual, key, value);
    }

    public static <A extends DbEntity<A>> GroupingDbErrorFactory<A> elements(
            final List<A> actual,
            final String key,
            final Object value) {
        return new BasicDbGroupingErrorFactory<>(
                actual,
                (a) -> create(a, key, value),
                "%nExpecting %3$s:%n"
                + "  <%1$s>%n"
                + "to have a property named %4$s with value:%n"
                + "  <%5$s>%n"
                + "but some %3$s have a different value for this property:",
                key,
                value
        );
    }
}
