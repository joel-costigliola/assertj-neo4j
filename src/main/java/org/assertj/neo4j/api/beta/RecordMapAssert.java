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
import org.assertj.core.api.MapAssert;
import org.assertj.core.api.ObjectAssert;

import java.util.Map;

/**
 * @author Patrick Allain - 26/01/2021
 */
public class RecordMapAssert extends AbstractAssert<RecordMapAssert, Map<String, Object>> {

    public RecordMapAssert(Map<String, Object> stringObjectMap) {
        super(stringObjectMap, RecordMapAssert.class);
    }
}
