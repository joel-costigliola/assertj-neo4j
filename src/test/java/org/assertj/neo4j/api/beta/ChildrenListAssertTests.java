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

import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.neo4j.api.beta.testing.Builders;
import org.assertj.neo4j.api.beta.testing.Samples;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.neo4j.api.beta.Bases.ConcreteParentAssert;
import static org.assertj.neo4j.api.beta.Bases.ConcreteRootAssert;
import static org.assertj.neo4j.api.beta.Bases.ROOT_ASSERT;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Patrick Allain - 21/02/2021
 */
class ChildrenListAssertTests {

    private static class BaseChildrenListAssertTests<ACTUAL> {

        protected ChildrenListAssert<ACTUAL, ConcreteParentAssert, ConcreteRootAssert> assertions;
        protected final List<ACTUAL> actual;

        @SafeVarargs
        BaseChildrenListAssertTests(ACTUAL... elements) {
            this.actual = Arrays.asList(elements);
        }

        @BeforeEach
        void setUp() {
            this.assertions = new ChildrenListAssert<>(this.actual, Builders.parent().build());
        }

    }

    @Nested
    class WithParentTests extends BaseChildrenListAssertTests<String> {

        WithParentTests() {
            super(Samples.STRING_LIST.toArray(new String[0]));
        }

        @Test
        void should_create_new_with_parent() {
            // GIVEN
            final ConcreteParentAssert newParentAssert = Mockito.mock(ConcreteParentAssert.class);
            when(newParentAssert.toRootAssert()).thenReturn(ROOT_ASSERT);
            when(newParentAssert.representation()).thenReturn(StandardRepresentation.STANDARD_REPRESENTATION);

            // WHEN
            ChildrenListAssert<String, ConcreteParentAssert, ConcreteRootAssert> result =
                    assertions.withParent(newParentAssert);

            // THEN
            verify(newParentAssert).representation();
            verify(newParentAssert).toRootAssert();
            verifyNoMoreInteractions(newParentAssert);
            assertThat(result)
                    .isInstanceOf(ChildrenListAssert.class)
                    .satisfies(i -> assertThat(i.toParentAssert()).isSameAs(newParentAssert))
                    .satisfies(i -> assertThat(i.getActual()).isEqualTo(this.actual));
        }
    }

}
