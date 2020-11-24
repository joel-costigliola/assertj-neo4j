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

import org.assertj.core.api.AbstractAssert;
import org.assertj.neo4j.api.beta.error.ShouldHaveRecord;
import org.assertj.neo4j.api.beta.error.ShouldHaveRecordNumber;
import org.assertj.neo4j.api.beta.error.ShouldHaveRecordType;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Value;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * First attempt to integrate result ... can be messy !
 *
 * Should be remove ?
 *
 * @author patouche - 5/26/20.
 */
public class DriverResultAssert extends AbstractAssert<DriverResultAssert, Result> {

    /** Register records here because they cannot be retrieve after a {@link Result#consume()} invocation. */
    private final List<Record> records;

    public DriverResultAssert(final Result result) {
        super(result, DriverResultAssert.class);
        this.records = result.list();
    }

    public DriverResultAssert haveRecords() {
        if (this.records.isEmpty()) {
            throwAssertionError(ShouldHaveRecord.create(actual.consume().query()));
        }
        return myself;
    }

    public DriverResultAssert haveRecords(final int size) {
        haveRecords();
        if (this.records.size() != size) {
            throwAssertionError(ShouldHaveRecordNumber.create(actual.consume().query(), size, this.records.size()));
        }
        return myself;
    }

    public DriverResultAssert haveTypeRecords(final RecordType type) {
        haveRecords();
        final List<RecordType> actualTypes = this.records.stream()
                .map(Record::values)
                .flatMap(Collection::stream)
                .map(Value::asObject)
                .map(RecordType::get)
                .distinct()
                .collect(Collectors.toList());
        if (actualTypes.stream().anyMatch(t -> t != type)) {
            throwAssertionError(ShouldHaveRecordType.create(actual.consume().query(), type, actualTypes));
        }
        return myself;
    }

    public DriverResultSummaryAssert asResultSummary() {
        return new DriverResultSummaryAssert(actual.consume());
    }

    public DriverRecordListAssert asSingleRecord() {
        return new DriverRecordListAssert(this.records);
    }


}
