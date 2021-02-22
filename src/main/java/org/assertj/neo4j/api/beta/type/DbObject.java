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

import org.neo4j.driver.Value;

import java.util.Arrays;
import java.util.Objects;

/**
 * An database object represent a returned value from the Neo4J driver.
 * <p/>
 * For more information, you can consult.
 * <ul>
 *     <li><a href='https://neo4j.com/docs/cypher-manual/current/syntax/values/#property-types'>property types</a></li>
 *     <li><a href='https://neo4j.com/docs/cypher-manual/current/syntax/values/#composite-types'>composite types</a>
 *     </li>
 *     <li><a href='https://neo4j.com/docs/cypher-manual/current/syntax/values/#structural-types'>structural types</a>
 *     </li>
 * </ul>
 *
 * @param <I> the type of db object
 * @author Patrick Allain - 18/02/2021
 */
public abstract class DbObject<I extends DbObject<I>> implements Representable<I> {

    protected final ObjectType objectType;

    private final Class<I> classType;

    protected DbObject(final ObjectType objectType, final Class<I> classType) {
        this.objectType = Objects.requireNonNull(objectType, "The object type cannot be null.");
        this.classType = Objects.requireNonNull(classType, "The class type cannot be null.");
    }

    public static DbObject<?> fromValue(final Value value) {
        if (value == null) {
            return null;
        }
        final Object obj = value.asObject();
        return Arrays.stream(ObjectType.values())
                .filter(ot -> ot.support(obj))
                .map(ot -> ot.convert(obj))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    public ObjectType objectType() {
        return objectType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final DbObject dbObject = (DbObject) o;
        return objectType == dbObject.objectType
               && Objects.equals(this.classType, ((DbObject<?>) o).classType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectType, classType);
    }
}
