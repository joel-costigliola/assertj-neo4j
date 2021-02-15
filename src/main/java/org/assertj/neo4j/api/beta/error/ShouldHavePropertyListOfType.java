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
import org.assertj.neo4j.api.beta.type.DbValue;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.assertj.neo4j.api.beta.util.EntityUtils;

import java.util.List;

/**
 * Creates an error message indicating that an assertion that verifies a {@link ENTITY} property list contains only
 * value of the expected type.
 *
 * @param <ENTITY> the entity type
 * @author Patrick ALLAIN  - 29/09/2020
 */
public class ShouldHavePropertyListOfType<ENTITY extends DbEntity> extends BasicEntityErrorMessageFactory<ENTITY> {

    private ShouldHavePropertyListOfType(final ENTITY actual, final String key, final ValueType type) {
        super(
                "%nExpected %s to have a composite property list named %s containing only type:%n"
                + "  <%s>%n"
                + "but this composite property list contains type:%n"
                + "  <%s>%n"
                + "with actual value:%n"
                + "  <%s>",
                actual,
                ArgDetail.excluded(key),
                ArgDetail.excluded(type),
                ArgDetail.included("Actual list value type",
                        EntityUtils.propertyList(actual, key)
                                .stream()
                                .findFirst()
                                .map(DbValue::getType)
                                .orElse(null)
                ),
                ArgDetail.included("Actual value", EntityUtils.properlyListValues(actual, key))
        );
    }

    public static <E extends DbEntity> ShouldHavePropertyListOfType<E> create(
            final E node, final String key, final ValueType type) {
        return new ShouldHavePropertyListOfType<>(node, key, type);
    }

    public static <E extends DbEntity> GroupingEntityErrorFactory<E> elements(
            final List<E> actual, final String key, final ValueType type) {
        return new BasicGroupingEntityErrorFactory<>(
                actual,
                (e) -> create(e, key, type),
                "%nExpecting %3$s:%n"
                + "  <%1$s>%n"
                + "to have a composite property list named %4$s containing only type:%n"
                + "  <%5$s>%n"
                + "but some %3$s have a composite list containing others type:",
                key,
                type
        );
    }
}
