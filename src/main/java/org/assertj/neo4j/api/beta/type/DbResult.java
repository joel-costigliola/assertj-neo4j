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

import org.neo4j.driver.Result;
import org.neo4j.driver.summary.ResultSummary;

import java.util.List;
import java.util.Map;

/**
 * @author Patrick Allain - 17/02/2021
 */
public class DbResult {

    private final List<Map<String, DbObject>> records;

    private final List<String> columns;

    private final ResultSummary summary;

    // TODO : Should we add a value RELATIONSHIP and NODE in ValueType and apply the conversion directory ?
    public DbResult(final List<Map<String, DbObject>> records, List<String> columns, ResultSummary summary) {
        this.records = records;
        this.columns = columns;
        this.summary = summary;
    }

    public static DbResult from(final Result result) {
        return new DbResult(result.list(r -> r.asMap(DbValue::fromValue)), result.keys(), result.consume());
    }

    public List<Map<String, DbObject>> getRecords() {
        return records;
    }

    public List<String> getColumns() {
        return columns;
    }

    public ResultSummary getSummary() {
        return summary;
    }
}
