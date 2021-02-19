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

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.DbObject;

import java.util.List;

/**
 * Factory for creating a {@link ErrorMessageFactory} from a list of elements.
 *
 * @param <ACTUAL> the type of actual elements.
 * @author Patrick Allain - 05/02/2021
 */
public interface GroupingDbErrorFactory<ACTUAL extends DbObject> {

    /**
     * Create a new {@link ErrorMessageFactory} for a group of entities.
     *
     * @param notSatisfies a list of entities that don't match the expected assertion condition.
     * @return a new {@link ErrorMessageFactory}
     */
    ErrorMessageFactory notSatisfies(final List<ACTUAL> notSatisfies);

}
