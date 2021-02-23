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

import org.assertj.neo4j.api.beta.testing.Builders;
import org.assertj.neo4j.api.beta.type.DbNode;
import org.assertj.neo4j.api.beta.type.Models;
import org.assertj.neo4j.api.beta.type.loader.DataLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.neo4j.api.beta.testing.Bases.ConcreteParentAssert;
import static org.assertj.neo4j.api.beta.testing.Bases.ConcreteRootAssert;
import static org.assertj.neo4j.api.beta.testing.Bases.ROOT_ASSERT;

/**
 * @author Patrick Allain - 22/02/2021
 */
class ChildrenDriverNodeAssertTests {

    private static class BaseChildrenNodeTests {
        private final DbNode.DbNodeBuilder[] builders;
        private final DataLoader<DbNode> loader = Mockito.mock(DataLoader.class);

        protected ChildrenDriverNodeAssert<ConcreteParentAssert, ConcreteRootAssert> assertions;

        public BaseChildrenNodeTests(DbNode.DbNodeBuilder... builders) {
            this.builders = builders;
        }

        @BeforeEach
        void setUp() {
            final List<DbNode> nodes = Arrays.stream(this.builders)
                    .map(DbNode.DbNodeBuilder::build)
                    .collect(Collectors.toList());
            this.assertions = new ChildrenDriverNodeAssert<>(nodes, loader, Builders.parent().build());
        }

        @AfterEach
        void tearDown() {
            Mockito.verifyNoMoreInteractions(loader);
        }
    }

    @Nested
    class WithParentTests extends BaseChildrenNodeTests {

        public WithParentTests() {
            super(
                    Models.node().id(1).labels("lbl-1"),
                    Models.node().id(2).labels("lbl-2")
            );
        }

        @Test
        void should_attach_to_parent() {
            // GIVE?
            final ConcreteParentAssert newParentAssert = Builders.parent().build();

            // WHEN
            final ChildrenDriverNodeAssert<ConcreteParentAssert, ConcreteRootAssert> result =
                    this.assertions.withParent(newParentAssert);

            // THEN
            assertThat(result).isNotSameAs(this.assertions);
            assertThat(result.toParentAssert()).isSameAs(newParentAssert);
            assertThat(result.toRootAssert()).isSameAs(ROOT_ASSERT);

        }

    }
}
