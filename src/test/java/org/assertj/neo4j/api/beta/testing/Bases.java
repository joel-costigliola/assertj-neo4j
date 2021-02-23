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

import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.neo4j.api.beta.ParentAssert;

import java.util.Optional;

/**
 * @author Patrick Allain - 22/02/2021
 */
public interface Bases {

    ConcreteRootAssert ROOT_ASSERT = new ConcreteRootAssert();

    class ConcreteRootAssert {

        private ConcreteRootAssert() {
        }

    }

    class ConcreteParentAssert implements ParentAssert<ConcreteRootAssert> {

        private final Representation representation;
        private final ConcreteRootAssert rootAssert;

        public ConcreteParentAssert(final Representation representation, final ConcreteRootAssert rootAssert) {
            this.representation = Optional.ofNullable(representation)
                    .orElse(StandardRepresentation.STANDARD_REPRESENTATION);
            this.rootAssert = rootAssert;
        }

        @Override
        public Representation representation() {
            return representation;
        }

        @Override
        public ConcreteRootAssert toRootAssert() {
            return rootAssert;
        }

    }
}
