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
package org.assertj.neo4j.api.beta.type;

import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;
import org.neo4j.driver.types.Relationship;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Record type.
 *
 * @author Patrick Allain - 02/11/2020
 */
public enum RecordType {

    NODE(Node.class, "nodes"),
    RELATIONSHIP(Relationship.class, "relationships"),
    PATH(Path.class, "paths"),
    ;

    private final static Map<Class<?>, RecordType> MAPPING = Arrays.stream(values())
            .collect(Collectors.toMap(i -> i.clazz, Function.identity()));

    private final Class<?> clazz;

    private final String pluralForm;

    RecordType(final Class<?> clazz, final String pluralForm) {
        this.clazz = clazz;
        this.pluralForm = pluralForm;
    }

    public static RecordType get(final Object object) {
        for (Map.Entry<Class<?>, RecordType> entry : MAPPING.entrySet()) {
            if (entry.getKey().isInstance(object)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public String pluralForm() {
        return pluralForm;
    }
}
