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
 * Children assertions can be adopt by another parent assertions after it has been properly created.
 *
 * @author Patrick Allain - 27/01/2021
 */
public interface Adoptable<ROOT_ASSERT> {

    /**
     * Set the parent of the children entities
     *
     * @param parentAssert the parent assert
     * @return the self type
     */
    <NEW_PARENT> Navigable<NEW_PARENT, ROOT_ASSERT> withParent(NEW_PARENT parentAssert);

}
