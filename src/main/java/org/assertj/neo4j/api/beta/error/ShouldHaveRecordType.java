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
import org.assertj.neo4j.api.beta.type.ObjectType;
import org.neo4j.driver.Query;

import java.util.List;

/**
 * @author Patrick Allain - 02/11/2020
 */
@Deprecated
public class ShouldHaveRecordType extends BasicErrorMessageFactory {

    public ShouldHaveRecordType(final Query query, final ObjectType expectedType, final List<ObjectType> actualTypes) {
        super("Expecting:\n  <%s> to return records <%s> type\n  but got <%s>",
                query.text(), expectedType, actualTypes
        );
    }

    public static ShouldHaveRecordType create(final Query query, final ObjectType expectedType,
            final List<ObjectType> actualTypes) {
        return new ShouldHaveRecordType(query, expectedType, actualTypes);
    }
}
