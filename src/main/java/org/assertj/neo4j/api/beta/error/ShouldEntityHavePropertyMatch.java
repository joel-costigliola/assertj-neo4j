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
 * Creates an error message indicating that an assertion that verifies a {@link ACTUAL} property value don't match a
 * predicate.
 *
 * @param <ACTUAL> the entity type
 * @author Patrick Allain - 10/02/2021
 */
public class ShouldEntityHavePropertyMatch<ACTUAL extends DbEntity<ACTUAL>> extends BasicDbErrorMessageFactory<ACTUAL> {

    protected ShouldEntityHavePropertyMatch(final ACTUAL actual, final String key) {
        super(
                "%nExpecting " + actual.objectName(1) + " to have property:%n  <%2$s>%n"
                + "matching the provided condition for its value:%n  <%3$s>%n"
                + "but this value of type %4$s did not.%n",
                actual,
                ArgDetail.excluded(key),
                ArgDetail.included("Actual property value", actual.getPropertyValue(key)),
                ArgDetail.included("Actual property type", actual.getPropertyType(key))
        );
    }

    public static <A extends DbEntity<A>> ShouldEntityHavePropertyMatch<A> create(final A actual, final String key) {
        return new ShouldEntityHavePropertyMatch<>(actual, key);
    }

    public static <A extends DbEntity<A>> GroupingDbErrorFactory<A> elements(final List<A> actual, final String key) {
        return new BasicDbGroupingErrorFactory<>(
                actual,
                (a) -> create(a, key),
                "%nExpecting %3$s:%n  <%1$s>%n"
                + "to have a for the property %4$s matching the condition but %3$s:%n  <%2$s>%n"
                + "did not:",
                key
        );
    }
}
