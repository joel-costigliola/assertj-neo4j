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

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.util.EntityUtils;
import org.assertj.neo4j.api.beta.util.Presentations;
import org.neo4j.driver.Query;

import java.util.List;

/**
 * @author patouche - 13/02/2021
 */
public class ShouldQueryResultBeEmpty<ENTITY extends DbEntity<ENTITY>> extends BasicErrorMessageFactory {

    private ShouldQueryResultBeEmpty(final List<ENTITY> actual, final Query query) {
        super(
                "%nExpecting query:%n"
                + "  <%s>%n"
                + "to return an empty list of %s got:%n"
                + "  <%s>",
                query,
                unquotedString(EntityUtils.type(actual).pluralForm()),
                Presentations.outputIds(actual)
        );
    }

    public static <E extends DbEntity<E>> ShouldQueryResultBeEmpty<E> create(final List<E> actual, final Query query) {
        return new ShouldQueryResultBeEmpty<>(actual, query);
    }

}
