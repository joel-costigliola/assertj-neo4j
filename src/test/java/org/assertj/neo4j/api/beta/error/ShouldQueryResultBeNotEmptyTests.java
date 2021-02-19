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

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.neo4j.api.beta.type.ObjectType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Query;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 14/02/2021
 */
class ShouldQueryResultBeNotEmptyTests {

    @Nested
    class CreateTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final Query query = new Query("MATCH (n) RETURN n");

            // WHEN
            final ErrorMessageFactory error = ShouldQueryResultBeNotEmpty.create(ObjectType.NODE, query);

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\n"
                    + "Expecting query:\n"
                    + "  <Query{text='MATCH (n) RETURN n', parameters={}}>\n"
                    + "to return a non empty result list of nodes but got no result."
            );
        }
    }

}
