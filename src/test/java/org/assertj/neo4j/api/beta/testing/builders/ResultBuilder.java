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
package org.assertj.neo4j.api.beta.testing.builders;

import org.assertj.neo4j.api.beta.testing.MockResult;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.summary.ResultSummary;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Patrick Allain - 11/11/2020
 */
public class ResultBuilder {

    private final List<Record> records = new ArrayList<>();

    private ResultSummary summary;

    public ResultBuilder record(final Record record) {
        this.records.add(record);
        return this;
    }

    public ResultBuilder summary(final ResultSummary summary) {
        this.summary = summary;
        return this;
    }

    public Result build() {
        return new MockResult(records, summary);
    }
}
