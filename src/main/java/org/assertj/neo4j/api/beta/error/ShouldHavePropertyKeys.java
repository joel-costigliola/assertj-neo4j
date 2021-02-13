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
import org.assertj.neo4j.api.beta.util.Utils;

import java.util.List;

/**
 * Creates an error message indicating that an assertion that verifies a {@link ENTITY} don't have the expected property
 * keys.
 *
 * @param <ENTITY> the entity type
 * @author Patrick Allain  - 29/09/2020
 */
public class ShouldHavePropertyKeys<ENTITY extends DbEntity<ENTITY>> extends BasicEntityErrorMessageFactory<ENTITY> {

    private ShouldHavePropertyKeys(final ENTITY actual, final Iterable<String> keys) {
        super(
                "%nExpecting %s with property keys:%n"
                + "  <%s>%n"
                + "to have property keys:%n"
                + "  <%s>%n"
                + "but the following property keys cannot be found:%n"
                + "  <%s>",
                actual,
                ArgDetail.included("Actual property keys", actual.getPropertyKeys()),
                ArgDetail.excluded(Utils.sorted(keys)),
                ArgDetail.included("Missing property keys", Utils.missing(keys, actual.getPropertyKeys()))
        );
    }

    public static <E extends DbEntity<E>> EntityErrorMessageFactory<E> create(
            final E actual, final Iterable<String> keys) {
        return new ShouldHavePropertyKeys<>(actual, keys);
    }

    public static <E extends DbEntity<E>> GroupingEntityErrorFactory<E> elements(
            final List<E> actual, final Iterable<String> keys) {
        return new BasicGroupingEntityErrorFactory<>(
                actual,
                (e) -> create(e, keys),
                "%nExpecting %3$s:%n"
                + "  <%1$s>%n"
                + "to have properties with keys:%n"
                + "  <%4$s>%n"
                + "but some %3$s don't have this properties:",
                Utils.sorted(keys)
        );
    }

}
