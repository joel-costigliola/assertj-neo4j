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

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.neo4j.driver.Query;

/**
 * Creates an error message indicating that an assertion that verifies a query should return a non empty result list.
 *
 * @author Patrick Allain - 14/02/2021
 */
public class ShouldQueryResultBeNotEmpty extends BasicErrorMessageFactory {

    private ShouldQueryResultBeNotEmpty(final RecordType type, final Query query) {
        super(
                "%nExpecting query:%n"
                + "  <%s>%n"
                + "to return a non empty result list of %s but got no result.",
                query,
                unquotedString(type.pluralForm())
        );
    }

    public static ErrorMessageFactory create(final RecordType type, final Query query) {
        return new ShouldQueryResultBeNotEmpty(type, query);
    }
}
