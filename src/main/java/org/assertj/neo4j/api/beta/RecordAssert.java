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
import org.assertj.core.api.AssertFactory;
import org.neo4j.driver.Record;

/**
 * FIXME : Should be remove ?
 */
public class RecordAssert extends AbstractAssert<RecordAssert, Record> {

    public RecordAssert(final Record record) {
        super(record, Record.class);
    }

    public static class Factory implements AssertFactory<Record, RecordAssert> {

        @Override
        public RecordAssert createAssert(final Record record) {
            return null;
        }
    }
}
