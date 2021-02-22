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

import java.util.List;

/**
 * Navigable list assertions.
 *
 * @author Patrick Allain - 26/01/2021
 */
//@formatter:off
public class ChildrenListAssert<ELEMENT,
                                PARENT_ASSERT extends ParentalAssert,
                                ROOT_ASSERT>
        extends AbstractDbListAssert<ChildrenListAssert<ELEMENT, PARENT_ASSERT, ROOT_ASSERT>,
                                     List<ELEMENT>,
                                     ELEMENT,
                                     ChildrenListAssert<ELEMENT, ChildrenListAssert<ELEMENT,PARENT_ASSERT, ROOT_ASSERT>, ROOT_ASSERT>,
                                     PARENT_ASSERT,
                                     ROOT_ASSERT>
        implements Navigable<PARENT_ASSERT, ROOT_ASSERT>, Adoptable<ROOT_ASSERT> , ParentalAssert {
//@formatter:on

    ChildrenListAssert(final List<ELEMENT> actual, final PARENT_ASSERT parentAssert, final ROOT_ASSERT rootAssert) {
        super(
                actual,
                ChildrenListAssert.class,
                (a, s) -> new ChildrenListAssert<>(a, s, s.toRootAssert()),
                parentAssert,
                rootAssert
        );
    }

    @Override
    public ROOT_ASSERT toRootAssert() {
        return this.rootAssert()
                .orElseThrow(() -> new RuntimeException("Children assertion should have root assertions"));
    }

    /** {@inheritDoc} */
    @Override
    public <NEW_PARENT extends ParentalAssert> ChildrenListAssert<ELEMENT, NEW_PARENT, ROOT_ASSERT> withParent(final NEW_PARENT parentAssert) {
        return new ChildrenListAssert<>(actual, parentAssert, toRootAssert());
    }
}
