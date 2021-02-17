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
 * Creates an error message indicating that an assertion that verifies a {@link ENTITY} property value is not of the
 * expected type.
 *
 * @param <ENTITY> the entity type
 * @author Patrick Allain - 30/01/2021
 */
public class ShouldHavePropertyInstanceOf<ENTITY extends DbEntity> extends BasicEntityErrorMessageFactory<ENTITY> {

    private ShouldHavePropertyInstanceOf(final ENTITY actual, final String key, final Class<?> expectedClass) {
        super(
                "%nExpecting " + EntityUtils.recordTypeSingular(actual)
                + " to have property value %2$s instance of:%n <%3$s>%n"
                + "but the actual property value for this key has class :%n <%4$s>%n"
                + "which is not an instance of the expected class.%n%n"
                + "Actual value for this property is:%n <%5$s>%n",
                actual,
                ArgDetail.excluded(key),
                ArgDetail.excluded(expectedClass),
                ArgDetail.included("Actual value class", actual.getPropertyValue(key).getClass()),
                ArgDetail.included("Actual value", actual.getPropertyValue(key))
        );
    }

    public static <E extends DbEntity> ShouldHavePropertyInstanceOf<E> create(
            final E actual, final String key, final Class<?> expectedClass) {
        return new ShouldHavePropertyInstanceOf<>(actual, key, expectedClass);
    }

    public static <E extends DbEntity> GroupingEntityErrorFactory<E> elements(
            final List<E> actual, final String key, final Class<?> expectedClass) {
        return new BasicGroupingEntityErrorFactory<>(
                actual,
                (e) -> create(e, key, expectedClass),
                "%nExpecting %3$s:%n"
                + "  <%1$s>%n"
                + "to have property value %4$s instance of:%n"
                + "  <%5$s>%n"
                + "but some %3$s have a property value which is not an instance of the expected class:",
                key,
                expectedClass
        );
    }
}
