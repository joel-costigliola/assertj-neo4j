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

/**
 * Indicate that an assertion can be navigable.
 * <p/>
 * To be navigable, a instance should be {@link ParentAssert}.
 *
 * @author Patrick Allain - 01/01/2021
 */
public interface Navigable<PARENT_ASSERT extends ParentAssert<ROOT_ASSERT>, ROOT_ASSERT> extends ParentAssert<ROOT_ASSERT> {

    /**
     * Return to the parent assertion.
     *
     * @return the parent assertion.
     */
    PARENT_ASSERT toParentAssert();

}
