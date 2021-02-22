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
package org.assertj.neo4j.api.beta.type;

/**
 * @author Patrick Allain - 18/02/2021
 */
public interface Representable<I extends Representable<I>> extends Comparable<I > {

    /**
     * The object type.
     *
     * @return the
     */
    ObjectType objectType();

    /**
     * The object name description.
     *
     * @param quantity the object quantity
     * @return the object name description
     */
    default String objectName(int quantity) {
        return objectType().format(quantity);
    }

    /**
     * Short representation of the underlying object.
     *
     * @return a object short description.
     */
    String abbreviate();

    /**
     * Detailed representation of the underlying object.
     *
     * @return a object detailed description.
     */
    String detailed();

}
