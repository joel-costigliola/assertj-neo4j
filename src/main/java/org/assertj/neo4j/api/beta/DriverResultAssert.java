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

import org.assertj.neo4j.api.beta.error.ShouldHaveValueType;
import org.assertj.neo4j.api.beta.type.DbResult;
import org.assertj.neo4j.api.beta.type.DbValue;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.assertj.neo4j.api.beta.util.Predicates;
import org.assertj.neo4j.api.beta.util.Wip;
import org.neo4j.driver.Result;

import java.util.List;
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

    private DriverResultAssert(final DbResult actual, final DriverResultAssert parent) {
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
        iterables.assertContains(info, actual.getColumns(), names);
        return myself;
    }

    public DriverResultAssert isNode(final String columnName) {
        hasColumns(columnName);
        final List<DbValue> values = actual.getRecords().stream()
                .map(m -> m.get(columnName))
                .collect(Collectors.toList());
        shouldAllVerify(values,
                Predicates.isValueType(ValueType.NODE),
                (i) -> ShouldHaveValueType
                        .elements(values, ValueType.NODE)
                        .notSatisfies(i)
        );
        Wip.TODO(this);
        // shouldAllVerify(values, Predicates.isValueType(ValueType.NODE))
        return myself;
    }

    public DriverNodesAssert asNodesAssert(String s) {
        Wip.TODO(this);
        return null;
    }

    public <T> NavigableListAssert<T, DriverResultAssert, DriverResultAssert> asListOf(Class<T> clazz) {
        return null;
    }

}
