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
package org.assertj.neo4j.api.beta.testing;

import org.assertj.neo4j.api.beta.testing.builders.NodeBuilder;
import org.assertj.neo4j.api.beta.testing.builders.RecordBuilder;
import org.assertj.neo4j.api.beta.testing.builders.RelationshipBuilder;
import org.assertj.neo4j.api.beta.testing.builders.ResultBuilder;

/**
 * @author Patrick Allain - 11/11/2020
 */
public interface Mocks {

    static ResultBuilder result() {
        return new ResultBuilder();
    }

    static RecordBuilder record() {
        return new RecordBuilder();
    }

    static NodeBuilder node() {
        return new NodeBuilder();
    }

    static RelationshipBuilder relation(final String type) {
        return new RelationshipBuilder(type);
    }

}
