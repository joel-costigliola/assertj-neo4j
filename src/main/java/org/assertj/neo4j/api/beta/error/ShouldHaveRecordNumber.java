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
import org.neo4j.driver.Query;

/**
 * @author patouche - 02/11/2020
 */
@Deprecated
public class ShouldHaveRecordNumber extends BasicErrorMessageFactory {

    private ShouldHaveRecordNumber(final Query query, final int expectedSize, final int actualSize) {
        super("\nExpecting:\n  <%s>\n  to return %d records\n  but retrieve <%d> records",
                query.text(), expectedSize, actualSize);
    }

    public static ShouldHaveRecordNumber create(final Query query, final int expectedSize, final int actualSize) {
        return new ShouldHaveRecordNumber(query, expectedSize, actualSize);
    }
}
