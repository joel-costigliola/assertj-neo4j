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

import org.assertj.neo4j.api.beta.type.DbValue;
import org.assertj.neo4j.api.beta.type.ObjectType;
import org.assertj.neo4j.api.beta.type.ValueType;

import java.util.List;

/**
 * @author Patrick Allain - 21/02/2021
 */
public class ShouldValueBeOfType extends BasicDbErrorMessageFactory<DbValue> {

    private ShouldValueBeOfType(final DbValue actual, final ValueType valueType) {
        super(
                "%nExpecting " + ObjectType.VALUE.format(1) + ":%n  <%s>%n"
                + "to have a value type:%n  <%s>%n"
                + "but actual value type is:%n  <%s>",
                actual,
                ArgDetail.excluded(valueType),
                ArgDetail.included("Actual value type", actual.getType())
        );
    }

    public static ShouldValueBeOfType create(final DbValue actual, final ValueType valueType) {
        return new ShouldValueBeOfType(actual, valueType);
    }

    public static GroupingDbErrorFactory<DbValue> elements(final List<DbValue> actual, final ValueType valueType) {
        return new BasicDbGroupingErrorFactory<>(
                actual,
                (v) -> create(v, valueType),
                "%nExpecting " + ObjectType.VALUE.format(actual.size()) + ":%n  <%1$s>%n"
                + "to have a value type:%n  <%4$s>%n"
                + "but some " + ObjectType.VALUE.format(actual.size()) + " have a different value type:",
                valueType
        );
    }
}
