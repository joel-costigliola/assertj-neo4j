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
import org.neo4j.driver.summary.QueryType;
import org.neo4j.driver.summary.ResultSummary;

import java.util.stream.Stream;

/**
 * @author patouche - 29/09/2020
 */
public class DriverResultSummaryAssert extends AbstractAssert<DriverResultSummaryAssert, ResultSummary> {

    public DriverResultSummaryAssert(final ResultSummary actual) {
        super(actual, DriverResultSummaryAssert.class);
    }

    public DriverResultSummaryAssert isQueryType(QueryType expectedQueryType) {
        return isOneOfQueryTypes(expectedQueryType);
    }

    public DriverResultSummaryAssert isOneOfQueryTypes(QueryType... acceptedQueryTypes) {
        final QueryType actualQueryType = actual.queryType();
        if (Stream.of(acceptedQueryTypes).noneMatch(eqt -> eqt == actualQueryType)) {
            this.failWithMessage(
                "Expected query type in %s but actual query type is '%s'",
                acceptedQueryTypes, actualQueryType);
        }
        return myself;
    }

    public DriverResultSummaryAssert isNotQueryType(QueryType... rejectedQueryType) {
        return isNotOneOfQueryTypes(rejectedQueryType);
    }

    public DriverResultSummaryAssert isNotOneOfQueryTypes(QueryType... rejectedQueryTypes) {
        final QueryType actualQueryType = actual.queryType();
        if (Stream.of(rejectedQueryTypes).anyMatch(eqt -> eqt == actualQueryType)) {
            this.failWithMessage(
                "Expected query type not in %s but actual query type is '%s'",
                rejectedQueryTypes, actualQueryType);
        }
        return myself;
    }

}
