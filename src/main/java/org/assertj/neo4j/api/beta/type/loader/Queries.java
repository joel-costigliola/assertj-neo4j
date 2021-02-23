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
package org.assertj.neo4j.api.beta.type.loader;

import org.assertj.core.util.Maps;
import org.neo4j.driver.Query;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Patrick Allain - 10/02/2021
 */
public interface Queries {

    static Query relationships(final String... types) {
        final String joined = Arrays.stream(types).filter(Objects::nonNull).sorted().collect(Collectors.joining("|"));
        final String orTypes = joined.isEmpty() ? "" : " :" + joined;
        return new Query(String.format("MATCH ()-[r%s]->() RETURN r", orTypes));
    }

    static Query nodes(final String... labels) {
        final String queryLabels = Arrays.stream(labels).map(i -> ":" + i).collect(Collectors.joining(""));
        return new Query(String.format("MATCH (n %s) RETURN n", queryLabels));
    }

    static Query nodeByIds(final Long... ids) {
        return new Query("MATCH (n) WHERE id(n) IN $ids RETURN n", Maps.newHashMap("ids", Arrays.asList(ids)));
    }
}
