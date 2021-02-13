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
 * Creates an error message indicating that an assertion that verifies {@link ENTITY} properties don't have the expected
 * size.
 *
 * @param <ENTITY> the entity type
 * @author Patrick Allain - 01/02/2021
 */
public class ShouldHavePropertySize<ENTITY extends DbEntity<ENTITY>> extends BasicEntityErrorMessageFactory<ENTITY> {

    private ShouldHavePropertySize(final ENTITY actual, final int size) {
        super(
                "%nExpecting %s to have property size:%n"
                + "  <%s>%n"
                + "but actual property size is:%n"
                + "  <%s>%n"
                + "containing the following property keys:%n"
                + "  <%s>",
                actual,
                ArgDetail.excluded(size),
                ArgDetail.included("Actual property size", actual.getPropertyKeys().size()),
                ArgDetail.included("Actual property keys", actual.getPropertyKeys())
        );
    }

    public static <E extends DbEntity<E>> ShouldHavePropertySize<E> create(final E actual, final int size) {
        return new ShouldHavePropertySize<>(actual, size);
    }

    public static <E extends DbEntity<E>> GroupingEntityErrorFactory<E> elements(final List<E> actual, final int size) {
        return new BasicGroupingEntityErrorFactory<>(
                actual,
                (e) -> create(e, size),
                "%nExpecting %3$s:%n"
                + "  <%1$s>%n"
                + "to have a property size:%n"
                + "  <%4$s>%n"
                + "but some %3$s have another property size:",
                size
        );
    }
}
