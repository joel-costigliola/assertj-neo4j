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

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.neo4j.driver.types.Node;

import java.util.stream.Stream;

/**
 * @author patouche - 29/09/2020
 */
public class ShouldAllHavePropertyTest {

    @Test
    public void errorMessage() {
        // GIVEN
        final ShouldAllHaveLabel error = ShouldAllHaveLabel.create("toto", Stream.<Node>builder().build());

        // WHEN
        final String message = error.create();

        // THEN
        Assertions.fail("TODO");
    }
}
