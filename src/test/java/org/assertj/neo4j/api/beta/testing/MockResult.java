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
package org.assertj.neo4j.api.beta.testing;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.exceptions.NoSuchRecordException;
import org.neo4j.driver.summary.ResultSummary;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Patrick Allain - 11/11/2020
 */
public class MockResult implements Result {

    private final List<Record> records;

    private final ResultSummary summary;

    private final Iterator<Record> iterator;

    public MockResult(List<Record> records, final ResultSummary summary) {
        this.records = records;
        this.summary = summary;
        this.iterator = records.iterator();
    }

    @Override
    public List<String> keys() {
        return null;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Record next() {
        return iterator.next();
    }

    @Override
    public Record single() throws NoSuchRecordException {
        if (records.size() != 1) {
            throw new NoSuchRecordException("Having records : " + records);
        }
        return records.get(0);
    }

    @Override
    public Record peek() {
        if (!hasNext()) {
            throw new NoSuchRecordException("No more records in iterator");
        }
        return next();
    }

    @Override
    public Stream<Record> stream() {
        return records.stream();
    }

    @Override
    public List<Record> list() {
        return records;
    }

    @Override
    public <T> List<T> list(final Function<Record, T> mapFunction) {
        return records.stream().map(mapFunction).collect(Collectors.toList());
    }

    @Override
    public ResultSummary consume() {
        return summary;
    }
}
