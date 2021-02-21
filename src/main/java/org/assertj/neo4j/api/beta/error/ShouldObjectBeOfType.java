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

import org.assertj.neo4j.api.beta.type.DbObject;
import org.assertj.neo4j.api.beta.type.ObjectType;

import java.util.List;

/**
 * @author patouche - 18/02/2021
 */
public class ShouldObjectBeOfType<ACTUAL extends DbObject<ACTUAL>> extends BasicDbErrorMessageFactory<ACTUAL> {

    public ShouldObjectBeOfType(ACTUAL actual, ObjectType type) {
        super(
                "%nExpecting " + actual.objectName(1) + ":%n  <%s>%n"
                + "to have type:%n  <%s>%n"
                + "but current type is:%n  <%s>%n",
                actual,
                ArgDetail.excluded(type),
                ArgDetail.included("Current type", actual.objectType())
        );
    }

    public static <A extends DbObject<A>> ShouldObjectBeOfType<A> create(A actual, ObjectType type) {
        return new ShouldObjectBeOfType<A>(actual, type);
    }

    public static GroupingDbErrorFactory.RawDbErrorFactory elements(List<DbObject> actual, ObjectType type) {
        return new BasicDbGroupingErrorFactory.BasicRawDbErrorFactory(
                actual,
                (a) -> create(a, type),
                "%nExpecting %3$s:%n  <%s>%n"
                + "to be of type:%n  <%4$s>%n"
                + "but some %3$s are from another type:",
                type
        );
    }
}
