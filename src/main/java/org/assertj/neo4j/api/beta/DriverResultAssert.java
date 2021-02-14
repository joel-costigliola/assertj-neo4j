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

import org.assertj.core.api.AbstractIterableAssert;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;

import java.util.List;
import java.util.Map;

/**
 * @author Patrick Allain - 26/01/2021
 */
public class DriverResultAssert  extends AbstractIterableAssert<
        DriverResultAssert, List<Map<String, Object>>, Map<String, Object>, RecordMapAssert> {

    private Result result;

    public DriverResultAssert(final Result result) {
        super(result.list(Record::asMap), DriverResultAssert.class);
    }

    public DriverResultAssert hasColumnNumber(int i) {
        return myself;
    }

    public DriverResultAssert isNode(String n) {
        return myself;
    }

    public DriverNodesAssert asNodesAssert(String s) {
        return null;
    }

    public <T> NavigableListAssert<T, DriverResultAssert, DriverResultAssert> asListOf(Class<T> clazz) {
        return null;
    }

    @Override
    protected RecordMapAssert toAssert(Map<String, Object> value, String description) {
        return null;
    }

    @Override
    protected DriverResultAssert newAbstractIterableAssert(Iterable<? extends Map<String, Object>> iterable) {
        return null;
    }
}
