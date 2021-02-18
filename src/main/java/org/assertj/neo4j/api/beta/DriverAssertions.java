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

import org.assertj.neo4j.api.beta.type.Request;
import org.assertj.neo4j.api.beta.type.loader.Nodes;
import org.assertj.neo4j.api.beta.type.loader.Relationships;
import org.neo4j.driver.Result;

/**
 * @author Patrick Allain - 5/26/20.
 */
public class DriverAssertions {

    public static DriverNodesAssert assertThat(final Nodes nodes) {
        return new DriverNodesAssert(nodes);
    }

    public static DriverRelationshipsAssert assertThat(final Relationships relationships) {
        return new DriverRelationshipsAssert(relationships);
    }

    public static DriverRequestAssert assertThat(final Request request) {
        return new DriverRequestAssert(request);
    }


    public static DriverResultAssert assertThat(final Result result) {
        return new DriverResultAssert(result);
    }

}
