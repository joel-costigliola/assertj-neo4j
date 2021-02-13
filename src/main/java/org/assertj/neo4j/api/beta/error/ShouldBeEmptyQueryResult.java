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
import org.neo4j.driver.Query;

import java.util.List;

/**
 * @author patouche - 13/02/2021
 */
public class ShouldBeEmptyQueryResult<ENTITY extends DbEntity<ENTITY>>
        extends BasicEntityErrorMessageFactory<ENTITY> {

    private ShouldBeEmptyQueryResult(final ENTITY actual, final Query query) {
        super(
                "%nExpecting query:%n"
                + "  <%2$s>%n"
                + "to return a empty list but got:%n"
                + "  <%1$s>",
                actual,
                ArgDetail.excluded(query)
        );
    }

    public static <E extends DbEntity<E>> ShouldBeEmptyQueryResult<E> create(E actual, Query query) {
        return new ShouldBeEmptyQueryResult<>(actual, query);
    }

    public static <E extends DbEntity<E>> GroupingEntityErrorFactory<E> elements(List<E> actual, Query query) {
        return new BasicGroupingEntityErrorFactory<>(
                actual,
                (e) -> create(e, query),
                "%nExpecting query:%n"
                + "  <%4$s>%n"
                + "to return no %3$s but got:%n"
                + "  <%1$s>",
                query
        );
    }

}
