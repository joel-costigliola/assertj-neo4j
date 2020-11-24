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
package org.assertj.neo4j.api.beta.output;

import org.assertj.core.util.Streams;
import org.assertj.neo4j.api.beta.type.DbEntity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Raw representation of various type of object.
 *
 * @author patouche - 14/11/2020
 */
public class Representations {

    public static <E extends DbEntity<E>> String id(final E entity) {
        return entity.getType() + "{id=" + entity.getId() + "}";
    }

    public static <E extends DbEntity<E>> List<String> ids(final Iterable<E> entities) {
        return Streams.stream(entities)
                .sorted(Comparator.comparing(DbEntity::getId))
                .map(Representations::id)
                .collect(Collectors.toList());
    }

}
