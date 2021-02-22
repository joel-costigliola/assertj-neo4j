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
import java.util.Objects;

/**
 * Children list assertions.
 *
 * @author Patrick Allain - 26/01/2021
 */
//@formatter:off
public class ChildrenListAssert<ELEMENT, PARENT_ASSERT extends ParentAssert<ROOT_ASSERT>, ROOT_ASSERT>
        extends AbstractDbListAssert<ChildrenListAssert<ELEMENT, PARENT_ASSERT, ROOT_ASSERT>,
                                     List<ELEMENT>,
                                     ELEMENT,
                                     ChildrenListAssert<ELEMENT,
                                                        ChildrenListAssert<ELEMENT, PARENT_ASSERT, ROOT_ASSERT>,
                                                        ROOT_ASSERT>,
                                     PARENT_ASSERT,
                                     ROOT_ASSERT>
        implements Adoptable<ROOT_ASSERT> {
//@formatter:on

    ChildrenListAssert(final List<ELEMENT> actual, final PARENT_ASSERT parentAssert) {
        super(
                actual,
                ChildrenListAssert.class,
                ChildrenListAssert::new,
                Objects.requireNonNull(parentAssert, "The parent assertion cannot be null."),
                Objects.requireNonNull(parentAssert.toRootAssert(), "The root assertion cannot be null.")
        );
    }

    /** {@inheritDoc} */
    @Override
    public <NEW_PARENT extends ParentAssert<ROOT_ASSERT>> ChildrenListAssert<ELEMENT, NEW_PARENT, ROOT_ASSERT> withParent(final NEW_PARENT parentAssert) {
        return new ChildrenListAssert<>(actual, parentAssert);
    }

}
