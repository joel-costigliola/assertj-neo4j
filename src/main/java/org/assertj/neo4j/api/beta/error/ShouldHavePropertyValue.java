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
import org.assertj.neo4j.api.beta.util.EntityUtils;

import java.util.List;

/**
 * Creates an error message indicating that an assertion that verifies {@link ENTITY}  don't have the expected property
 * value.
 *
 * @param <ENTITY> the entity type
 * @author Patrick Allain - 31/01/2021
 */
public class ShouldHavePropertyValue<ENTITY extends DbEntity> extends BasicEntityErrorMessageFactory<ENTITY> {

    private ShouldHavePropertyValue(final ENTITY actual, final String key, final Object value) {
        super(
                "%nExpecting " + EntityUtils.recordTypeSingular(actual)
                + " to have a property %2$s with value:%n <%3$s>%n"
                + "but current value of this property is:%n <%4$s>%n",
                actual,
                ArgDetail.excluded(key),
                ArgDetail.excluded(value),
                ArgDetail.included("Actual value", actual.getPropertyValue(key)),
                ArgDetail.included("Actual type", actual.getPropertyType(key))
        );
    }

    public static <E extends DbEntity> ShouldHavePropertyValue<E> create(
            final E actual, final String key, final Object value) {
        return new ShouldHavePropertyValue<>(actual, key, value);
    }

    public static <E extends DbEntity> GroupingEntityErrorFactory<E> elements(
            final List<E> actual,
            final String key,
            final Object value) {
        return new BasicGroupingEntityErrorFactory<>(
                actual,
                (e) -> create(e, key, value),
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
