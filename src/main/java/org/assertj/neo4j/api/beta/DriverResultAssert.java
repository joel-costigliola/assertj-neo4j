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
package org.assertj.neo4j.api.beta;

import org.assertj.core.util.Preconditions;
import org.assertj.neo4j.api.beta.error.ShouldObjectBeOfType;
import org.assertj.neo4j.api.beta.error.ShouldValueBeInstanceOf;
import org.assertj.neo4j.api.beta.error.ShouldValueBeOfType;
import org.assertj.neo4j.api.beta.type.DbObject;
import org.assertj.neo4j.api.beta.type.DbResult;
import org.assertj.neo4j.api.beta.type.DbValue;
import org.assertj.neo4j.api.beta.type.ObjectType;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.assertj.neo4j.api.beta.util.GroupNames;
import org.assertj.neo4j.api.beta.util.Predicates;
import org.assertj.neo4j.api.beta.util.Utils;
import org.assertj.neo4j.api.beta.util.Wip;
import org.neo4j.driver.Result;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Patrick Allain - 26/01/2021
 */
//@formatter:off
public class DriverResultAssert
        extends AbstractDbAssert<DriverResultAssert,
                                 DbResult,
                                 DriverResultAssert,
                                 DriverResultAssert,
                                 DriverResultAssert>
        implements ParentalAssert {
//@formatter:on

    public DriverResultAssert(final Result result) {
        this(DbResult.from(result), null);
    }

    protected DriverResultAssert(final DbResult actual, final DriverResultAssert parent) {
        super(
                actual,
                DriverResultAssert.class,
                DriverResultAssert::new,
                parent,
                Navigable.rootAssert(parent)
        );
    }

    @Override
    public DriverResultAssert toRootAssert() {
        return rootAssert().orElse(this);
    }

    public DriverResultAssert hasRecordSize(int expectedSize) {
        iterables.assertHasSize(info, actual.getRecords(), expectedSize);
        return myself;
    }

    public DriverResultAssert hasColumnSize(int expectedColumn) {
        iterables.assertHasSize(info, actual.getColumns(), expectedColumn);
        return myself;
    }

    public DriverResultAssert hasColumns(final String... names) {
        iterables.assertContains(info, GroupNames.columns(actual.getColumns()), names);
        return myself;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public DriverResultAssert haveType(final String columnName, final ObjectType type) {
        hasColumns(columnName);
        final List<DbObject> values = actual.getRecords().stream()
                .map(m -> m.get(columnName))
                .collect(Collectors.toList());
        return this.<DbObject>shouldAllVerify(values,
                Predicates.isObjectType(type),
                (notSatisfies) -> ShouldObjectBeOfType.elements(values, type).notSatisfies(notSatisfies)
        );
    }

    private <O extends DbObject<O>> List<O> getColumnObjects(String columnName, ObjectType type, Class<O> targetClass) {
        Preconditions.checkArgument(
                Objects.equals(type.getTargetClass(), targetClass),
                "Object type and target class don't match"
        );
        haveType(columnName, type);
        return actual.getRecords().stream()
                .map(i -> i.get(columnName))
                .map(targetClass::cast)
                .collect(Collectors.toList());
    }

    public DriverResultAssert haveValueType(final String columnName, final ValueType valueType) {
        final List<DbValue> objects = getColumnObjects(columnName, ObjectType.VALUE, DbValue.class);
        return shouldAllVerify(
                objects,
                Predicates.isValueType(valueType),
                (notSatisfies) -> ShouldValueBeOfType.elements(objects, valueType).notSatisfies(notSatisfies)
        );
    }

    public <T> DriverResultAssert haveValueInstanceOf(final String columnName, final Class<T> expectedClass) {
        final List<DbValue> objects = getColumnObjects(columnName, ObjectType.VALUE, DbValue.class);
        return shouldAllVerify(
                objects,
                Predicates.isValueInstanceOf(expectedClass),
                (notSatisfies) -> ShouldValueBeInstanceOf.elements(objects, expectedClass).notSatisfies(notSatisfies)
        );
    }

    public DriverNodesAssert asNodesAssert(final String columnName) {
        Wip.TODO(this);
        return null;
    }

    public <T> ChildrenListAssert<T, DriverResultAssert, DriverResultAssert> asListOf(Class<T> clazz) {
        iterables.assertHasSize(info, actual.getColumns(), 1);
        final String columnName = Utils.first(actual.getColumns());
        haveValueInstanceOf(columnName, clazz);
        return asListOf(columnName, clazz);
    }

    public <T> ChildrenListAssert<T, DriverResultAssert, DriverResultAssert> asListOf(
            final String columnName, final Class<T> clazz) {
        haveValueInstanceOf(columnName, clazz);
        final List<T> objects = getColumnObjects(columnName, ObjectType.VALUE, DbValue.class)
                .stream()
                .map(DbValue::getContent)
                .map(clazz::cast)
                .collect(Collectors.toList());
        return new ChildrenListAssert<>(objects, this, this.toRootAssert());
    }

}
