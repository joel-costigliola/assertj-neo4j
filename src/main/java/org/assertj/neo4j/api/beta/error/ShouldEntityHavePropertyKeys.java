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
 * Creates an error message indicating that an assertion that verifies a {@link ACTUAL} don't have the expected property
 * keys.
 *
 * @param <ACTUAL> the entity type
 * @author Patrick Allain  - 29/09/2020
 */
public class ShouldEntityHavePropertyKeys<ACTUAL extends DbEntity<ACTUAL>> extends BasicDbErrorMessageFactory<ACTUAL> {

    private ShouldEntityHavePropertyKeys(final ACTUAL actual, final Iterable<String> keys) {
        super(
                "%nExpecting " + actual.objectName(1) + " with property keys:%n  <%2$s>%n"
                + "to have property keys:%n  <%3$s>%n"
                + "but the following property keys cannot be found:%n  <%4$s>%n",
                actual,
                ArgDetail.included("Actual property keys", actual.getPropertyKeys()),
                ArgDetail.excluded(Utils.sorted(keys)),
                ArgDetail.included("Missing property keys", Utils.missing(keys, actual.getPropertyKeys()))
        );
    }

    public static <A extends DbEntity<A>> DbErrorMessageFactory<A> create(
            final A actual, final Iterable<String> keys) {
        return new ShouldEntityHavePropertyKeys<>(actual, keys);
    }

    public static <A extends DbEntity<A>> GroupingDbErrorFactory<A> elements(
            final List<A> actual, final Iterable<String> keys) {
        return new BasicDbGroupingErrorFactory<>(
                actual,
                (a) -> create(a, keys),
                "%nExpecting %3$s:%n  <%1$s>%n"
                + "to have properties with keys:%n  <%4$s>%n"
                + "but some %3$s don't have this properties:",
                Utils.sorted(keys)
        );
    }

}
