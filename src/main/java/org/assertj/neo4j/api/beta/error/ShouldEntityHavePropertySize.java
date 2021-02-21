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
 * Creates an error message indicating that an assertion that verifies {@link ACTUAL} properties don't have the expected
 * size.
 *
 * @param <ACTUAL> the entity type
 * @author Patrick Allain - 01/02/2021
 */
public class ShouldEntityHavePropertySize<ACTUAL extends DbEntity<ACTUAL>> extends BasicDbErrorMessageFactory<ACTUAL> {

    private ShouldEntityHavePropertySize(final ACTUAL actual, final int size) {
        super(
                "%nExpecting " + actual.objectName(1) + " to have property size:%n  <%2$s>%n"
                + "but actual property size is:%n  <%3$s>%n"
                + "containing the following property keys:%n  <%4$s>%n",
                actual,
                ArgDetail.excluded(size),
                ArgDetail.included("Actual property size", actual.getPropertyKeys().size()),
                ArgDetail.included("Actual property keys", actual.getPropertyKeys())
        );
    }

    public static <A extends DbEntity<A>> ShouldEntityHavePropertySize<A> create(final A actual, final int size) {
        return new ShouldEntityHavePropertySize<>(actual, size);
    }

    public static <A extends DbEntity<A>> GroupingDbErrorFactory<A> elements(final List<A> actual, final int size) {
        return new BasicDbGroupingErrorFactory<>(
                actual,
                (a) -> create(a, size),
                "%nExpecting %3$s:%n"
                + "  <%1$s>%n"
                + "to have a property size:%n"
                + "  <%4$s>%n"
                + "but some %3$s have another property size:",
                size
        );
    }
}
