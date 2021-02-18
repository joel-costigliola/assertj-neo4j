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
package org.assertj.neo4j.api.beta.type.loader;

import org.assertj.neo4j.api.beta.type.DbNode;
import org.neo4j.driver.Driver;

/**
 * {@link DbNode} entities loader definition.
 *
 * @author Patrick Allain - 31/10/2020
 */
public interface Nodes extends DataLoader<DbNode> {

    static Nodes of(Driver driver, String... labels) {
        return new NodeLoader(driver, labels);
    }

}

