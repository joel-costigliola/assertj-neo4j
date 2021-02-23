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

import org.assertj.core.presentation.Representation;

/**
 * Interface for assertion which can be a parent assertion of another assertion.
 * <p/>
 * This interface indicate which behavior a children assertion will be able to retrieve from its parent.
 *
 * @author Patrick Allain - 15/02/2021
 */
interface ParentAssert<ROOT_ASSERT> {

    /**
     * Retrieve the {@link Representation} for render any object in error message.
     *
     * @return the currently use representation.
     */
    Representation representation();

    /**
     * Return to the root assertion.
     *
     * @return the root assertion.
     */
    ROOT_ASSERT toRootAssert();

}
