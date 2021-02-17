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
 * @author Patrick Allain - 10/02/2021
 */
public class ShouldPropertyMatch<ENTITY extends DbEntity> extends BasicEntityErrorMessageFactory<ENTITY> {

    protected ShouldPropertyMatch(final ENTITY actual, final String key) {
        super(
                "%nExpecting " + EntityUtils.recordTypeSingular(actual) + " to have property:%n <%2$s>%n"
                + "matching the provided condition for its value:%n <%3$s>%n"
                + "but this value of type %4$s did not.%n",
                actual,
                ArgDetail.excluded(key),
                ArgDetail.included("Actual property value", actual.getPropertyValue(key)),
                ArgDetail.included("Actual property type", actual.getPropertyType(key))
        );
    }

    public static <E extends DbEntity> ShouldPropertyMatch<E> create(final E actual, final String key) {
        return new ShouldPropertyMatch<>(actual, key);
    }

    public static <E extends DbEntity> GroupingEntityErrorFactory<E> elements(final List<E> actual,
                                                                              final String key) {
        return new BasicGroupingEntityErrorFactory<>(
                actual,
                (e) -> create(e, key),
                "%nExpecting %3$s:%n"
                + "  <%1$s>%n"
                + "to have a for the property %4$s matching the condition but %3$s:%n"
                + "  <%2$s>%n"
                + "did not:",
                key
        );
    }
}
